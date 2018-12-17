package de.treichels.hott.model.enums

interface Registered<T> where T : Registered<T>, T : Enum<T> {
    val productCode: Int
}

