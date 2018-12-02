package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.util.ExceptionDialog
import de.treichels.hott.firmware.Firmware
import de.treichels.hott.firmware.getFirmware
import de.treichels.hott.util.hash
import de.treichels.lzma.canCompress
import de.treichels.lzma.uncompress
import tornadofx.*
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.logging.Logger

class Mz32(private val rootDir: File) {
    private val cfgFile = CfgFile.load(File(rootDir, GRAUPNER_DISK_CFG))
    private val md5 = MD5Sum(rootDir)

    @Suppress("MemberVisibilityCanBePrivate")
    val productName: String = cfgFile["Product name"]!!
    @Suppress("MemberVisibilityCanBePrivate")
    val productCode: Int = cfgFile["Product code"]!!.toInt()
    @Suppress("MemberVisibilityCanBePrivate")
    val productVersion: Float = cfgFile["Product ver."]!!.toFloat() / 1000
    @Suppress("unused")
    val rfidNumber: String = cfgFile["RFID number"]!!
    @Suppress("MemberVisibilityCanBePrivate")
    val updatePath: String = cfgFile["Update path"]!!
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

        task.updateProgress(0.0, 1.0)

        val (latestFirmware, latestResources) = findLatest(task)

        md5.load()
        task.updateProgress(0.0, 1.0)
        if (updateResources) updateResources(task, languages, latestResources, replaceHelpPages, replaceVoiceFiles)

        task.updateProgress(0.0, 1.0)
        if (updateFirmware) TransmitterType.forProductCode(productCode).getFirmware().firstOrNull { it.name == latestFirmware }?.apply {
            updateFirmware(task, this)
        }

        md5.save()

        if (task.isCancelled)
            task.print("\ncancelled\n")
        else
            task.print("\nall done\n")
    }

    private fun findLatest(task: FXTask<*>) = Firmware.download("$remotePath/latest.txt") { inputStream, _ ->
        task.print("Checking for latest online versions ... ")
        val cfg = CfgFile.load(inputStream)
        task.print("ok\n")

        Pair(cfg["firmware"]!!, cfg["resources"]!!)
    }

    private fun updateResources(task: FXTask<*>, languages: List<Language>, firmware: String, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        task.print("\nFound resources $firmware ...\n")

        val remoteRoot = "$remotePath/$firmware"

        try {
            // process remote md5sum.txt
            val remoteMd5 = MD5Sum().apply {
                task.print("\tDownloading remote md5sum.txt from server ... ")
                Firmware.download("$remoteRoot//md5sum.txt") { inputStream, _ -> load(inputStream) }
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

            // check for obsolete local md5 entries and files
            val files = rootDir.walk().iterator().asSequence()
                    // ignore directories
                    .filter { it.isFile }
                    // get the relative path
                    .map { "/${it.relativeTo(rootDir).path}" }
                    // convert path separators on Windows
                    .map { it.replace(File.separatorChar, '/') }
                    // collect to a mutable set
                    .toMutableSet()

            // add keys from md5sum.txt
            files.addAll(md5.keys)

            // keep entries in remote md5sum.txt
            files.removeAll(remoteMd5.keys)

            val toBeDeleted = files.asSequence()
                    // convert to path
                    .map { Path(it) }
                    // keep protected entries
                    .filterNot { it.isProtected }
                    // keep help pages unless replaceHelpPages was selected
                    .filterNot { it.isHelp && !replaceHelpPages }
                    // keep voice files unless replaceVoiceFiles was selected
                    .filterNot { it.isVoice && !replaceVoiceFiles }
                    // convert to string
                    .map { it.value }.toList().sorted()

            toBeDeleted.forEach {
                // delete remaining entries
                task.print("\tRemoving obsolete file $it\n")

                File(rootDir, it).apply {
                    if (exists()) delete()
                }

                md5.remove(it)
            }

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
        log.fine("Remote file: $root/$filePath, $remoteHash")

        // local file on transmitter
        val file = File(rootDir, filePath).apply { parentFile.mkdirs() }
        val fileExists = file.exists()

        // local file size
        val fileSize = file.length()

        // local hash according to local md5sum.txt
        var localHash = md5[filePath]

        // local size according to local md5sum.txt
        val localSize = localHash?.size ?: 0L

        // remote size according to remote md5sum.txt
        val remoteSize = remoteHash.size


        // re-calculate missing or invalid hash if file exists and has same size as remote
        if (fileExists && fileSize == remoteSize && (localHash == null || localSize != fileSize)) {
            task.print("\tCalculating checksum for $file ... ")
            localHash = Hash(fileSize, file.hash())
            md5[filePath] = localHash
            task.print("ok\n")
        }

        log.fine("Local file: ${file.absolutePath} (${if (file.exists()) "$fileSize Bytes" else "missing"}), $localHash")

        // if target file does not exist or has different size as remote or has different remoteHash
        if (!fileExists || (fileSize != remoteSize || remoteHash != localHash) && !path.isUser && !path.isLangUser) {
            task.print("\tDownloading $file from server ... ")
            try {
                if (canCompress(file.extension))
                    Firmware.download("$root$filePath.lzma") { inputStream, _ -> uncompress(inputStream, file.outputStream()) }
                else
                    Firmware.download(task, "$root$filePath", file)

                md5[filePath] = remoteHash
                task.print("ok\n")
            } catch (e: Exception) {
                task.print("failed: $e\n")
                showError(e)
            }
        }

        if (task.isCancelled) return
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

        val fileName = firmware.name
        val filePath = "$updatePath/$fileName"
        val file = File(rootDir, filePath)
        val fileSize = file.length()
        val firmwareSize = firmware.size

        task.print("\nFound firmware $fileName ...\n")

        if (!file.exists() || fileSize != firmwareSize) {
            task.print("\tDownloading $file from server ... ")
            val hash = Firmware.download(task, "$remotePath/$fileName", file)
            md5[filePath] = Hash(firmwareSize, hash)
            task.print("ok\n")
        } else {
            task.print("\t$file is uptodate\n")
        }

        md5.save()
        task.print("done\n")
    }

    override fun toString(): String = "$rootDir ($productName [$productCode] v$productVersion)"

    companion object {
        private const val GRAUPNER_DISK_CFG = "/GraupnerDisk.cfg"
        private val log = Logger.getLogger(this::class.qualifiedName)

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
        updateMessage(newMessage)
    }
}
