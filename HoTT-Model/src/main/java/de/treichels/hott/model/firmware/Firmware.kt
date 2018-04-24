package de.treichels.hott.model.firmware

import org.apache.http.client.fluent.Request
import org.apache.http.message.BasicNameValuePair
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Uility class to mange downloads from Graupner's official FTP server.
 */
class Firmware<T>(val device: T, val path: String, val name: String, val size: Long) where T : Updatable<T>, T : Enum<T> {
    companion object {
        private const val HASH_ALGORITHM = "SHA-256"
        private const val BUFFER_SIZE = 65536
        private const val FTP_SERVER_ADDRESS = "data.graupnersj.com"
        private const val FILE_LIST = "file_list.php"
        private const val FILE_DOWN = "file_down.php"

        /** list available files for device */
        internal fun <T> listFiles(device: T, category: String): List<Firmware<T>> where T : Updatable<T>, T : Enum<T> {
            return listFiles(device,category,device.productCode.toString())
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
        download()

        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val buffer = ByteArray(BUFFER_SIZE)

        file.inputStream().use { stream ->
            while (true) {
                val len = stream.read(buffer)
                if (len >= 0) digest.update(buffer, 0, len) else break
            }
        }

        BigInteger(1, digest.digest()).toString(16)
    }

    /** download file is not already cached */
    fun download() {
        if (!isCached) {
            parentDir.mkdirs()
            val postParam = BasicNameValuePair("file", "$path$name")
            Request.Post("http://$FTP_SERVER_ADDRESS/$FILE_DOWN").bodyForm(postParam).execute().saveContent(file)
        }
    }
}