import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.serial.JSSCSerialPort

fun main(args: Array<String>) {
    val availablePorts = JSSCSerialPort.availablePorts

    for (i in 1..10) {
        val port = JSSCSerialPort(availablePorts.first())
        val hott = HoTTSerialPort(port)

        hott.use { p ->
            println("$i ${p.isOpen} ${hott.isOpen} ${port.isOpen}")
            p.open()
            p.allModelInfos.filter { it.modelType != ModelType.Unknown && it.modelName.isNotEmpty() }.map { println("${it.modelNumber}: ${it.modelType.char}${it.modelName}.mdl") }
            println("$i ${p.isOpen} ${hott.isOpen} ${port.isOpen}")
        }

        println("$i ${hott.isOpen} ${port.isOpen}")
    }
}