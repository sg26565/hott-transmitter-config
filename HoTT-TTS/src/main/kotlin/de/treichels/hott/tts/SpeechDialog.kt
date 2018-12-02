package de.treichels.hott.tts

import de.treichels.hott.voice.Player
import de.treichels.hott.voice.VoiceData
import de.treichels.hott.tts.polly.PollyTTSProvider
import de.treichels.hott.tts.voicerss.VoiceRSSTTSProvider
import de.treichels.hott.tts.win10.Win10TTSProvider
import de.treichels.hott.util.ExceptionDialog
import de.treichels.hott.util.MessageDialog
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ComboBox
import javafx.scene.control.ProgressBar
import javafx.scene.control.Slider
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File
import java.net.UnknownHostException
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioSystem

fun main(args: Array<String>) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<SpeechApp>(args)
}

private var standalone = false

class SpeechApp : App() {
    override val primaryView = SpeechDialog::class

    init {
        standalone = true
    }
}

class SpeechDialog : View() {
    companion object {
        private const val PREFERRED_PROVIDER = "preferredProvider"
        private const val PREFERRED_VOICE = "preferredLanguage"
        private const val PREFERRED_FORMAT = "preferredFormat"
        private const val LAST_FOLDER = "lastFolder"
        private const val LAST_TEXT = "lastText"
        private const val EXT_WAV = "*.wav"
    }

    // resources
    private val iconImage = resources.image("icon.png")
    private val prefsImage = resources.imageview("prefs.png")

    // dialog
    private val prefs by inject<PreferencesView>()

    // background service
    private val service = Text2SpeechService().apply {
        setOnFailed {
            onFail(exception)
        }
    }

    // State
    private var providers = listOf(Win10TTSProvider(), VoiceRSSTTSProvider(), PollyTTSProvider())

    // Controls
    private lateinit var progressBar: ProgressBar
    private lateinit var textArea: TextArea
    private lateinit var providerComboBox: ComboBox<Text2SpeechProvider>
    private lateinit var voiceComboBox: ComboBox<Voice>
    private lateinit var qualityComboBox: ComboBox<Quality>
    private lateinit var volumeSlider: Slider
    private lateinit var speedSlider: Slider

    // Helpers
    private val provider: Text2SpeechProvider
        get() = providerComboBox.selectionModel.selectedItem
    private val voice: Voice
        get() = voiceComboBox.selectedItem ?: provider.defaultVoice
    private val text: String
        get() = textArea.textProperty().value
    private val volume: Double
        get() = volumeSlider.value / 3.0
    private val speed: Int
        get() = speedSlider.value.toInt() * 2 - 10
    private val quality: Quality
        get() = qualityComboBox.selectedItem ?: provider.defaultQuality

    override val root = vbox {
        spacing = 5.0
        padding = insets(all = 10)
        prefWidth = 700.0
        prefHeight = 250.0

        hbox {
            label(messages["descr_label"])

            region {
                hgrow = Priority.ALWAYS
            }

            button(graphic = prefsImage) {
                isFocusTraversable = false
                action {
                    prefs.openModal(StageStyle.UTILITY)
                }
            }
        }

        anchorpane {
            textArea = textarea {
                isFocusTraversable = false
                textProperty().onChange {
                    preferences { put(LAST_TEXT, textArea.text) }
                }
                anchorpaneConstraints {
                    topAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }
            }
        }


        hbox {
            alignment = Pos.CENTER
            spacing = 10.0

            vbox {
                label(messages["provider_label"])
                providerComboBox = combobox {
                    initProvider()
                }
            }

            if (standalone) {
                region {
                    hgrow = Priority.SOMETIMES
                }

                vbox {
                    label(messages["quality"])
                    qualityComboBox = combobox {
                        initQuality()
                    }
                }
            }

            region {
                hgrow = Priority.ALWAYS
            }

            label(messages["volume"])
            volumeSlider = slider {
                valueProperty().onChange {
                    preferences { putDouble("prefVolume", it) }
                }

                preferences {
                    value = getDouble("prefVolume", 5.0)
                }

                min = 1.0
                max = 10.0
                isShowTickMarks = true
                isShowTickLabels = true
                blockIncrement = 0.5
                majorTickUnit = 1.0
                minorTickCount = 1
                isDisable = !provider.volumeSupported
            }
        }

        hbox {
            alignment = Pos.CENTER
            spacing = 10.0


            vbox {
                label(messages["language_label"])
                voiceComboBox = combobox {
                    initVoices()
                }
            }

            region {
                hgrow = Priority.ALWAYS
            }

            label(messages["speed"])
            speedSlider = slider {
                valueProperty().onChange {
                    preferences { putDouble("prefSpeed", it) }
                }

                preferences {
                    value = getDouble("prefSpeed", 5.0)
                }

                min = 0.0
                max = 10.0
                isShowTickMarks = true
                isShowTickLabels = true
                blockIncrement = 0.5
                majorTickUnit = 1.0
                minorTickCount = 1
                isDisable = !provider.speedSupported
            }
        }

        anchorpane {
            prefHeight = 20.0
            progressBar = progressbar {
                progressProperty().bind(service.runningProperty().integerBinding { if (it == true) -1 else 0 })
                anchorpaneConstraints {
                    topAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }
            }
        }

        hbox {
            alignment = Pos.BASELINE_RIGHT
            spacing = 10.0

            button(messages["play_button"]) {
                disableWhen { textArea.lengthProperty().isEqualTo(0).or(service.runningProperty()) }
                action { onPlay() }
            }

            button {
                text = if (standalone) messages["save_button"] else messages["start_button"]
                disableWhen { textArea.lengthProperty().isEqualTo(0).or(service.runningProperty()) }
                isDefaultButton = true
                action { onStart() }
            }

            button {
                text = if (standalone) messages["close_button"] else messages["abort_button"]
                isCancelButton = true
                action { onAbort() }
            }
        }
    }

