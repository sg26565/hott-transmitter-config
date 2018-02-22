package de.treichels.hott.tts.polly

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.polly.AmazonPolly
import com.amazonaws.services.polly.AmazonPollyClientBuilder
import com.amazonaws.services.polly.model.DescribeVoicesRequest
import com.amazonaws.services.polly.model.OutputFormat
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest
import com.amazonaws.services.polly.model.TextType
import de.treichels.hott.tts.*
import java.util.*
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class PollyTTSProvider(private val accessKey: String, private val secretKey: String) : Text2SpeechProvider() {
    private val credentialsProvider = AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
    private val polly: AmazonPolly = AmazonPollyClientBuilder.standard().withCredentials(credentialsProvider).withRegion(Regions.EU_CENTRAL_1).build()

    override val enabled: Boolean = true
    override val name = "Amazon Polly"
    override val defaultQuality = Quality(16000, 1)
    override val qualities: List<Quality> = listOf(Quality(8000, 1), Quality(16000, 1))
    override val speedSupported = false

    override fun speak(text: String, voice: Voice, speed: Int, volume: Int, sampleSize: Int, channels: Int, sampleRate: Int, ssml: Boolean): AudioInputStream {
        val quality = qualities.find { it.sampleRate == sampleRate } ?: defaultQuality
        val speechRequest = SynthesizeSpeechRequest()
                .withOutputFormat(OutputFormat.Pcm)
                .withVoiceId(voice.id)
                .withSampleRate(quality.sampleRate.toString())
                .withTextType(if (ssml) TextType.Ssml else TextType.Text)
                .withText(text)

        val speechResult = polly.synthesizeSpeech(speechRequest)
        val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate.toFloat(), 16, 1, 2, sampleRate.toFloat(), false)
        return AudioInputStream(speechResult.audioStream, format, AudioSystem.NOT_SPECIFIED.toLong())
    }

    override fun installedVoices(): List<Voice> {
        return polly.describeVoices(DescribeVoicesRequest()).voices.map { v ->
            Voice().apply {
                enabled = true
                age = Age.Unknown // Amazon has adult and child voices but does not specify them
                locale = Locale.forLanguageTag(v.languageCode)
                description = "${v.name} (${v.gender}) - ${v.languageName} (${v.languageCode})"
                gender = Gender.valueOf(v.gender)
                id = v.id
                name = v.name
            }
        }
    }
}