package de.treichels.hott.mz32

import de.treichels.hott.util.hash
import tornadofx.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class MD5Sum(private val root: File?) : TreeMap<String, Hash>() {
    constructor() : this(null)

    private val md5Sum = if (root != null) File(root, MD5_FILE_NAME) else null

    /**
     * Scan all files under root an re-calculate hashes.
     */
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

    /**
     * Load hashes from default file under root.
     */
    fun load() {
        if (md5Sum != null) load(md5Sum)
    }

    /**
     * Load hashes from specified file.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun load(file: File) {
        clear()
        if (file.exists() && file.canRead()){
            load(file.inputStream())
        }
    }

    /**
     * Load hashes from input stream.
     */
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

    /**
     * Save hashes to default file under root.
     */
    fun save() {
        if (md5Sum != null) save(md5Sum)
    }

    /**
     * Save hashes to specified file.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun save(file: File) = save(file.outputStream())

    /**
     * Save hashes to output stream.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun save(outputStream: OutputStream) {
        outputStream.writer().use {
            forEach { path, hash -> it.write("${hash.hash}|${hash.size}|$path\n") }
        }
    }

    companion object {
        /** Default file path */
        private const val MD5_FILE_NAME = "/md5sum.txt"
    }
}
