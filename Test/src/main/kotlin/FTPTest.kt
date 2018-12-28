
import de.treichels.hott.firmware.getFirmware
import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.model.enums.TransmitterType

fun main(vararg args: String) {
    ReceiverType.values().forEach { receiver ->
        receiver.getFirmware().forEach { firmware ->
            val cached = firmware.isCached

            println("${firmware.device} -> ${firmware.path}${firmware.name} ${firmware.size} bytes, ${if (cached) "(cached)" else "(online)"}")

            if (!cached) firmware.download()
        }
    }

    TransmitterType.values().forEach { transmitter ->
        transmitter.getFirmware().forEach { firmware ->
            val cached = firmware.isCached

            println("${firmware.device} -> ${firmware.path}${firmware.name} ${firmware.size} bytes, ${if (cached) "(cached)" else "(online)"}")

            if (!cached) firmware.download()
        }
    }
}
