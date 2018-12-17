package de.treichels.hott.tts

import de.treichels.hott.voice.TrimInputStream
import de.treichels.hott.voice.VolumeControlAudioInputStream
import javafx.concurrent.Service
import javafx.concurrent.Task
import javax.sound.sampled.AudioInputStream

class Text2SpeechService : Service<AudioInputStream>() {
    override fun createTask(): Task<AudioInputStream> = object : Task<AudioInputStream>() {
        override fun call(): AudioInputStream {
            val audioInputStream = provider.speak(text, voice, speed, (volume * 100).toInt(), Quality(frameRate, channels, sampleSize), ssml)
            val format = audioInputStream.format
            val trimmedInputStream = TrimInputStream(audioInputStream, sampleSize * channels)
            return VolumeControlAudioInputStream(trimmedInputStream, format, volume)
        }
    }

    lateinit var provider: Text2SpeechProvider
    lateinit var text: String
    lateinit var voice: Voice
    var speed: Int = 0
    var volume: Double = 1.0
    var sampleSize: Int = 16
    var channels: Int = 1
    var frameRate: Int = 11025
    var ssml: Boolean = false
}
