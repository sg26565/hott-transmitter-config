package de.treichels.hott.model.voice

import javax.sound.sampled.AudioInputStream

interface Text2Speech {
    fun convert(text: String, options: Map<String, String>): AudioInputStream
}
