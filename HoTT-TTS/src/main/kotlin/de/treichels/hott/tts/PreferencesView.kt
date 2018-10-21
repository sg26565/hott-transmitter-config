package de.treichels.hott.tts

import de.treichels.hott.tts.polly.PollyTTSProvider
import de.treichels.hott.tts.voicerss.VoiceRSSTTSProvider
import tornadofx.*

class PreferencesView : View() {
    private val prefsImage = resources.imageview("prefs.png")

    override val root = vbox {
        spacing = 5.0
        paddingAll = 5.0
        prefWidth = 250.0
        prefHeight = 245.0

        titledpane("VoiceRSS") {
            isCollapsible = false
            vbox {
                label("API Key")
                textfield {
                    text = VoiceRSSTTSProvider.apiKey

                    setOnKeyReleased {
                        if (text != null) VoiceRSSTTSProvider.apiKey = text
                    }
                }
            }
        }

        titledpane("Amazon Web Services") {
            isCollapsible = false
            vbox {
                label("Access Key ID")
                textfield {
                    text = PollyTTSProvider.accessKey

                    setOnKeyReleased {
                        if (text != null) PollyTTSProvider.accessKey = text
                    }
                }

                label("Secret Key")
                textfield {
                    text = PollyTTSProvider.secretKey

                    setOnKeyReleased {
                        if (text != null) PollyTTSProvider.secretKey = text
                    }
                }
            }
        }
    }

    init {
        icon = prefsImage
        title = messages["title"]
        primaryStage.sizeToScene()
    }
}
