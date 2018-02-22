package de.treichels.hott.tts

import java.util.*
import javax.sound.sampled.AudioInputStream

enum class Gender {
    Male, Female, Unknown
}

enum class Age {
    Adult, Child, Unknown
}

open class Voice {
    var enabled: Boolean = false
    lateinit var age: Age
    lateinit var locale: Locale
    lateinit var description: String
    lateinit var gender: Gender
    lateinit var id: String
    lateinit var name: String

    override fun toString() = "$description"
}

open class Quality(val sampleRate: Int, val channels: Int) {
    override fun toString(): String {
        return "${sampleRate / 1000}kHz ${if (channels == 1) "mono" else "stereo"}"
    }
}

abstract class Text2SpeechProvider {
    abstract val enabled: Boolean
    abstract val qualities: List<Quality>
    abstract val name: String
    open val defaultVoice: Voice
        get() = installedVoices().find { it.locale == Locale.getDefault() } ?: installedVoices().first()
    open val defaultQuality: Quality = Quality(24000, 2)
    open val speedSupported = true
    open val volumeSupported = true
    open val ssmlSupported  = true

    abstract fun installedVoices(): List<Voice>
    abstract fun speak(text: String, voice: Voice, speed: Int = 0, volume: Int = 100, sampleSize: Int = 16, channels: Int = 2, sampleRate: Int = 16_000, ssml: Boolean = false): AudioInputStream

    override fun toString() = name
}