    private fun ComboBox<Text2SpeechProvider>.initProvider() {
        items = providers.filter { it.enabled }.observable()

        setOnAction {
            preferences { put(PREFERRED_PROVIDER, provider.name) }
            voiceComboBox.initVoices()
            if (standalone) qualityComboBox.initQuality()

            volumeSlider.isDisable = !provider.volumeSupported
            speedSlider.isDisable = !provider.speedSupported
        }

        // get preferred provider from prefs
        preferences {
            val prefProviderName: String? = get(PREFERRED_PROVIDER, null)
            selectionModel.select(items.find { it.name == prefProviderName } ?: items[0])
        }
    }

    private fun ComboBox<Quality>.initQuality() {
        items = provider.qualities.observable()

        setOnAction {
            preferences { put(PREFERRED_FORMAT, quality.toString()) }
        }

        // get preferred quality from prefs
        preferences {
            val prefFormatName: String? = get(PREFERRED_FORMAT, null)
            selectionModel.select(provider.qualities.find { it.toString() == prefFormatName }
                    ?: provider.defaultQuality)
        }
    }

    private fun ComboBox<Voice>.initVoices() {
        try {
            items = provider.installedVoices().observable()

            setOnAction {
                preferences { put(PREFERRED_VOICE, voice.id) }
            }

            // get preferred language from prefs
            preferences {
                val prefVoiceName: String? = get(PREFERRED_VOICE, null)
                selectionModel.select(provider.installedVoices().find { it.id == prefVoiceName }
                        ?: provider.defaultVoice)
            }
        } catch (e: Exception) {
            ExceptionDialog.show(e)
        }
    }

    init {
        if (standalone) setStageIcon(iconImage)
        title = messages["title"]

        preferences {
            textArea.text = get(LAST_TEXT, "")
        }

        Platform.runLater { textArea.requestFocus() }
    }

    private fun onFail(t: Throwable) {
        if (t is UnknownHostException)
            MessageDialog.show(AlertType.ERROR, messages["error"], messages["unknown_Host"])
        else
            ExceptionDialog.show(t)
    }

    private fun reStartService(op: () -> Unit = {}) {
        service.reset()
        service.provider = provider
        service.text = text
        service.voice = voice
        if (standalone) {
            service.frameRate = quality.sampleRate
            service.channels = quality.channels
        } else {
            service.frameRate = provider.defaultQuality.sampleRate
            service.channels = provider.defaultQuality.channels
        }
        service.volume = volume
        service.speed = speed
        service.start()
        service.setOnSucceeded { op() }
    }

    private fun onPlay() {
        reStartService {
            val stream = service.value
            runAsync {
                if (standalone)
                // play as-is
                    Player.play(stream)
                else
                // convert to ADPCM and play
                    VoiceData.forStream(sourceAudioStream = stream, name = "test", volume = service.volume).play()
            }
        }
    }

    private fun onAbort() {
        service.cancel()

        if (standalone)
            Platform.exit()
        else
            close()
    }

    private fun onStart() {
        reStartService()

        if (standalone)
            save()
        else
            close()
    }

    private fun save() {
        FileChooser().apply {
            title = messages["save_title"]
            extensionFilters.add(FileChooser.ExtensionFilter(messages["wav_files"], EXT_WAV))

            preferences {
                val folder = File(get(LAST_FOLDER, System.getProperty("user.home")))
                if (folder.exists() && folder.isDirectory)
                    initialDirectory = folder
            }
        }.showSaveDialog(primaryStage)?.apply {
            preferences { put(LAST_FOLDER, parentFile.absolutePath) }
            AudioSystem.write(service.value, AudioFileFormat.Type.WAVE, this)
        }
    }

    fun openDialog(): Text2SpeechService {
        preferences {
            textArea.text = get(LAST_TEXT, "")
        }

        textArea.requestFocus()

        openModal(stageStyle = StageStyle.UTILITY, modality = Modality.APPLICATION_MODAL, block = true)?.setOnCloseRequest {
            onAbort()
        }

        return service
    }
}
