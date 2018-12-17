import de.treichels.hott.voice.Player
import de.treichels.hott.tts.Gender
import de.treichels.hott.tts.polly.PollyTTSProvider
import de.treichels.hott.tts.voicerss.VoiceRSSTTSProvider
import de.treichels.hott.tts.win10.Win10TTSProvider

fun main(vararg args: String) {
    val providers = listOf(VoiceRSSTTSProvider(), Win10TTSProvider(), PollyTTSProvider())

    providers.forEach { provider ->
        if (provider.enabled) {
            provider.installedVoices().filter { it.locale.language == "en" }.forEach { voice ->
                val text = "This is a test using the ${provider.name} text-to-speech engine using the ${voice.gender} ${voice.name} voice for ${voice.locale.getDisplayLanguage(voice.locale)} in ${voice.locale.getDisplayCountry(voice.locale)}."
                println(text)
                Player.play(provider.speak(text, voice))
            }
        }
    }

    providers.forEach { provider ->
        if (provider.enabled) {
            provider.installedVoices().filter { it.locale.language == "de" }.forEach { voice ->
                val gender = when (voice.gender) {
                    Gender.Male -> "männlichen"
                    Gender.Female -> "weiblichen"
                    Gender.Unknown -> ""
                }
                val text = "Dies ist ein Test der ${provider.name} Sprachsynthese mit der $gender ${voice.name} Stimme für ${voice.locale.getDisplayLanguage(voice.locale)} in ${voice.locale.getDisplayCountry(voice.locale)}."
                println(text)
                Player.play(provider.speak(text, voice))
            }
        }
    }
}