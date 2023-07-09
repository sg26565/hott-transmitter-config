package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.model.firmware.Firmware
import de.treichels.hott.model.firmware.getFirmware
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
import java.text.MessageFormat
import java.util.*
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

    private val messages = ResourceBundle.getBundle(Mz32Downloader::class.java.canonicalName)
    private val remotePath = "/Firmware/${TransmitterType.category}/$productCode"

    fun scan(task: FXTask<*>, languages: List<Language>) {
        md5.clear()
        md5.scan(task, languages)
        md5.save()
    }

    fun update(
        task: FXTask<*>,
        languages: List<Language>,
        updateResources: Boolean = true,
        updateHelpPages: Boolean = true,
        updateVoiceFiles: Boolean = true,
        updateManuals: Boolean = true,
        updateWidgets: Boolean = true,
        updateImages: Boolean = true,
        updateMaps: Boolean = true,
        updateUtils: Boolean = true,
        updateFirmware: Boolean = true
    ) {
        if (task.isCancelled) return

        try {
            task.updateProgress(0.0, 1.0)

            val (latestFirmware, latestResources) = findLatest(task)

            md5.load()
            task.updateProgress(0.0, 1.0)
            if (updateResources) updateResources(
                task,
                languages,
                latestResources,
                updateHelpPages,
                updateVoiceFiles,
                updateManuals,
                updateWidgets,
                updateImages,
                updateMaps,
                updateUtils
            )

            task.updateProgress(0.0, 1.0)
            if (updateFirmware) TransmitterType.forProductCode(productCode).getFirmware()
                .firstOrNull { it.name == latestFirmware }?.apply {
                    updateFirmware(task, this)
                }

            md5.save()
        } catch (e: CancellationException) {
            task.print(messages["cancelled"])
        } catch (e: Exception) {
            task.print(MessageFormat.format(messages["failed"], e))
            showError(e)
        }

        if (task.isCancelled)
            task.print(messages["download_cancelled"])
        else
            task.print(messages["all_done"])
    }

    private fun findLatest(task: FXTask<*>) =
        Firmware.download(CallbackAdapter(task), "$remotePath/latest.txt") { inputStream, _ ->
            task.print(messages["checking_online"])
            val cfg = CfgFile.load(inputStream)
            task.print(messages["ok"])

            Pair(cfg["firmware"]!!, cfg["resources"]!!)
        }

    private fun updateResources(
        task: FXTask<*>,
        languages: List<Language>,
        firmware: String,
        updateHelpPages: Boolean = true,
        updateVoiceFiles: Boolean = true,
        updateManuals: Boolean = true,
        updateWidgets: Boolean = true,
        updateImages: Boolean = true,
        updateMaps: Boolean = true,
        updateUtils: Boolean = true
    ) {
        if (task.isCancelled) return

        task.print(MessageFormat.format(messages["found_resources"], firmware))

        val remoteRoot = "$remotePath/$firmware"

        // process remote md5sum.txt
        val remoteMd5 = MD5Sum().apply {
            task.print("$remoteRoot/md5sum.txt\n")
            task.print(messages["downloading.remote.md5sum.txt.from.server"])
            Firmware.download(CallbackAdapter(task), "$remoteRoot/md5sum.txt") { inputStream, _ ->
                load(inputStream)
            }
            task.print(messages["ok"])

            if (task.isCancelled) return
            task.print(messages["checking_resource_files"])

            entries.forEachIndexed { index, entry ->
                if (task.isCancelled) return

                val path = Path(entry.key)
                val hash = entry.value

                when {
                    path.isHelp -> if (updateHelpPages && languages.contains(path.language)) updateFileOnline(
                        task,
                        remoteRoot,
                        path,
                        hash
                    )
                    path.isVoice -> if (updateVoiceFiles && languages.contains(path.language)) updateFileOnline(
                        task,
                        remoteRoot,
                        path,
                        hash
                    )
                    path.isManual -> if (updateManuals && languages.contains(path.language)) updateFileOnline(
                        task,
                        remoteRoot,
                        path,
                        hash
                    )
                    path.isWidgets -> if (updateWidgets) updateFileOnline(
                        task,
                        remoteRoot,
                        path,
                        hash
                    )
                    path.isImage -> if (updateImages) updateFileOnline(
                        task,
                        remoteRoot,
                        path,
                        hash
                    )
                    path.isMaps -> if (updateMaps) updateFileOnline(
                        task,
                        remoteRoot,
                        path,
                        hash
                    )
                    path.isUtils -> if (updateUtils) updateFileOnline(
                        task,
                        remoteRoot,
                        path,
                        hash
                    )
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
            // keep help pages unless updateHelpPages was selected
            .filterNot { it.isHelp && !updateHelpPages }
            // keep voice files unless updateVoiceFiles was selected
            .filterNot { it.isVoice && !updateVoiceFiles }
            // keep voice files unless updateManuals was selected
            .filterNot { it.isManual && !updateManuals }
            // convert to string
            .map { it.value }.toMutableList()

        // special handling of /System/Revision_rXXXX.txt
        toBeDeleted += files.asSequence()
            .filter { revisions.matcher(it).matches() }
            .filterNot { remoteMd5.containsKey(it) }

        toBeDeleted.sorted().forEach {
            // delete remaining entries
            task.print(MessageFormat.format(messages["removing_obsolete_file"], it))

            File(rootDir, it).apply {
                if (exists()) delete()
            }

            md5.remove(it)
        }

        task.print(messages["done"])

        // delete VoiceList.lst in each language folder
        if (updateVoiceFiles) languages.map { "/Voice/${it.name}/VoiceList.lst" }.forEach {
            File(rootDir, it).delete()
        }
    }

    private fun updateFileOnline(task: FXTask<*>, root: String, path: Path, remoteHash: Hash) {
        if (task.isCancelled) return

        val filePath = path.value
        log.fine(MessageFormat.format(messages["remote_file"], root, filePath, remoteHash))

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
                task.print(MessageFormat.format(messages["missing_checksum"], file))
            else
                task.print(MessageFormat.format(messages["stale_checksum"], file))

            localHash = Hash(fileSize, file.hash())
            md5[filePath] = localHash
            task.print(messages["ok"])
        }

        log.fine("Local file: ${file.absolutePath} (${if (file.exists()) "$fileSize Bytes" else "missing"}), $localHash")

        // if target file does not exist or has different size as remote or has different remoteHash
        if (!fileExists || (fileSize != remoteSize || remoteHash != localHash) && !path.isUser && !path.isLangUser) {
            when {
                !fileExists -> task.print(MessageFormat.format(messages["new_file"], file))
                fileSize != remoteSize -> task.print(
                    MessageFormat.format(
                        messages["size_mismatch"],
                        file
                    )
                )
                remoteHash != localHash -> task.print(
                    MessageFormat.format(
                        messages["checksum_mismatch"],
                        file
                    )
                )
            }

            // download into temporary file
            val tempFile = File(file.parent, "${file.name}.tmp")
            val callback = CallbackAdapter(task)

            try {
                if (canCompress(file.extension))
                    Firmware.download(callback, "$root$filePath.lzma") { inputStream, _ ->
                        uncompress(
                            inputStream,
                            tempFile.outputStream()
                        )
                    }
                else
                    Firmware.download(callback, "$root$filePath", tempFile)

                // replace target file only after successfully download
                Files.move(
                    tempFile.toPath(),
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
                )

                md5[filePath] = remoteHash
                task.print("ok\n")
            } catch (e: Exception) {
                task.print(MessageFormat.format(messages["failed"], e))
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

        task.print(MessageFormat.format(messages["found_firmware"], fileName))

        //Update directory will be created by radio, if update firmware runs against empty SD card create it
        val updatePath = File("$rootDir/$updatePath")
        if (!updatePath.exists()) {
            updatePath.mkdir()
            task.print(MessageFormat.format(messages["update_dir_created"]))
        }

        if (!file.exists() || fileSize != firmwareSize) {
            // download into temporary file
            val tempFile = File(file.parent, "${file.name}.tmp")

            task.print(MessageFormat.format(messages["downloading_from_server"], file))
            val hash = Firmware.download(CallbackAdapter(task), "$remotePath/$fileName", tempFile)

            // replace target file only after successfull download
            Files.move(
                tempFile.toPath(),
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            )

            md5[filePath] = Hash(firmwareSize, hash)
            task.print("ok\n")
        } else {
            task.print(MessageFormat.format(messages["uptodate"], file))
        }

        md5.save()
        task.print(messages["done"])
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
