import de.treichels.hott.firmware.Firmware
import de.treichels.hott.firmware.getFirmware
import de.treichels.hott.model.enums.*

fun Firmware<*>.print() {
    val cached = isCached

    println("$device -> $path$name $size bytes, ${if (cached) "(cached)" else "(online)"}")

    if (!cached) download()
}

fun main(vararg args: String) {
    ReceiverType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
    TransmitterType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
    ModuleType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
    SensorType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
    ESCType.values().forEach { it.getFirmware().forEach(Firmware<*>::print) }
}
