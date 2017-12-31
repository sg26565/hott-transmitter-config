package de.treichels.hott.model.voice

import java.net.URL
import java.net.URLEncoder
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class VoiceRSS(private val apiKey: String = VOICE_RSS_DEFAULT_API_KEY, var text: String = "", var language: VoiceRssLanguage = VoiceRssLanguage.de_de, var speed: Int = 0, var codec: Codec = Codec.WAV, var format: Format = Format.pcm_22khz_16bit_mono, var isBase64: Boolean = false) : Text2Speech {
    @Suppress("EnumEntryName")
    enum class Bits(val num: Int) {
        _8BIT(8), _16BIT(16)
    }

    enum class Channels(val num: Int) {
        MONO(1), STEREO(2)
    }

    enum class Codec {
        MP3, WAV, AAC, OGG, CAF
    }

    @Suppress("EnumEntryName")
    enum class VoiceRSSEncoding(val encoding: AudioFormat.Encoding) {
        PCM(AudioFormat.Encoding.PCM_SIGNED), ALAW(AudioFormat.Encoding.ALAW), ULAW(AudioFormat.Encoding.ULAW)
    }

    @Suppress("EnumEntryName")
    enum class Format(private val voiceRSSEncoding: VoiceRSSEncoding, private val sampleRate: SampleRate, private val bits: Bits? = null, private val channels: Channels) {
        pcm_8khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_8khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_8khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_8khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_11khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_11khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_11khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_11khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
        pcm_12khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_12khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_12khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_12khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
        pcm_16khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_16khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_16khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_16khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
        pcm_22khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_22khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_22khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_22khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
        pcm_24khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_24khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_24khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_24khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
        pcm_32khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_32khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_32khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_32khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
        pcm_44khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_44khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_44khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_44khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
        pcm_48khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._8BIT, channels = Channels.MONO),
        pcm_48khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
        pcm_48khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._16BIT, channels = Channels.MONO),
        pcm_48khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._16BIT, channels = Channels.STEREO), alaw_8khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.MONO),
        alaw_8khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.STEREO), alaw_11khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.MONO),
        alaw_11khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.STEREO), alaw_22khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.MONO),
        alaw_22khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.STEREO), alaw_44khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.MONO),
        alaw_44khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.STEREO), ulaw_8khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.MONO),
        ulaw_8khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.STEREO), ulaw_11khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.MONO),
        ulaw_11khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.STEREO), ulaw_22khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.MONO),
        ulaw_22khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.STEREO), ulaw_44khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.MONO),
        ulaw_44khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.STEREO);

        val key: String
            get() {
                val sb = StringBuilder()

                if (voiceRSSEncoding == VoiceRSSEncoding.PCM)
                    sb.append(sampleRate.name.substring(1)).append(bits!!.name)
                else
                    sb.append(voiceRSSEncoding.name).append(sampleRate.name)

                sb.append('_').append(channels.name)

                return sb.toString().toLowerCase()
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

    @Suppress("EnumEntryName")
    enum class SampleRate(val rate: Float) {
        _8KHZ(8000f), _11KHZ(11025f), _12KHZ(12000f), _16KHZ(16000f), _22KHZ(22050f), _24KHZ(24000f), _32KHZ(32000f), _44KHZ(44100f), _48KHZ(48000f)
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

