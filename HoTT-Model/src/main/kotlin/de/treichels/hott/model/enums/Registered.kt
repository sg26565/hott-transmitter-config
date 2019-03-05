package de.treichels.hott.model.enums

interface Registered<T> where T : Registered<T>, T : Enum<T> {
    val productCode: Int
    val orderNo: String
    val category: String
    val name: String
    val baudRate: Int
}

