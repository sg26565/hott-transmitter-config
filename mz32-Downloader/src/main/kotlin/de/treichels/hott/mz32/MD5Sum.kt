package de.treichels.hott.mz32

import de.treichels.hott.util.hash
import tornadofx.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * A map that stores a hash value for each file path.
 */
class MD5Sum(private val root: File?) : TreeMap<String, Hash>() {
    constructor() : this(null)

    /** the default file were data will be loaded from or saved to */
    private val md5Sum by lazy { File(root, MD5_FILE_NAME) }

    /**
     * Scan all files under root an re-calculate hashes.
     */
    fun scan(task: FXTask<*>, languages: List<Language>) {
        if (task.isCancelled || root == null) return

        task.print("Calculating checksums for all files on $root\n")

        val files = root.walk().iterator().asSequence().filter { it.isFile }.toList().sorted()
        val total = files.size.toLong()

        files.forEachIndexed { index, file ->
            if (task.isCancelled) return

            val path = "/${file.relativeTo(root).path.replace(File.separatorChar, '/')}"

            Path(path).apply {
                if (!isProtected && (!isLang || languages.contains(language))) {
                    val hash = file.hash()
                    val size = file.length()
                    this@MD5Sum[path] = Hash(size, hash)
                    task.print("\thash=$hash\tsize=$size\tpath=$path\n")
                } else {
                    task.print("\tskipping $path\n")
                }
            }

            task.updateProgress(index.toLong(), total)
        }
    }

    /**
     * Load hashes from default file under root.
     */
    fun load() {
        if (root != null) load(md5Sum)
    }

    /**
     * Load hashes from specified file.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun load(file: File) {
        clear()
        if (file.exists() && file.canRead()) {
            load(file.inputStream())
        }
    }

    /**
     * Load hashes from input stream.
     */
    fun load(inputStream: InputStream) {
        inputStream.reader().use { reader ->
            reader.forEachLine { line ->
                try {
                    if (!line.isBlank() && !line.startsWith("#") && line.count { it == '|' } == 2) {
                        val (hash, size, path) = line.split('|')
                        this[path.trim()] = Hash(size.toLong(), hash.trim())
                    }
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
        if (root != null) save(md5Sum)
    }

    /**
     * Save hashes to specified file.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun save(file: File): Unit = save(file.outputStream())

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
