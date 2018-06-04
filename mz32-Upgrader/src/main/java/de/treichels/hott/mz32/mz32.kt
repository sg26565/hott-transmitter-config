package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.model.firmware.Firmware
import de.treichels.hott.util.copyTo
import de.treichels.hott.util.extract
import de.treichels.hott.util.hash
import de.treichels.lzma.canCompress
import de.treichels.lzma.uncompress
import tornadofx.*
import java.io.File
import java.util.logging.Logger
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.streams.toList

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
            updateResourcesDownload(task, firmware, languages, replaceHelpPages, replaceVoiceFiles)
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

    private fun updateResourcesDownload(task: FXTask<*>, firmware: Firmware<TransmitterType>, languages: List<Language>, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        if (!firmware.isCached) {
            task.print("\tDownloading resources ${firmware.name} ... ")
            try {
                firmware.download(task)
                task.print("ok\n")
            } catch (e: Exception) {
                task.print("failed: $e\n")
            }
        }

        val resourceFile = firmware.file
        task.print("\tUpdating resources from file ${resourceFile.name}\n")

        val zipFile = ZipFile(resourceFile, Charsets.ISO_8859_1)
        val entries = zipFile.stream().filter { !it.isDirectory }.toList()

        entries.forEachIndexed { index, entry ->
            if (!task.isCancelled) {
                val path = Path(entry.name)

                when {
                    path.isHelp() -> if (replaceHelpPages && languages.contains(path.language)) updateFileFromZip(task, entry, zipFile, path)
                    path.isVoice() -> if (replaceVoiceFiles && languages.contains(path.language)) updateFileFromZip(task, entry, zipFile, path)
                    else -> updateFileFromZip(task, entry, zipFile, path)
                }
            }

            task.updateProgress(index.toLong(), entries.size.toLong())
        }

        md5.save()
        task.print("done\n")
    }

    private fun updateFileFromZip(task: FXTask<*>, entry: ZipEntry, zipFile: ZipFile, path: Path) {
        if (task.isCancelled) return

        val expectedSize = entry.size
        val targetFile = path.targetFile

        if (!targetFile.exists() || targetFile.length() != expectedSize || Hash(expectedSize, zipFile.hash(entry)) != path.hash) {
            task.print("\tinstalling $targetFile ... ")
            path.hash = Hash(expectedSize, zipFile.extract(task, entry, targetFile))
            task.print("ok\n")
            cleanup(task, targetFile, path)
        }
    }

    private fun updateFirmware(task: FXTask<*>, firmware: Firmware<TransmitterType>) {
        if (task.isCancelled) return

        task.print("\nUpdating firmware ...\n")

        if (!firmware.isCached) {
            task.print("\tDownloading firmware ${firmware.name} ... ")
            try {
                firmware.download(task)
                task.print("ok\n")
            } catch (e: Exception) {
                task.print("failed: $e\n")
            }
        }

        val firmwareFile = firmware.file
        val targetDir = File(root, updatePath)
        val targetFile = File(targetDir, firmwareFile.name)
        val path = Path("$updatePath/${firmwareFile.name}")

        if (!targetFile.exists() || targetFile.length() != firmwareFile.length() || firmware.hash != path.hash?.hash) {
            task.print("\tinstalling $targetFile ... ")
            firmwareFile.copyTo(task, targetFile, overwrite = true)
            path.hash = Hash(firmwareFile.length(), firmwareFile.hash())
            task.print("ok\n")
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
