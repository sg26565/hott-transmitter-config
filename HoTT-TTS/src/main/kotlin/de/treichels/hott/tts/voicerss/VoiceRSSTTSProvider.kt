package de.treichels.hott.tts.voicerss

import de.treichels.hott.tts.*
import java.net.URL
import java.net.URLEncoder
import java.util.*
import java.util.prefs.Preferences
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class VoiceRSSTTSProvider : Text2SpeechProvider() {
    companion object {
        private val PREFS = Preferences.userNodeForPackage(VoiceRSSTTSProvider::class.java)
        //private const val VOICE_RSS_DEFAULT_API_KEY = "1def8e9c6ebf4a2eb02fc7b510b04387"
        private const val VOICE_RSS_DEFAULT_API_KEY = "bb123703dfc1486893ce391ab241ec54"
        private const val API_KEY = "apiKey"

        internal var apiKey: String?
            get() = PREFS.get(API_KEY, null)
            set(key) {
                PREFS.put(API_KEY, key)
            }
    }

    override val enabled = true
    override val name = "VoiceRSS"
    override val qualities: List<Quality> = SampleRate.values().flatMap {
        listOf(Quality(it.rate.toInt(), 1), Quality(it.rate.toInt(), 2))
    }
    override val ssmlSupported: Boolean = false


    override fun installedVoices() = VoiceRssLanguage.values().map { lang ->
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

    override fun speak(text: String, voice: Voice, speed: Int, volume: Int, quality: Quality, ssml: Boolean): AudioInputStream {
        if (ssml) throw UnsupportedOperationException("SSML markup is not supported")

        // test format - throws exception if not valid
        val format = Format.valueOf("pcm_${quality.sampleRate / 1000}khz_${quality.sampleSize}bit_${if (quality.channels == 1) "mono" else "stereo"}")
        val key = if (apiKey.isNullOrBlank()) VOICE_RSS_DEFAULT_API_KEY else apiKey!!
        val url = URL("http://api.voicerss.org/?key=$key&hl=${voice.id}&r=$speed&c=WAV&f=${format.key}&ssml=false&b64=false&src=${URLEncoder.encode(text, "UTF-8")}")
        return AudioSystem.getAudioInputStream(url)
    }
}

