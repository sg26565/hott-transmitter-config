package de.treichels.hott.tts

import de.treichels.hott.tts.voicerss.VoiceRSS
import de.treichels.hott.tts.voicerss.VoiceRssLanguage
import javafx.concurrent.Service
import javafx.concurrent.Task
import javax.sound.sampled.AudioInputStream

class Text2SpeechService : Service<AudioInputStream>() {
    private val voiceRSS = VoiceRSS()

    override fun createTask(): Task<AudioInputStream> = object : Task<AudioInputStream>() {
        override fun call(): AudioInputStream = voiceRSS.stream
    }

    var language: VoiceRssLanguage
        get() = voiceRSS.language
        set(language) {
            voiceRSS.language = language
        }

    var text: String
        get() = voiceRSS.text
        set(text) {
            voiceRSS.text = text
        }

    var volume = 1.0

    var speed: Int
        get() = voiceRSS.speed
        set(speed) {
            voiceRSS.speed = speed
        }
}