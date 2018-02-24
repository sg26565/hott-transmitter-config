package de.treichels.hott.tts.polly

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.polly.AmazonPolly
import com.amazonaws.services.polly.AmazonPollyClientBuilder
import com.amazonaws.services.polly.model.DescribeVoicesRequest
import com.amazonaws.services.polly.model.OutputFormat
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest
import com.amazonaws.services.polly.model.TextType
import de.treichels.hott.tts.*
import java.util.*
import java.util.prefs.Preferences
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class PollyTTSProvider : Text2SpeechProvider() {
    companion object {
        private val PREFS = Preferences.userNodeForPackage(PollyTTSProvider::class.java)
        private const val ACCESS_KEY = "accessKey"
        private const val SECRET_KEY = "secretKey"

        internal var accessKey: String?
            get() = PREFS.get(ACCESS_KEY, null)
            set(key) {
                PREFS.put(ACCESS_KEY, key)
            }

        internal var secretKey: String?
            get() = PREFS.get(SECRET_KEY, null)
            set(key) {
                PREFS.put(SECRET_KEY, key)
            }
    }

    class PreferencesCredentialProvider : AWSCredentialsProvider {
        override fun refresh() {}
        override fun getCredentials() = if (!accessKey.isNullOrBlank() && !secretKey.isNullOrBlank()) BasicAWSCredentials(accessKey, secretKey) else null
    }

    private val credentialsProvider = AWSCredentialsProviderChain(PreferencesCredentialProvider(), DefaultAWSCredentialsProviderChain())
    private val polly: AmazonPolly = AmazonPollyClientBuilder.standard().withCredentials(credentialsProvider).withRegion(Regions.EU_CENTRAL_1).build()

    override val enabled: Boolean
        get() = try {
            credentialsProvider.credentials != null
        } catch (e: Throwable) {
            false
        }

    override val name = "Amazon Polly"
    override val defaultQuality = Quality(16000, 1)
    override val qualities: List<Quality> = listOf(Quality(8000, 1), Quality(16000, 1))
    override val speedSupported = false

    override fun speak(text: String, voice: Voice, speed: Int, volume: Int, quality: Quality, ssml: Boolean): AudioInputStream {
        val speechRequest = SynthesizeSpeechRequest()
                .withOutputFormat(OutputFormat.Pcm)
                .withVoiceId(voice.id)
                .withSampleRate(quality.sampleRate.toString())
                .withTextType(if (ssml) TextType.Ssml else TextType.Text)
                .withText(text)

        val speechResult = polly.synthesizeSpeech(speechRequest)
        val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, quality.sampleRate.toFloat(), quality.sampleSize, quality.channels, quality.sampleSize / 8, quality.sampleRate.toFloat(), false)
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