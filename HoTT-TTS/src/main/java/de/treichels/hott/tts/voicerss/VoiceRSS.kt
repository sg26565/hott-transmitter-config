package de.treichels.hott.tts.voicerss

import com.sun.media.sound.WaveFileReader
import de.treichels.hott.tts.Text2Speech
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader
import java.net.URL
import java.net.URLEncoder
import javax.sound.sampled.AudioInputStream

class VoiceRSS(private val apiKey: String = VOICE_RSS_DEFAULT_API_KEY, var text: String = "", var language: VoiceRssLanguage = VoiceRssLanguage.de_de, var speed: Int = 0, var codec: Codec = Codec.WAV, var format: Format = Format.pcm_22khz_16bit_mono, var isBase64: Boolean = false) : Text2Speech {
    companion object {
        //private const val VOICE_RSS_DEFAULT_API_KEY = "1def8e9c6ebf4a2eb02fc7b510b04387"
        private const val VOICE_RSS_DEFAULT_API_KEY = "bb123703dfc1486893ce391ab241ec54"
    }

    val stream: AudioInputStream
        get() {
            val audioFileReader = when (codec) {
                Codec.MP3 -> MpegAudioFileReader()
                Codec.WAV -> WaveFileReader()
                Codec.AAC -> TODO()
                Codec.OGG -> VorbisAudioFileReader()
                Codec.CAF -> TODO()
            }

            val openConnection = url.openConnection()
            val inputStream = openConnection.getInputStream()
            return audioFileReader.getAudioInputStream(inputStream)
        }

    private val url: URL
        get() {
            val spec = "http://api.voicerss.org/?key=$apiKey&hl=${language.key}&r=$speed&c=${codec.name}&f=${format.key}&ssml=false&b64=$isBase64&src=${URLEncoder.encode(text, "UTF-8")}"
            println(spec)
            return URL(spec)
        }

    override fun convert(text: String, options: Map<String, String>): AudioInputStream {
        return convert(text, VoiceRssLanguage.forString(options["language"]!!))
    }

    private fun convert(text: String, language: VoiceRssLanguage): AudioInputStream {
        this.text = text
        this.language = language
        return stream
    }
}

