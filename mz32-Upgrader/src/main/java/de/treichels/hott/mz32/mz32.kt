package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.util.HASH_SIZE
import de.treichels.hott.util.extract
import de.treichels.hott.util.hash
import tornadofx.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class Mz32(val root: File) {
    private val cfgFile = CfgFile.load(File(root, GRAUPNER_DISK_CFG))
    private val md5 = MD5Sum(root).apply { load() }

    val productName = cfgFile["Product name"]!!
    val productCode = cfgFile["Product code"]!!.toInt()
    val productVersion = cfgFile["Product ver."]!!.toFloat() / 1000
    val rfidNumber = cfgFile["RFID number"]!!
    val updatePath = cfgFile["Update path"]!!
    val userPath = cfgFile["User path"]!!.split(';').filter { !it.isBlank() }
    val langPath = cfgFile["Lang path"]!!.split(';').filter { !it.isBlank() }
    val langUser = cfgFile["Lang User"]!!.split(';').filter { !it.isBlank() }

    fun scan(task: FXTask<*>) {
        md5.scan(task)
        md5.save()
    }

    fun update(task: FXTask<*>, updateResources: Boolean = true, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true, updateFirmware: Boolean = true) {
        if (task.isCancelled) return

        task.updateMessage("Checking for latest online version")

        TransmitterType.forProductCode(productCode).getFirmware().forEach { firmware ->
            if (firmware.name.endsWith(".bin") && updateFirmware) {
                if (!firmware.isCached) {
                    task.updateMessage("Downloading firmware ${firmware.name}")
                    firmware.download(task)
                }
                updateFirmware(task, firmware.file)
            } else if (firmware.name.endsWith(".zip") && updateResources) {
                if (!firmware.isCached) {
                    task.updateMessage("Downloading firmware ${firmware.name}")
                    firmware.download(task)
                }
                updateResources(task, firmware.file, replaceHelpPages, replaceVoiceFiles)
            }
        }


        task.updateMessage("done")
    }

    private fun updateResources(task: FXTask<*>, resourceFile: File, replaceHelpPages: Boolean = true, replaceVoiceFiles: Boolean = true) {
        if (task.isCancelled) return

        task.updateMessage("Updating resources from file ${resourceFile.name}")

        val zipFile = ZipFile(resourceFile)

        zipFile.stream().filter { !it.isDirectory }.forEach { entry ->
            if (!task.isCancelled) {
                val targetFile = File(root, entry.name)
                val path = "/${entry.name}"

                when {
                    path.startsWith("/Help") -> {
                        if (replaceHelpPages) updateFile(targetFile, entry, task, path, zipFile)
                    }
                    path.startsWith("/Voice") -> {
                        if (replaceVoiceFiles) updateFile(targetFile, entry, task, path, zipFile)
                    }
                    else -> updateFile(targetFile, entry, task, path, zipFile)
                }
            }
        }

        md5.save()
    }

    private fun updateFile(targetFile: File, entry: ZipEntry, task: FXTask<*>, path: String, zipFile: ZipFile) {
        if (!targetFile.exists() || targetFile.length() != entry.size) {
            task.updateMessage("\tupdate $targetFile")
            md5[path] = zipFile.extract(entry, targetFile)
        } else {
            val targetHash = md5[path]
            val zipHash = zipFile.hash(entry)

            if (zipHash != targetHash) {
                task.updateMessage("\tupdate $targetFile")
                md5[path] = zipFile.extract(entry, targetFile)
            }
        }
    }

    private fun updateFirmware(task: FXTask<*>, firmwareFile: File) {
        if (task.isCancelled) return

        task.updateMessage("Updating firmware from file ${firmwareFile.name}")

        val targetDir = File(root, updatePath)
        val targetFile = File(targetDir, firmwareFile.name)
        val path = "$updatePath/${firmwareFile.name}"
        val firmwareHash = firmwareFile.hash()

        if (!targetFile.exists() || targetFile.length() != firmwareFile.length()) {
            task.updateMessage("\tupdate $targetFile")
            firmwareFile.copyTo(targetFile, overwrite = true)
            md5[path] = firmwareHash
        } else {
            val targetHash = md5[path]

            if (firmwareHash != targetHash) {
                task.updateMessage("\tupdate $targetFile")
                firmwareFile.copyTo(targetFile, overwrite = true)
                md5[path] = firmwareHash
            }
        }

        md5.save()
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

class MD5Sum(private val root: File) : TreeMap<String, String>() {
    private val md5Sum = File(root, MD5_FILE_NAME)

    fun scan(task: FXTask<*>) {
        if (task.isCancelled) return

        task.updateMessage("Calculating checksums for all files on $root")

        clear()
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
                this[path] = hash
                task.updateMessage("\t$hash  $path")
            }
        }
    }

    fun load() = load(md5Sum)
    fun load(file: File) {
        clear()
        if (file.exists() && file.canRead()) load(file.inputStream())
    }

    fun load(inputStream: InputStream) {
        inputStream.reader().use {
            it.forEachLine { line ->
                val hash = line.substring(0, HASH_SIZE)
                val path = line.substring(HASH_SIZE + 2)
                this[path] = hash
            }
        }
    }

    fun save() = save(md5Sum)
    fun save(file: File) = save(file.outputStream())
    fun save(outputStream: OutputStream) {
        outputStream.writer().use {
            forEach { path, hash -> it.write("$hash  $path\n") }
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
