package de.treichels.hott.mz32

import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.util.HASH_SIZE
import de.treichels.hott.util.extract
import de.treichels.hott.util.hash
import tornadofx.*
import java.io.File
import java.util.*
import java.util.zip.ZipFile

class Mz32(val root: File) {
    private val cfgFile = CfgFile.load(File(root, GRAUPNER_DISK_CFG))
    val productName = cfgFile["Product name"]!!
    val productCode = cfgFile["Product code"]!!.toInt()
    val productVersion = cfgFile["Product ver."]!!.toFloat() / 1000
    val rfidNumber = cfgFile["RFID number"]!!
    val updatePath = cfgFile["Update path"]!!
    val userPath = cfgFile["User path"]!!.split(';')
    val langPath = cfgFile["Lang path"]!!.split(';')
    val langUser = cfgFile["Lang User"]!!.split(';')
    val md5 = MD5Sum().apply { load() }

    fun update(task: FXTask<*>, updateResources: Boolean = true, updateFirmware: Boolean = true) {
        if (task.isCancelled) return

        task.updateMessage("Checking for latest online version")

        TransmitterType.forProductCode(productCode).getFirmware().forEach { firmware ->
            if (firmware.name.endsWith(".bin") && updateFirmware) {
                firmware.download()
                updateFirmware(task, firmware.file)
            } else if (firmware.name.endsWith(".zip") && updateResources) {
                firmware.download()
                updateResources(task, firmware.file)
            }
        }

        task.updateMessage("done")
    }

    fun updateResources(task: FXTask<*>, resourceFile: File) {
        if (task.isCancelled) return

        task.updateMessage("Updating resources from file ${resourceFile.name}")

        val zipFile = ZipFile(resourceFile)

        zipFile.stream().filter { !it.isDirectory }.forEach { entry ->
            if (!task.isCancelled) {
                val targetFile = File(root, entry.name)
                val path = "/${entry.name}"

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
        }

        md5.save()
    }

    fun updateFirmware(task: FXTask<*>, firmwareFile: File) {
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

    inner class MD5Sum : TreeMap<String, String>() {
        private val file = File(root, MD5_FILE_NAME)

        fun scan(task: FXTask<*>) {
            if (task.isCancelled) return

            task.updateMessage("Calculating MD5 checksums for all files on radio")

            clear()
            scan(task, root)
            save()
        }

        private fun scan(task: FXTask<*>, dir: File) {
            dir.listFiles().forEach { file ->
                if (task.isCancelled) return

                if (file.isDirectory) {
                    scan(task, file)
                } else {
                    val path = "/" + file.relativeTo(root).path.replace(File.separatorChar, '/')
                    val hash = file.hash()
                    this[path] = hash
                    task.updateMessage("\t$hash  $path")
                }
            }
        }

        fun load() {
            clear()

            if (file.exists() && file.canRead()) {
                file.reader().use {
                    it.forEachLine { line ->
                        val hash = line.substring(0, HASH_SIZE)
                        val path = line.substring(HASH_SIZE + 2)
                        this[path] = hash
                    }
                }
            }
        }

        fun save() {
            file.writer().use {
                forEach { path, hash -> it.write("$hash  $path\n") }
            }
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

    companion object {
        private const val MD5_FILE_NAME = "/System/md5sums.txt"
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

