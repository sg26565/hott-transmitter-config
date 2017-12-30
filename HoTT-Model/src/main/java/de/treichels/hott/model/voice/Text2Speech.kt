package de.treichels.hott.model.voice

import java.io.File

interface Text2Speech {
    @Throws(Exception::class)
    fun convert(text: String, language: String): File
}
