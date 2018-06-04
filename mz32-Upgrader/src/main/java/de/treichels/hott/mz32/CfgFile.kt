package de.treichels.hott.mz32

import java.io.File
import java.util.*

class CfgFile : HashMap<String, String>() {
    companion object {
        private const val delimiter = " : "

        fun load(file: File) = CfgFile().apply {
            if (file.exists() && file.isFile && file.canRead()) {
                println(file)
                file.forEachLine { line ->
                    if (line.contains(delimiter)) {
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
