import de.treichels.hott.decoder.Firmware
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.FileOutputStream
import java.io.IOException

fun Firmware<*>.print() {
    val cached = isCached

    println("$device -> $path$name $size bytes, ${if (cached) "(cached)" else "(online)"}")

    if (!cached) download()
}

fun main() {
//    ReceiverType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
//    TransmitterType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
//    ModuleType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
//    SensorType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
//    ESCType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }

    FTPClient().apply {
        try {
            connect("ftp.graupner.de")

            if (FTPReply.isPositiveCompletion(replyCode)) println("Connected to server") else throw IOException("cannot connect to server")

            if (login("ftpuser01", "1fQjHTGc")) println("Login succeeded.") else throw IOException("Login failed")

            val dir = "/Transmitter/16008200"
            listFiles(dir).filter { it.isFile }.forEach {
                val fileSize = it.size
                val localFileName = "C:/Temp/${it.name}"
                val remoteFileName = "$dir/${it.name}"
                val start = System.currentTimeMillis()
                val outputStream = FileOutputStream(localFileName)

                print("Downloading $remoteFileName:$fileSize ... ")
                if (!retrieveFile(remoteFileName, outputStream)) throw IOException("")
                val duration = System.currentTimeMillis() - start
                val rate = fileSize / 1024.0 * 1000.0 / duration
                println("took $duration ms (${rate.toInt()} kb/s)")
            }
        } finally {
            if (isConnected) disconnect()
        }
    }
}
