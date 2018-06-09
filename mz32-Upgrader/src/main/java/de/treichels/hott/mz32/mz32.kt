package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.model.firmware.Firmware
import de.treichels.lzma.canCompress
import de.treichels.lzma.uncompress
import org.controlsfx.dialog.ExceptionDialog
import tornadofx.*
import java.io.File
import java.util.concurrent.CountDownLatch

class Mz32(private val root: File) {
    private val cfgFile = CfgFile.load(File(root, GRAUPNER_DISK_CFG))
    private val md5 = MD5Sum(root)

    @Suppress("MemberVisibilityCanBePrivate")
    val productName = cfgFile["Product name"]!!
    @Suppress("MemberVisibilityCanBePrivate")
    val productCode = cfgFile["Product code"]!!.toInt()
    @Suppress("MemberVisibilityCanBePrivate")
    val productVersion = cfgFile["Product ver."]!!.toFloat() / 1000
    @Suppress("unused")
    val rfidNumber = cfgFile["RFID number"]!!
    @Suppress("MemberVisibilityCanBePrivate")
    val updatePath = cfgFile["Update path"]!!
    //val userPath = cfgFile["User path"]!!.split(';').filter { !it.isBlank() }
    //val langPath = cfgFile["Lang path"]!!.split(';').filter { !it.isBlank() }
    //val langUser = cfgFile["Lang User"]!!.split(';').filter { !it.isBlank() }.map { "^" + it.replace("*", "[^/]*") + ".*$" }.map { Regex(it) }

    private val remotePath = "/Firmware/${TransmitterType.category}/$productCode"

    fun scan(task: FXTask<*>) {
        md5.clear()
        md5.scan(task)
        md5.save()
    }

    fun update(task: FXTask<*>, languages: List<Language>, updateResources: Boolean = true, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true, updateFirmware: Boolean = true) {
        if (task.isCancelled) return

        task.print("Checking for latest online versions ...\n")

        md5.load()
        TransmitterType.forProductCode(productCode).getFirmware().sortedBy { it.name }.forEach { firmware ->
            if (firmware.name.endsWith(".bin") && updateFirmware)
                updateFirmware(task, firmware)
            else if (firmware.name.endsWith(".zip") && updateResources)
                updateResources(task, languages, firmware, replaceHelpPages, replaceVoiceFiles)
        }
        md5.save()

        if (task.isCancelled)
            task.print("\ncancelled\n")
        else
            task.print("\nall done\n")
    }

    private fun updateResources(task: FXTask<*>, languages: List<Language>, firmware: Firmware<TransmitterType>, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        task.print("\nFound resources ${firmware.name} ...\n")

        val remoteRoot = "$remotePath/${firmware.name.substringBeforeLast(".zip")}.lzma"
        //println("updateResources: remoteRoot = $remoteRoot")

        task.updateProgress(0.0, 1.0)

        try {
            // process remote md5sum.txt
            MD5Sum().apply {
                task.print("\tDownloading remote md5sum.txt from server ... ")
                Firmware.download("$remoteRoot//md5sum.txt") { load(it) }
                task.print("ok\n")

                if (task.isCancelled) return
                task.print("\tChecking for new or updated resource files ...\n")

                entries.forEachIndexed { index, entry ->
                    if (task.isCancelled) return

                    val path = Path(entry.key)
                    val hash = entry.value

                    when {
                        path.isHelp -> if (replaceHelpPages && languages.contains(path.language)) updateFileOnline(task, remoteRoot, path, hash)
                        path.isVoice -> if (replaceVoiceFiles && languages.contains(path.language)) updateFileOnline(task, remoteRoot, path, hash)
                        else -> updateFileOnline(task, remoteRoot, path, hash)
                    }
                    task.updateProgress(index.toLong(), size.toLong())
                }
            }

            if (task.isCancelled) return
            task.print("done\n")
        } catch (e: Exception) {
            task.print("failed: $e\n")
            showError(e)
        }

        // delete VoiceList.lst in each language folder
        if (replaceVoiceFiles) languages.map { "/Voice/${it.name}/VoiceList.lst" }.forEach {
            File(root, it).delete()
        }
    }

    private fun updateFileOnline(task: FXTask<*>, root: String, path: Path, hash: Hash) {
        if (task.isCancelled) return

        val targetFile = path.targetFile

        if (!targetFile.exists() || (targetFile.length() != hash.size || hash != path.hash) && !path.isUser && !path.isLangUser) {
            task.print("\tDownloading ${path.value} from server ... ")
            try {
                if (canCompress(targetFile.extension))
                    Firmware.download("$root${path.value}.lzma") { inputStream -> uncompress(inputStream, targetFile.outputStream()) }
                else
                    Firmware.download(task, "$root${path.value}", file = targetFile)

                path.hash = hash
                task.print("ok\n")

                if (task.isCancelled) return

                // cleanup lang files (/Help and /Voice, except /Voice/*/10_*)
                if (path.isLang && !path.isLangUser) {
                    val number = targetFile.number()
                    targetFile.parentFile.listFiles().forEach { file ->
                        try {
                            if (file != targetFile && file.number() == number) {
                                task.print("\tRemoving obsolete file ${file.absolutePath}\n")
                                file.delete()
                            }
                        } catch (e: Exception) {
                            // ignore
                        }
                    }
                }
            } catch (e: Exception) {
                task.print("failed: $e\n")
                showError(e)
            }
        }
    }

    private fun File.number() = NUM_REGEX.matchEntire(name)!!.groupValues[1].toInt()

    private val Path.targetFile
        get() = File(root, value).apply { parentFile.mkdirs() }
    private var Path.hash: Hash?
        get() = md5[value]
        set(hash) {
            if (hash != null) md5[value] = hash
        }

    private fun showError(e: Exception) {
        val latch = CountDownLatch(1)

        runLater {
            ExceptionDialog(e).showAndWait()
            latch.countDown()
        }

        latch.await()
    }

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
        private val NUM_REGEX = Regex("^([0-9]+).*$")
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
