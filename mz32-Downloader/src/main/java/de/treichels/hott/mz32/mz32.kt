package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.model.firmware.Firmware
import de.treichels.hott.util.hash
import de.treichels.lzma.canCompress
import de.treichels.lzma.uncompress
import org.controlsfx.dialog.ExceptionDialog
import tornadofx.*
import java.io.File
import java.util.concurrent.CountDownLatch

class Mz32(private val rootDir: File) {
    private val cfgFile = CfgFile.load(File(rootDir, GRAUPNER_DISK_CFG))
    private val md5 = MD5Sum(rootDir)

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

    fun scan(task: FXTask<*>, languages: List<Language>) {
        md5.clear()
        md5.scan(task, languages)
        md5.save()
    }

    fun update(task: FXTask<*>, languages: List<Language>, updateResources: Boolean = true, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true, updateFirmware: Boolean = true) {
        if (task.isCancelled) return

        val (latestFirmware, latestResources) = findLatest(task)

        md5.load()
        if (updateResources) updateResources(task, languages, latestResources, replaceHelpPages, replaceVoiceFiles)
        if (updateFirmware) TransmitterType.forProductCode(productCode).getFirmware().firstOrNull { it.name == latestFirmware }?.apply {
            updateFirmware(task, this)
        }

        md5.save()

        if (task.isCancelled)
            task.print("\ncancelled\n")
        else
            task.print("\nall done\n")
    }

    private fun findLatest(task: FXTask<*>) = Firmware.download("$remotePath/latest.txt") {
        task.print("Checking for latest online versions ... ")
        val cfg = CfgFile.load(it)
        task.print("ok\n")

        Pair(cfg["firmware"]!!, cfg["resources"]!!)
    }

    private fun updateResources(task: FXTask<*>, languages: List<Language>, firmware: String, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        task.print("\nFound resources $firmware ...\n")

        val remoteRoot = "$remotePath/$firmware"

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
            File(rootDir, it).delete()
        }
    }

    private fun updateFileOnline(task: FXTask<*>, root: String, path: Path, remoteHash: Hash) {
        if (task.isCancelled) return

        val filePath = path.value

        // local file on transmitter
        val file = File(rootDir, filePath).apply { parentFile.mkdirs() }

        // local file size
        val fileSize = file.length()

        // local hash according to local md5sum.txt
        val localHash = md5[filePath]

        // local size according to local md5sum.txt
        val localSize = localHash?.size ?: 0L

        // remote size according to remote md5sum.txt
        val remoteSize = remoteHash.size

        // re-calculate missing or invalid hash if file exists and has same size as remote
        if (file.exists() && fileSize == remoteSize && (localHash == null || localSize != fileSize)) {
            task.print("\tCalculating checksum for $file ... ")
            val hash = Hash(fileSize, file.hash())
            md5[filePath] = hash
            task.print("ok\n")
        }

        // if target file does not exist or has different size as remote or has different remoteHash
        if (!file.exists() || (fileSize != remoteSize || remoteHash !=  md5[filePath]) && !path.isUser && !path.isLangUser) {
            task.print("\tDownloading $file from server ... ")
            try {
                if (canCompress(file.extension))
                    Firmware.download("$root$filePath.lzma") { inputStream -> uncompress(inputStream, file.outputStream()) }
                else
                    Firmware.download(task, "$root$filePath", file = file)

                md5[filePath] = remoteHash
                task.print("ok\n")

                if (task.isCancelled) return

                // cleanup lang files (/Help and /Voice, except /Voice/*/10_*)
                if (path.isLang && !path.isLangUser) {
                    val number = file.number()
                    file.parentFile.listFiles().forEach {
                        try {
                            if (it != it && it.number() == number) {
                                task.print("\tRemoving obsolete file $it\n")
                                it.delete()
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

        val fileName = firmware.name
        val filePath = "$updatePath/$fileName"
        val file = File(rootDir, filePath)
        val fileSize = file.length()
        val firmwareSize = firmware.size

        task.print("\nFound firmware $fileName ...\n")

        if (!file.exists() || fileSize != firmwareSize) {
            task.print("\tDownloading $file from server ... ")
            val hash = Firmware.download(task, "$remotePath/$fileName", firmwareSize, file)
            md5[filePath] = Hash(firmwareSize, hash)
            task.print("ok\n")
        } else {
            task.print("\t$file is uptodate\n")
        }

        md5.save()
        task.print("done\n")
    }

    override fun toString(): String {
        return "$rootDir ($productName [$productCode] v$productVersion)"
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
