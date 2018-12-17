package de.treichels.hott.model.enums

// the last enum constant
inline fun <reified T : Enum<T>> last() = enumValues<T>().last()

// get the ordinal of the last enum constant
inline fun <reified T : Enum<T>> lastIndex() = enumValues<T>().size - 1

// the code of the last enum constant is -1
inline val <reified T : Enum<T>> T.code
    get() = if (ordinal == lastIndex<T>()) -1 else ordinal

// find enum constant by ordinal - if not found use last constant
inline fun <reified T : Enum<T>> fromCode(code: Int) = enumValues<T>().find { it.ordinal == code } ?: last()

