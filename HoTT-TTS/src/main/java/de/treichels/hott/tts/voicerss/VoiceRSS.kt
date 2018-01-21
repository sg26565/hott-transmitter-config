package de.treichels.hott.tts.voicerss

import de.treichels.hott.tts.Text2Speech
import java.net.URL
import java.net.URLEncoder
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class VoiceRSS(private val apiKey: String = VOICE_RSS_DEFAULT_API_KEY, var text: String = "", var language: VoiceRssLanguage = VoiceRssLanguage.de_de, var speed: Int = 0, var codec: Codec = Codec.WAV, var format: Format = Format.pcm_22khz_16bit_mono, var isBase64: Boolean = false) : Text2Speech {
            }

        val audioFormat: AudioFormat
            get() {
                val encoding = voiceRSSEncoding.encoding
                val rate = sampleRate.rate
                val sampleSize = bits?.num ?: 0
                val channelNum = channels.num
                val frameSize = sampleSize / 8 * channelNum

                return AudioFormat(encoding, rate, sampleSize, channelNum, frameSize, rate, false)
            }
    }

    }

    companion object {
        private const val BASE_ADDRESS = "http://api.voicerss.org/?key=%s&hl=%s&r=%d&c=%s&f=%s&ssml=false&b64=%s&src=%s"
        private const val VOICE_RSS_DEFAULT_API_KEY = "1def8e9c6ebf4a2eb02fc7b510b04387"
        // private const val VOICE_RSS_API_KEY = " bb123703dfc1486893ce391ab241ec54"; // oliver.treichel@gmx.de
    }

    val stream: AudioInputStream
        get() = AudioInputStream(url.openConnection().getInputStream(), format.audioFormat, AudioSystem.NOT_SPECIFIED.toLong())

    private val url: URL
        get() = URL(String.format(BASE_ADDRESS, apiKey, language.key, speed, codec, format.key, java.lang.Boolean.toString(isBase64), URLEncoder.encode(text, "UTF-8")))

    override fun convert(text: String, options: Map<String, String>): AudioInputStream {
        return convert(text, VoiceRssLanguage.forString(options["language"]!!))
    }

    private fun convert(text: String, language: VoiceRssLanguage): AudioInputStream {
        this.text = text
        this.language = language
        return stream
    }
}

