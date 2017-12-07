package gde.model.voice

import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.*

private const val REGEXP = "[^a-zA-Z0-9 _-äöüÄÖÜß]"
private const val BASE_ADDRESS = "http://api.voicerss.org/?key=%s&hl=%s&r=%d&c=%s&f=%s&ssml=false&b64=%s&src=%s"
private const val VOICE_RSS_DEFAULT_API_KEY = "1def8e9c6ebf4a2eb02fc7b510b04387"
// private const val VOICE_RSS_API_KEY = " bb123703dfc1486893ce391ab241ec54"; // oliver.treichel@gmx.de

@Suppress("EnumEntryName")
enum class Bits {
    _8BIT, _16BIT
}

enum class Channels {
    MONO, STEREO
}

enum class Codec {
    MP3, WAV, AAC, OGG, CAF
}

@Suppress("EnumEntryName")
enum class Encoding {
    PCM, ALAW, ULAW
}

@Suppress("EnumEntryName")
enum class Format(private val encoding: Encoding, private val sampleRate: SampleRate, private val bits: Bits? = null, private val channels: Channels) {
    pcm_8khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_8khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_8khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_8khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_11khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_11khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_11khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_11khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
    pcm_12khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_12khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_12khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_12khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
    pcm_16khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_16khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_16khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_16khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
    pcm_22khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_22khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_22khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_22khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
    pcm_24khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_24khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_24khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_24khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
    pcm_32khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_32khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_32khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_32khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
    pcm_44khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_44khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_44khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_44khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits._16BIT, channels = Channels.STEREO),
    pcm_48khz_8bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._8BIT, channels = Channels.MONO),
    pcm_48khz_8bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._8BIT, channels = Channels.STEREO),
    pcm_48khz_16bit_mono(encoding = Encoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._16BIT, channels = Channels.MONO),
    pcm_48khz_16bit_stereo(encoding = Encoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits._16BIT, channels = Channels.STEREO), alaw_8khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.MONO),
    alaw_8khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.STEREO), alaw_11khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.MONO),
    alaw_11khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.STEREO), alaw_22khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.MONO),
    alaw_22khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.STEREO), alaw_44khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.MONO),
    alaw_44khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.STEREO), ulaw_8khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.MONO),
    ulaw_8khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.STEREO), ulaw_11khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.MONO),
    ulaw_11khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.STEREO), ulaw_22khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.MONO),
    ulaw_22khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.STEREO), ulaw_44khz_mono(encoding = Encoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.MONO),
    ulaw_44khz_stereo(encoding = Encoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.STEREO);

    val key: String
        get() {
            val sb = StringBuilder()

            if (encoding == Encoding.PCM)
                sb.append(sampleRate.name.substring(1)).append(bits!!.name)
            else
                sb.append(encoding.name).append(sampleRate.name)

            sb.append('-').append(channels.name)

            return sb.toString().toLowerCase()
        }
}

@Suppress("EnumEntryName")
enum class SampleRate {
    _8KHZ, _11KHZ, _12KHZ, _16KHZ, _22KHZ, _24KHZ, _32KHZ, _44KHZ, _48KHZ
}

class VoiceRSS(val apiKey: String = VOICE_RSS_DEFAULT_API_KEY, var text: String = "", var language: VoiceRssLanguage = VoiceRssLanguage.de_de, var speed: Int = 0, var codec: Codec = Codec.WAV, var format: Format = Format.pcm_22khz_16bit_mono, var isBase64: Boolean = false) : Text2Speech {
    // Give file a fancy name
    // remove unsafe characters
    // shorten to 17 characters
    val file: File
        @Throws(MalformedURLException::class, UnsupportedEncodingException::class, IOException::class)
        get() {
            val tmpdir = File(System.getProperty("java.io.tmpdir"))

            val name = text.replace(REGEXP.toRegex(), "")
            val filename = name.substring(0, Math.min(17, name.length))
            var file = File(tmpdir, filename + ".wav")

            while (file.exists())
                file = File(tmpdir, filename + "_" + UUID.randomUUID().toString().substring(0, 3) + ".wav")

            val connection = url.openConnection()
            connection.getInputStream().use { `is` -> FileOutputStream(file).use { os -> IOUtils.copy(`is`, os) } }

            return file
        }

    private val url: URL
        @Throws(MalformedURLException::class, UnsupportedEncodingException::class)
        get() = URL(String.format(BASE_ADDRESS, apiKey, language.key, speed, codec, format.key,
                java.lang.Boolean.toString(isBase64), URLEncoder.encode(text, "UTF-8")))

    @Throws(Exception::class)
    override fun convert(text: String, language: String): File {
        return convert(text, VoiceRssLanguage.forString(language))
    }

    @Throws(MalformedURLException::class, UnsupportedEncodingException::class, IOException::class)
    private fun convert(text: String, language: VoiceRssLanguage): File {
        this.text = text
        this.language = language
        return file
    }
}
