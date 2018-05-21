package de.treichels.hott.model.firmware

import de.treichels.hott.util.hash
import org.apache.http.client.fluent.Request
import org.apache.http.message.BasicNameValuePair
import tornadofx.*
import java.io.File
import java.io.IOException

/**
 * Uility class to mange downloads from Graupner's official FTP server.
 */
class Firmware<T>(val device: T, val path: String, val name: String, val size: Long) where T : Updatable<T>, T : Enum<T> {
    companion object {
        private const val FTP_SERVER_ADDRESS = "data.graupnersj.com"
        private const val FILE_LIST = "file_list.php"
        private const val FILE_DOWN = "file_down.php"

        /** list available files for device */
        internal fun <T> listFiles(device: T, category: String): List<Firmware<T>> where T : Updatable<T>, T : Enum<T> {
            return listFiles(device, category, device.productCode.toString())
        }

        /** list available files for device using a nonstandard product code*/
        internal fun <T> listFiles(device: T, category: String, productCode: String): List<Firmware<T>> where T : Updatable<T>, T : Enum<T> {
            val path = "/Firmware/$category/$productCode/"
            val postParam = BasicNameValuePair("path", path)

            return Request.Post("http://$FTP_SERVER_ADDRESS/$FILE_LIST").bodyForm(postParam).execute()
                    .returnContent().asStream().reader().readLines().map { line ->
                        val parts = line.split("|")
                        val name = parts[0]
                        val size = parts[1].toLong()

                        Firmware(device, path, name, size)
                    }
        }
    }

    override fun toString(): String {
        return "$device -> $path$name:$size"
    }

    /** root directory of local cache */
    private val parentDir = File("${System.getProperty("user.home")}/.java/cache/HoTT/firmware/${device.name}")

    /** local cached file */
    val file = File(parentDir, name)

    val isCached
        get() = file.exists() && file.length() == size

    /** checksum of file */
    val hash: String by lazy {
        download().get().hash()
    }

    /** download file if not already cached and report progress to FXTask object */
    fun download(task: FXTask<*>? = null): File {
        if (!isCached) {
            val start = System.currentTimeMillis()
            val totalKb = size / 1024

            parentDir.mkdirs()

            // download from FTP server
            val request = Request.Post("http://$FTP_SERVER_ADDRESS/$FILE_DOWN").bodyForm(BasicNameValuePair("file", "$path$name"))
            request.execute().handleResponse { response ->
                val status = response.statusLine

                if (status.statusCode != 200)
                    throw IOException(status.toString())

                val buffer = ByteArray(1024 * 1024)
                var bytesRead = 0L

                // copy response data to file
                response.entity.content.use { inputStream ->
                    file.outputStream().use { outputStream ->
                        while (true) {
                            val len = inputStream.read(buffer)

                            if (len < 0) break

                            bytesRead += len
                            outputStream.write(buffer, 0, len)

                            if (task != null) {
                                val elapsed = (System.currentTimeMillis() - start) / 1000
                                val elapsedStr = if (elapsed < 60) "$elapsed s" else "${elapsed / 60} min"
                                val doneKb = bytesRead / 1024
                                val progress = doneKb * 100 / totalKb
                                val rate = doneKb / elapsed
                                val remaining = (totalKb - doneKb) / rate
                                val remainStr = if (remaining < 60) "$remaining s" else "${remaining / 60} min"
                                task.updateProgress(bytesRead, size)
                                task.updateMessage("\tdownloading $progress% - $doneKb/$totalKb kb @ $rate kb/s - elapsed $elapsedStr - remaining $remainStr")
                            }
                        }
                    }
                }

                if (bytesRead != size)
                    throw IOException("Size mismatch: expected $size, but got $bytesRead.")
            }
        }

        return file
    }

    /** download file asynchronously in background */
    fun download() = runAsync { download(this) }
}
