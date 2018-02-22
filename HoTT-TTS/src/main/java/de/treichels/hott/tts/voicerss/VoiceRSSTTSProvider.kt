package de.treichels.hott.tts.voicerss

import com.sun.media.sound.WaveFileReader
import de.treichels.hott.tts.*
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.sound.sampled.AudioInputStream

class VoiceRSSTTSProvider(private val apiKey: String = VOICE_RSS_DEFAULT_API_KEY) : Text2SpeechProvider() {
    override val enabled = true
    override val name = "VoiceRSS"
    override val qualities: List<Quality> = SampleRate.values().flatMap {
        listOf(Quality(it.rate.toInt(), 1), Quality(it.rate.toInt(), 2))
    }
    override val ssmlSupported: Boolean = false

    companion object {
        //private const val VOICE_RSS_DEFAULT_API_KEY = "1def8e9c6ebf4a2eb02fc7b510b04387"
        private const val VOICE_RSS_DEFAULT_API_KEY = "bb123703dfc1486893ce391ab241ec54"
    }

    override fun installedVoices(): List<Voice> {
        val languages = VoiceRssLanguage.values()
        return List(languages.size) { i ->
            val lang = languages[i]

            Voice().apply {
                enabled = true
                age = Age.Adult // VoiceRSS has only adult voices
                locale = Locale.forLanguageTag(lang.name.replace("_", "-"))
                description = lang.toString()
                gender = Gender.Female // VoiceRSS has only female voices
                id = lang.key
                name = locale.getDisplayLanguage(locale) // VoiceRSS does not provide a name for its voices - use the language instead
            }
        }
    }

    override fun speak(text: String, voice: Voice, speed: Int, volume: Int, sampleSize: Int, channels: Int, sampleRate: Int, ssml: Boolean): AudioInputStream {
        if (ssml) throw UnsupportedOperationException("SSML markup is not supported")
        if (channels != 1 && channels != 2) throw IllegalArgumentException("choose either one (mono) to two channels (stereo)")

        // test format - throws exception if not valid
        val format = Format.valueOf("pcm_${sampleRate / 1000}khz_${sampleSize}bit_${if (channels == 1) "mono" else "stereo"}")

        val url = URL("http://api.voicerss.org/?key=$apiKey&hl=${voice.id}&r=$speed&c=WAV&f=${format.key}&ssml=false&b64=false&src=${URLEncoder.encode(text, "UTF-8")}")
        return WaveFileReader().getAudioInputStream(url.openConnection().getInputStream())
    }
}

