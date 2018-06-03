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
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


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

    fun scan(task: FXTask<*>) {
        md5.clear()
        md5.scan(task)
        md5.save()
    }

    fun update(task: FXTask<*>, updateResources: Boolean = true, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true, updateFirmware: Boolean = true) {
        if (task.isCancelled) return

        task.print("Checking for latest online versions ...\n")

        md5.load()
        TransmitterType.forProductCode(productCode).getFirmware().forEach { firmware ->
            if (firmware.name.endsWith(".bin") && updateFirmware) {
                updateFirmware(task, firmware)
            } else if (firmware.name.endsWith(".zip") && updateResources) {
                updateResources(task, firmware, replaceHelpPages, replaceVoiceFiles)
            }
        }
        md5.save()

        task.print("\nall done")
    }

    private fun updateResources(task: FXTask<*>, firmware: Firmware<TransmitterType>, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        task.print("\nUpdating resources ...\n")

        val remoteRoot = "$remotePath/${firmware.name.substringBeforeLast(".zip")}.lzma"

        try {
            // process remote md5sum.txt
            MD5Sum().apply {
                task.print("\tDownloading md5sum.txt from server ... ")
                Firmware.download("$remoteRoot//md5sum.txt") { load(it) }
                task.print("ok\n")

                entries.stream().forEach { entry ->
                    if (!task.isCancelled) {
                        val path = entry.key
                        val hash = entry.value
                        val targetFile = File(root, path).apply { parentFile.mkdirs() }

                        when {
                            path.startsWith("/Help") -> {
                                if (replaceHelpPages) updateFileOnline(targetFile, task, remoteRoot, path, hash)
                            }
                            path.startsWith("/Voice") -> {
                                if (replaceVoiceFiles) updateFileOnline(targetFile, task, remoteRoot, path, hash)
                            }
                            else -> updateFileOnline(targetFile, task, remoteRoot, path, hash)
                        }
                    }
                }
            }

            task.print("done\n")
        } catch (e: Exception) {
            // fall back to resource file download
            task.print("failed: $e\n")
            updateResourcesDownload(task, firmware, replaceHelpPages, replaceVoiceFiles)
        }
    }

    private fun updateFileOnline(targetFile: File, task: FXTask<*>, root: String, path: String, hash: Hash) {
        if (task.isCancelled) return

        if (!targetFile.exists() || targetFile.length() != hash.size || hash != md5[path]) {
            task.print("\tDownloading $path from server ... ")

            try {
                if (canCompress(targetFile.extension))
                    Firmware.download("$root$path.lzma") { inputStream ->
                        uncompress(inputStream, targetFile.outputStream())
                    }
                else
                    Firmware.download(task, "$root$path", file = targetFile)

                md5[path] = hash
                task.print("ok\n")
            } catch (e: Exception) {
                task.print("failed: $e\n")
            }
        }
    }

    private fun updateResourcesDownload(task: FXTask<*>, firmware: Firmware<TransmitterType>, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
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
        zipFile.stream().filter { !it.isDirectory }.forEach { entry ->
            if (!task.isCancelled) {
                val targetFile = File(root, entry.name)
                val path = "/${entry.name}"

                when {
                    path.startsWith("/Help") -> {
                        if (replaceHelpPages) updateFileFromZip(targetFile, entry, task, path, zipFile)
                    }
                    path.startsWith("/Voice") -> {
                        if (replaceVoiceFiles) updateFileFromZip(targetFile, entry, task, path, zipFile)
                    }
                    else -> updateFileFromZip(targetFile, entry, task, path, zipFile)
                }
            }
        }

        task.print("done\n")
    }

    private fun updateFileFromZip(targetFile: File, entry: ZipEntry, task: FXTask<*>, path: String, zipFile: ZipFile) {
        if (task.isCancelled) return

        val expectedSize = entry.size
        if (!targetFile.exists() || targetFile.length() != expectedSize || Hash(expectedSize, zipFile.hash(entry)) != md5[path]) {
            task.print("\tinstalling $targetFile ... ")
            md5[path] = Hash(expectedSize, zipFile.extract(task, entry, targetFile))
            task.print("ok\n")
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
        val path = "$updatePath/${firmwareFile.name}"
        val expectedHash = Hash(firmware.size, firmwareFile.hash())

        if (!targetFile.exists() || targetFile.length() != firmwareFile.length() || expectedHash != md5[path]) {
            task.print("\tinstalling $targetFile ... ")
            firmwareFile.copyTo(task, targetFile, overwrite = true)
            md5[path] = expectedHash
            task.print("ok\n")
        }

        task.print("done\n")
    }

    override fun toString(): String {
        return "$root ($productName [$productCode] v$productVersion)"
    }

    companion object {
        private const val GRAUPNER_DISK_CFG = "/GraupnerDisk.cfg"

        fun find(): List<Mz32> = File.listRoots().mapNotNull {
            try {
                Mz32(it)
            } catch (e: Exception) {
                null
            }
        }
    }
}

class Hash(val size: Long, val hash: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hash

        if (size != other.size) return false
        if (hash != other.hash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + hash.hashCode()
        return result
    }

    override fun toString(): String {
        return "Hash(size=$size, hash='$hash')"
    }
}

class MD5Sum(private val root: File?) : TreeMap<String, Hash>() {
    constructor() : this(null)

    private val md5Sum = if (root != null) File(root, MD5_FILE_NAME) else null

    fun scan(task: FXTask<*>) {
        if (task.isCancelled || root == null) return

        task.print("Calculating checksums for all files on $root\n")

        scan(task, root, root)
    }

    private fun scan(task: FXTask<*>, dir: File, root: File) {
        dir.listFiles().forEach { file ->
            if (task.isCancelled) return

            if (file.isDirectory) {
                scan(task, file, root)
            } else {
                val path = "/" + file.relativeTo(root).path.replace(File.separatorChar, '/')
                val hash = file.hash()
                this[path] = Hash(file.length(), hash)
                task.print("hash=\t$hash\tsize=${file.length()}\tpath=$path\n")
            }
        }
    }

    fun load() {
        if (md5Sum != null) load(md5Sum)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun load(file: File) {
        clear()
        if (file.exists() && file.canRead()) load(file.inputStream())
    }

    fun load(inputStream: InputStream) {
        inputStream.reader().use {
            it.forEachLine { line ->
                try {
                    val (hash, size, path) = line.split('|')
                    this[path] = Hash(size.toLong(), hash)
                } catch (e: Exception) {
                    // ignore malformed lines
                }
            }
        }
    }

    fun save() {
        if (md5Sum != null) save(md5Sum)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun save(file: File) = save(file.outputStream())

    @Suppress("MemberVisibilityCanBePrivate")
    fun save(outputStream: OutputStream) {
        outputStream.writer().use {
            forEach { path, hash -> it.write("${hash.hash}|${hash.size}|$path\n") }
        }
    }

    companion object {
        private const val MD5_FILE_NAME = "/md5sum.txt"
    }
}

class CfgFile : HashMap<String, String>() {
    companion object {
        private const val delimiter = " : "

        fun load(file: File) = CfgFile().apply {
            file.forEachLine { line ->
                if (line.contains(delimiter)) {
                    val name = line.substringBefore(delimiter).trim()
                    val value = line.substringAfter(delimiter).trim()
                    this[name] = value
                }
            }
        }
    }
}

fun FXTask<*>.print(newMessage: String) {
    runLater {
        updateMessage(message + newMessage)
    }
}
