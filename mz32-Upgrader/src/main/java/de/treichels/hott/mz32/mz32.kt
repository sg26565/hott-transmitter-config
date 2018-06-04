package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.model.firmware.Firmware
import de.treichels.lzma.canCompress
import de.treichels.lzma.uncompress
import tornadofx.*
import java.io.File

class Mz32(private val root: File) {
    private val cfgFile = CfgFile.load(File(root, GRAUPNER_DISK_CFG))
    private val md5 = MD5Sum(root)

    @Suppress("MemberVisibilityCanBePrivate")
    val productName = cfgFile["Product name"]!!
    @Suppress("MemberVisibilityCanBePrivate")
    val productCode = cfgFile["Product code"]!!.toInt()
    @Suppress("MemberVisibilityCanBePrivate")
    val productVersion = cfgFile["Product ver."]!!.toFloat() / 1000
    //val rfidNumber = cfgFile["RFID number"]!!
    @Suppress("MemberVisibilityCanBePrivate")
    val updatePath = cfgFile["Update path"]!!
    //val userPath = cfgFile["User path"]!!.split(';').filter { !it.isBlank() }
    //val langPath = cfgFile["Lang path"]!!.split(';').filter { !it.isBlank() }
    //val langUser = cfgFile["Lang User"]!!.split(';').filter { !it.isBlank() }

    private val remotePath = "/Firmware/${TransmitterType.category}/$productCode"

    private inner class Path(p: String) {
        val value = if (p.startsWith("/")) p else "/$p"
        val type = PathType.valueOf(value.split("/")[1])
        val language by lazy {
            Language.valueOf(value.split("/")[2])
        }
        val targetFile = File(root, value).apply { parentFile.mkdirs() }
        var hash: Hash?
            get() = md5[value]
            set(hash) {
                if (hash != null) md5[value] = hash
            }

        fun isHelp() = type == PathType.Help
        fun isVoice() = type == PathType.Voice
        fun isUserVoice() = isVoice() && targetFile.parentFile.name == "10_User"
    }


    fun scan(task: FXTask<*>) {
        md5.clear()
        md5.scan(task)
        md5.save()
    }

    fun update(task: FXTask<*>, languages: List<Language>, updateResources: Boolean = true, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true, updateFirmware: Boolean = true) {
        if (task.isCancelled) return

        task.print("Checking for latest online versions ...\n")

        md5.load()
        TransmitterType.forProductCode(productCode).getFirmware().forEach { firmware ->

            if (firmware.name.endsWith(".bin") && updateFirmware) {
                updateFirmware(task, firmware)
            } else if (firmware.name.endsWith(".zip") && updateResources) {
                updateResources(task, languages, firmware, replaceHelpPages, replaceVoiceFiles)

                // delete VoiceList.lst in each language folder
                if (replaceVoiceFiles) languages.map { "/Voice/${it.name}/VoiceList.lst" }.forEach {
                    File(root, it).delete()
                }
            }
        }
        md5.save()

        task.print("\nall done")
    }

    private fun updateResources(task: FXTask<*>, languages: List<Language>, firmware: Firmware<TransmitterType>, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        task.print("\nUpdating resources ...\n")

        val remoteRoot = "$remotePath/${firmware.name.substringBeforeLast(".zip")}.lzma"
        //println("updateResources: remoteRoot = $remoteRoot")

        task.updateProgress(0.0, 1.0)

        try {
            // process remote md5sum.txt
            MD5Sum().apply {
                task.print("\tDownloading md5sum.txt from server ... ")
                Firmware.download("$remoteRoot//md5sum.txt") { load(it) }
                task.print("ok\n")

                entries.forEachIndexed { index, entry ->
                    if (!task.isCancelled) {
                        val path = Path(entry.key)
                        val hash = entry.value

                        when {
                            path.isHelp() -> if (replaceHelpPages && languages.contains(path.language)) updateFileOnline(task, remoteRoot, path, hash)
                            path.isVoice() -> if (replaceVoiceFiles && languages.contains(path.language)) updateFileOnline(task, remoteRoot, path, hash)
                            else -> updateFileOnline(task, remoteRoot, path, hash)
                        }
                    }

                    task.updateProgress(index.toLong(), size.toLong())
                }
            }

            md5.save()
            task.print("done\n")
        } catch (e: Exception) {
            // fall back to resource file download
            task.print("failed: $e\n")
        }
    }

    private fun updateFileOnline(task: FXTask<*>, root: String, path: Path, hash: Hash) {
        if (task.isCancelled) return

        val targetFile = path.targetFile

        if (!targetFile.exists() || targetFile.length() != hash.size || hash != path.hash) {
            task.print("\tDownloading ${path.value} from server ... ")
            try {
                if (canCompress(targetFile.extension))
                    Firmware.download("$root${path.value}.lzma") { inputStream ->
                        uncompress(inputStream, targetFile.outputStream())
                    }
                else
                    Firmware.download(task, "$root${path.value}", file = targetFile)

                path.hash = hash
                task.print("ok\n")

                cleanup(task, targetFile, path)
            } catch (e: Exception) {
                task.print("failed: $e\n")
            }
        }
    }

    private fun cleanup(task: FXTask<*>, targetFile: File, path: Path) {
        // cleanup
        val number = targetFile.number()
        if (path.isHelp() || path.isVoice() && !path.isUserVoice()) {
            targetFile.parentFile.listFiles().filter { it != targetFile && it.number() == number }.forEach {
                task.print("\tremoving obsolete file ${it.absolutePath}")
                it.delete()
            }
        }
    }

    private fun File.number() = name.substring(0, 3).toInt()

    private fun updateFirmware(task: FXTask<*>, firmware: Firmware<TransmitterType>) {
        if (task.isCancelled) return

        task.print("\nFound firmware ${firmware.name} ...\n")

        val targetDir = File(root, updatePath)
        val targetFile = File(targetDir, firmware.name)
        val path = Path("$updatePath/${firmware.name}")

        if (!targetFile.exists() || targetFile.length() != firmware.size || firmware.hash != path.hash?.hash) {
            task.print("\tDownloading $targetFile from server ... ")
            val hash = Firmware.download(task, "$remotePath/${firmware.name}", firmware.size, targetFile)
            path.hash = Hash(firmware.size, hash)
            task.print("ok\n")
        } else {
            task.print("\t$targetFile is uptodate\n")
        }

        md5.save()
        task.print("done\n")
    }

    override fun toString(): String {
        return "$root ($productName [$productCode] v$productVersion)"
    }

    companion object {
        private const val GRAUPNER_DISK_CFG = "/GraupnerDisk.cfg"

        fun find(): List<Mz32> = File.listRoots().mapNotNull {
            try {
                Mz32(it).apply {
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}

internal fun FXTask<*>.print(newMessage: String) {
    runLater {
        updateMessage(message + newMessage)
    }
}
