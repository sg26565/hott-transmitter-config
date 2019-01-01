package de.treichels.hott.firmware

import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.model.enums.Registered
import de.treichels.hott.util.HASH_ALGORITHM
import de.treichels.hott.util.getHash
import org.apache.http.client.fluent.Request
import org.apache.http.message.BasicNameValuePair
import tornadofx.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.util.logging.Logger

/**
 * Uility class to mange downloads from Graupner's official FTP server.
 */
class Firmware<T>(val device: T, val path: String, val name: String, val size: Long) where T : Registered<*> {
    companion object {
        private val LOG = Logger.getLogger(Firmware::class.qualifiedName)
        private const val FTP_SERVER_ADDRESS = "data.graupnersj.com"
        private const val FILE_LIST = "file_list.php"
        private const val FILE_DOWN = "file_down.php"

        /** list available files for device */
        fun <T> list(device: T): List<Firmware<T>> where T : Registered<*> = list(device, device.category, device.productCode)

        /** list available files for device */
        @Suppress("unused")
        fun <T> list(device: T, category: String): List<Firmware<T>> where T : Registered<*> = list(device, category, device.productCode)

        /** list available files for device */
        fun <T> list(device: T, productCode: Int): List<Firmware<T>> where T : Registered<*> = list(device, device.category, productCode)

        /** list available files for device */
        fun <T> list(device: T, productCode: Int, mask: Int): List<Firmware<T>> where T : Registered<*> {
            val part = productCode % mask
            val min = mask / 10
            return if (part >= min)
                list(device, device.category, part)
            else
                emptyList()
        }

        /** list available files for device using a nonstandard product code*/
        fun <T> list(device: T, category: String, productCode: Any): List<Firmware<T>> where T : Registered<*> {
            val path = "/Firmware/$category/$productCode/"

            return list(path).map { (name, size) -> Firmware(device, path, name, size) }
        }

        /** get a list of files from the FTP server with sizes. Server returns a list with <name>|<size> rows*/
        private fun list(path: String): List<Pair<String, Long>> = Request.Post("http://$FTP_SERVER_ADDRESS/$FILE_LIST").bodyForm(BasicNameValuePair("path", path)).execute().handleResponse { response ->
            response.statusLine.apply {
                LOG.finer("$path -> $this")

                if (statusCode != 200) throw IOException(toString())
            }

            response.entity.content.reader().readLines().map { line ->
                LOG.finer(line)
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

        fun download(task: FXTask<*>?, path: String, file: File): String {
            val md = MessageDigest.getInstance(HASH_ALGORITHM)
            val buffer = ByteArray(1024 * 1024)
            var bytesRead = 0L

            // download from FTP server
            download(path) { inputStream, size ->
                file.outputStream().use { outputStream ->
                    while (task?.isCancelled != false) {
                        val len = inputStream.read(buffer)

                        if (len < 0) break

                        bytesRead += len
                        outputStream.write(buffer, 0, len)
                        md.update(buffer, 0, len)
                        task?.updateProgress(bytesRead, size)
                    }
                }

                if (size > 0L && bytesRead != size)
                    throw IOException("Size mismatch: expected $size, but got $bytesRead.")
            }

            if (task?.isCancelled == true)
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

    /** download file if not already cached and report progress to FXTask object */
    @Suppress("MemberVisibilityCanBePrivate")
    fun download(task: FXTask<*>?): File {
        if (!isCached && task?.isCancelled != true) {
            cacheDir.mkdirs()
            download(task, "$path$name", file)
        }

        return file
    }

    fun download() = download(null)
}

fun ReceiverType.getFirmware(): List<Firmware<ReceiverType>> {
    var result = emptyList<Firmware<ReceiverType>>()

    result += Firmware.list(this)
    result += Firmware.list(this, productCode, 100000)
    result += Firmware.list(this, productCode, 10000)
    if (id > 0) result += Firmware.list(this, "Server Updates", id.toString(16).toUpperCase())

    return result
}

fun <T> T.getFirmware(): List<Firmware<T>> where T : Registered<*> {
    var result = emptyList<Firmware<T>>()

    result += Firmware.list(this)
    result += Firmware.list(this, productCode, 100000)
    result += Firmware.list(this, productCode, 10000)

    return result
}
