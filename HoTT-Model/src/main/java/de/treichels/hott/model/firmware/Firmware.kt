package de.treichels.hott.model.firmware

import org.apache.http.client.fluent.Request
import org.apache.http.message.BasicNameValuePair
import java.io.File

class Firmware<T>(val device: T, val path: String, val name: String, val size: Long) where T : Updatable<T>, T : Enum<T> {
    companion object {
        private const val FTP_SERVER_ADDRESS = "data.graupnersj.com"
        //private const val FTP_SERVER_ADDRESS = "210.122.9.64"
        //private const val FTP_SERVER_ADDRESS = "211.62.35.33"
        private const val FILE_LIST = "file_list.php"
        private const val FILE_DOWN = "file_down.php"

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

    private val parentDir = File("${System.getProperty("user.home")}/.java/cache/HoTT/firmware/${device.name}")
    val file = File(parentDir, name)

    val isCached
        get() = file.exists() && file.length() == size

    fun download() {
        if (!isCached) {
            parentDir.mkdirs()
            val postParam = BasicNameValuePair("file", "$path$name")
            Request.Post("http://$FTP_SERVER_ADDRESS/$FILE_DOWN").bodyForm(postParam).execute().saveContent(file)
        }
    }
}