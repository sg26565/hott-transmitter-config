package de.treichels.hott.mz32

import de.treichels.hott.decoder.Firmware
import de.treichels.hott.decoder.getFirmware
import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.ui.CallbackAdapter
import de.treichels.hott.ui.ExceptionDialog
import de.treichels.hott.util.hash
import de.treichels.lzma.canCompress
import de.treichels.lzma.uncompress
import tornadofx.*
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.CancellationException
import java.util.concurrent.CountDownLatch
import java.util.logging.Logger
import java.util.regex.Pattern

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

        try {
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
        } catch (e: CancellationException) {
            task.print("cancelled.\n")
        } catch (e: Exception) {
            task.print("failed: $e\n")
            showError(e)
        }

        if (task.isCancelled)
            task.print("\nDownload was cancelled\n")
        else
            task.print("\nall done\n")
    }

    private fun findLatest(task: FXTask<*>) = Firmware.download(CallbackAdapter(task), "$remotePath/latest.txt") { inputStream, _ ->
        task.print("Checking for latest online versions ... ")
        val cfg = CfgFile.load(inputStream)
        task.print("ok\n")

        Pair(cfg["firmware"]!!, cfg["resources"]!!)
    }

    private fun updateResources(task: FXTask<*>, languages: List<Language>, firmware: String, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        task.print("\nFound resources $firmware ...\n")

        val remoteRoot = "$remotePath/$firmware"

        // process remote md5sum.txt
        val remoteMd5 = MD5Sum().apply {
            task.print("\tDownloading remote md5sum.txt from server ... ")
            Firmware.download(CallbackAdapter(task), "$remoteRoot//md5sum.txt") { inputStream, _ ->
                load(inputStream)
            }
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
                    path.isManual -> if (languages.contains(path.language)) updateFileOnline(task, remoteRoot, path, hash)
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
        files.removeIf { remoteMd5.keys.contains(it.toLowerCase()) }

        val toBeDeleted = files.asSequence()
                // convert to path
                .map { Path(it) }
                .filter { it.isAutoDelete }
                // keep protected entries
                .filterNot { it.isProtected }
                // keep help pages unless replaceHelpPages was selected
                .filterNot { it.isHelp && !replaceHelpPages }
                // keep voice files unless replaceVoiceFiles was selected
                .filterNot { it.isVoice && !replaceVoiceFiles }
                // convert to string
                .map { it.value }.toMutableList()

        // special handling of /System/Revision_rXXXX.txt
        toBeDeleted += files.asSequence()
                .filter { revisions.matcher(it).matches() }
                .filterNot { remoteMd5.containsKey(it) }

        toBeDeleted.sorted().forEach {
            // delete remaining entries
            task.print("\tRemoving obsolete file $it\n")

            File(rootDir, it).apply {
                if (exists()) delete()
            }

            md5.remove(it)
        }

        task.print("done\n")

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
            if (localHash == null)
                task.print("\t$file\tMissing checksum ... ")
            else
                task.print("\t$file\tStale checksum ... ")

            localHash = Hash(fileSize, file.hash())
            md5[filePath] = localHash
            task.print("ok\n")
        }

        log.fine("Local file: ${file.absolutePath} (${if (file.exists()) "$fileSize Bytes" else "missing"}), $localHash")

        // if target file does not exist or has different size as remote or has different remoteHash
        if (!fileExists || (fileSize != remoteSize || remoteHash != localHash) && !path.isUser && !path.isLangUser) {
            when {
                !fileExists -> task.print("\t$file\tNew file ... ")
                fileSize != remoteSize -> task.print("\t$file\tSize mismatch ... ")
                remoteHash != localHash -> task.print("\t$file\tChecksum mismatch ... ")
            }

            // download into temporary file
            val tempFile = File(file.parent, "${file.name}.tmp")
            val callback = CallbackAdapter(task)

            try {
                if (canCompress(file.extension))
                    Firmware.download(callback, "$root$filePath.lzma") { inputStream, _ -> uncompress(inputStream, tempFile.outputStream()) }
                else
                    Firmware.download(callback, "$root$filePath", tempFile)

                // replace target file only after successfull download
                Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)

                md5[filePath] = remoteHash
                task.print("ok\n")
            } finally {
                if (tempFile.exists()) tempFile.delete()
            }
        }
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
            // download into temporary file
            val tempFile = File(file.parent, "${file.name}.tmp")

            task.print("\tDownloading $file from server ... ")
            val hash = Firmware.download(CallbackAdapter(task), "$remotePath/$fileName", tempFile)

            // replace target file only after successfull download
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)

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
        private val revisions = Pattern.compile("/System/Revision_r[0-9]*\\.txt")
        private val log = Logger.getLogger(this::class.qualifiedName)

        fun find(): List<Mz32> {
            // File roots
            val canditates = File.listRoots().toMutableSet()

            // FileSystem roots
            try {
                canditates.addAll(FileSystems.getDefault().rootDirectories.map { it.toFile() })
            } catch (e: Exception) {
                // ignore
            }

            // /proc/mounts
            try {
                val procMounts = File("/proc/mounts")
                if (procMounts.exists())
                    canditates.addAll(procMounts.readLines().map { File(it.split(" ")[1]) })
            } catch (e: Exception) {
                // ignore
            }

            // mount command
            try {
                Runtime.getRuntime().exec("mount").apply {
                    waitFor()
                    if (exitValue() == 0)
                        canditates.addAll(inputStream.reader().readLines().map {
                            val path = it.substringAfter(" on ").substringBeforeLast(" (").trim()
                            File(path)
                        })
                }
            } catch (e: Exception) {
                // ignore
            }

            return canditates.mapNotNull {
                try {
                    Mz32(it)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}

internal fun FXTask<*>.print(newMessage: String) {
    runLater {
        updateMessage(newMessage)
    }
}
