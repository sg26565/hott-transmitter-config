package de.treichels.hott.mz32

import java.io.File
import java.io.InputStream
import java.util.*

/**
 * Parse a config file into a hash map. Each line consists of a key-value pair separated by a colon. Whitespace is
 * ignored as well as empty lines and lines starting with #
 */
class CfgFile : HashMap<String, String>() {
    companion object {
        private const val delimiter = " : "

        fun load(file: File): CfgFile = load(file.inputStream())

        fun load(inputStream: InputStream): CfgFile = CfgFile().apply {
            inputStream.use {
                it.reader().forEachLine { s ->
                    val line = s.trim()

                    if (!line.isBlank() && !line.startsWith("#") && line.contains(delimiter)) {
                        val name = line.substringBefore(delimiter).trim()
                        val value = line.substringAfter(delimiter).trim()
                        this[name] = value
                        println("\t$name = $value")
                    }
                }
            }
        }
    }
}
