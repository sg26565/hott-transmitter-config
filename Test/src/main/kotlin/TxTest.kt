import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.model.enums.LCDType
import de.treichels.hott.serial.SerialPort

fun main() {
    val port = HoTTTransmitter(SerialPort.getPort("COM5"))

    val info = port.txInfo
    println(info)

    when (info.lcdType) {
        LCDType.LCD_G128X64 -> port.writeScreen("""---------------------
* Wireless Download *
* Process Start     *
* Please Wait....   *
---------------------""")

        LCDType.LCD_G256X48 -> port.writeScreen("""----------------------------------------
* Wireless Download Process Start      *
* Please Wait....                      *
----------------------------------------""")
    }
}
