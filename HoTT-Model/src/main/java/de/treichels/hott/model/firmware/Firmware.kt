package de.treichels.hott.model.firmware

import de.treichels.hott.util.HASH_ALGORITHM
import de.treichels.hott.util.getHash
import de.treichels.hott.util.hash
import org.apache.http.client.fluent.Request
import org.apache.http.message.BasicNameValuePair
import tornadofx.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest

/**
 * Uility class to mange downloads from Graupner's official FTP server.
 */
class Firmware<T>(val device: T, val path: String, val name: String, val size: Long) where T : Updatable<T>, T : Enum<T> {
    companion object {
        private const val FTP_SERVER_ADDRESS = "data.graupnersj.com"
        private const val FILE_LIST = "file_list.php"
        private const val FILE_DOWN = "file_down.php"

        /** list available files for device */
        internal fun <T> list(device: T, category: String): List<Firmware<T>> where T : Updatable<T>, T : Enum<T> = list(device, category, device.productCode.toString())

        /** list available files for device using a nonstandard product code*/
        internal fun <T> list(device: T, category: String, productCode: String): List<Firmware<T>> where T : Updatable<T>, T : Enum<T> {
            val path = "/Firmware/$category/$productCode/"

            return list(path).map { (name, size) -> Firmware(device, path, name, size) }
        }

        /** get a list of files from the FTP server with sizes. Server returns a list with <name>|<size> rows*/
        fun list(path: String): List<Pair<String, Long>> = Request.Post("http://$FTP_SERVER_ADDRESS/$FILE_LIST").bodyForm(BasicNameValuePair("path", path)).execute().handleResponse { response ->
            response.statusLine.apply { if (statusCode != 200) throw IOException(toString()) }

            response.entity.content.reader().readLines().map { line ->
                val parts = line.split("|")
                val name = parts[0]
                val size = parts[1].toLong()

                Pair(name, size)
            }
        }

        /** download a file from the FTP server */
        fun <T> download(path: String, func: (InputStream, size: Long) -> T): T = Request.Post("http://$FTP_SERVER_ADDRESS/$FILE_DOWN").bodyForm(BasicNameValuePair("file", path)).execute().handleResponse { response ->
            response.statusLine.apply { if (statusCode != 200) throw IOException(toString()) }
            response.entity.content.use {
                func(it, response.entity.contentLength)
            }
        }

        fun download(task: FXTask<*>, path: String, file: File): String {
            val md = MessageDigest.getInstance(HASH_ALGORITHM)
            val buffer = ByteArray(1024 * 1024)
            var bytesRead = 0L

            // download from FTP server
            download(path) { inputStream, size ->
                file.outputStream().use { outputStream ->
                    while (!task.isCancelled) {
                        val len = inputStream.read(buffer)

                        if (len < 0) break

                        bytesRead += len
                        outputStream.write(buffer, 0, len)
                        md.update(buffer, 0, len)
                        task.updateProgress(bytesRead, size)
                    }
                }

                if (size > 0L && bytesRead != size)
                    throw IOException("Size mismatch: expected $size, but got $bytesRead.")
            }

            if (task.isCancelled)
                throw InterruptedException()

            return md.getHash()
        }
    }

    override fun toString(): String {
        return "$device -> $path$name:$size"
    }

    /** root directory of local cache */
    private val cacheDir = File("${System.getProperty("user.home")}/.java/cache/HoTT/firmware/${device.name}")

    /** local cached file */
    val file = File(cacheDir, name)

    val isCached
        get() = file.exists() && file.length() == size

    /** checksum of file */
    @Suppress("unused")
    val hash: String by lazy {
        download().get().hash()
    }

    /** download file if not already cached and report progress to FXTask object */
    fun download(task: FXTask<*>): File {
        if (!isCached && !task.isCancelled) {
            cacheDir.mkdirs()
            download(task, "$path$name", file)
        }

        return file
    }


    /** download file asynchronously in background */
    fun download() = runAsync { download(this) }
}