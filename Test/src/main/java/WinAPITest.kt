
import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.model.serial.WinAPISerialPort
import jtermios.JTermios
import java.util.logging.LogManager

fun main(vararg args: String) {
    LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("logging.properties"))
    JTermios.JTermiosLogging.setLogLevel(0)

    val port = WinAPISerialPort("COM5")
    val hott = HoTTSerialPort(port)
    val txInfo = hott.txInfo
}