import de.treichels.hott.decoder.internal.firmware.*
import de.treichels.hott.decoder.internal.firmware.DeviceFirmware.Companion.COMMAND_START
import de.treichels.hott.decoder.internal.firmware.DeviceFirmware.Companion.COMMAND_START_BOOT5
import de.treichels.hott.decoder.internal.firmware.DeviceFirmware.Companion.COMMAND_START_TELEMETRY
import de.treichels.hott.decoder.internal.firmware.DeviceFirmware.Companion.waitForBoot
import de.treichels.hott.decoder.internal.firmware.StandardDeviceFirmware.Companion.getDeviceId
import de.treichels.hott.decoder.internal.firmware.StandardDeviceFirmware.Companion.getVersionInfo
import de.treichels.hott.decoder.internal.firmware.StandardDeviceFirmware.Companion.getVersionInfoAddr
import de.treichels.hott.decoder.internal.firmware.StandardDeviceFirmware.Companion.updateMode
import de.treichels.hott.serial.SerialPort
import de.treichels.hott.util.SimpleCallback
import javafx.application.Application
import javafx.stage.Stage
import java.io.File
import java.util.concurrent.CountDownLatch

// create a dummy JavaFX application
class JavaFXInitializer : Application(), Runnable {
    companion object {
        private val latch = CountDownLatch(1)

        fun initialize() {
            Thread(JavaFXInitializer()).start()
            latch.await()
        }
    }

    override fun run() {
        Application.launch(JavaFXInitializer::class.java)
    }

    override fun start(primaryStage: Stage?) {
        latch.countDown()
    }
}

fun main() {
    JavaFXInitializer.initialize()

    val port = SerialPort.getPort("COM5")
    val binFile = File("C:\\Users\\olive\\.java\\cache\\HoTT\\firmware\\bl_control_18\\AIR3G_4S_2a010.bin")
    val firmware = DeviceFirmware.load(binFile)

    firmware.updateDevice(SimpleCallback(), port)
}