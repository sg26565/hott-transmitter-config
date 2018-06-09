import com.sun.javafx.application.PlatformImpl
import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.model.firmware.Firmware
import tornadofx.*

fun main(vararg args: String) {
    PlatformImpl.startup { }

    ReceiverType.values().forEach { receiver ->
        receiver.getFirmware().forEach { firmware ->
            val cached = firmware.isCached

            println("${firmware.device} -> ${firmware.path}${firmware.name} ${firmware.size} bytes, ${if (cached) "(cached)" else "(online)"}")

            if (!cached) download(firmware)
        }
    }

    TransmitterType.values().forEach { transmitter ->
        transmitter.getFirmware().forEach { firmware ->
            val cached = firmware.isCached
            println("${firmware.device} -> ${firmware.path}${firmware.name} ${firmware.size} bytes, ${if (cached) "(cached)" else "(online)"}")

            if (!cached) download(firmware)
        }
    }

    PlatformImpl.exit()
}

fun download(firmware: Firmware<*>) {
    runAsync {
        firmware.download(this)
    }.apply {
        messageProperty().addListener { _, _, newValue -> println(newValue) }
    }.get()
}

