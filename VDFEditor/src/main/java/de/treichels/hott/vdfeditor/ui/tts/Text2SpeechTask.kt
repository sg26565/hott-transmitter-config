package de.treichels.hott.vdfeditor.ui.tts

import gde.model.voice.VoiceRSS
import gde.model.voice.VoiceRssLanguage
import javafx.concurrent.Task
import java.io.File

internal class Text2SpeechTask : Task<File>() {
    private val voiceRSS = VoiceRSS()

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

    override fun call(): File = voiceRSS.file
}
