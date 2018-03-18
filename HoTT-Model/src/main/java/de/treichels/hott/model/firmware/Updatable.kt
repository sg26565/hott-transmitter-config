package de.treichels.hott.model.firmware

interface Updatable<T> where T : Updatable<T>, T : Enum<T> {
    val productCode: Int
    fun getFirmware(): List<Firmware<T>>
    fun downloadFirmware() = getFirmware().forEach { it.download() }
}
