package de.treichels.hott.tts

import de.treichels.hott.model.voice.Player
import de.treichels.hott.model.voice.VoiceData
import de.treichels.hott.tts.voicerss.Bits
import de.treichels.hott.tts.voicerss.Format
import de.treichels.hott.tts.voicerss.VoiceRSSEncoding
import de.treichels.hott.tts.voicerss.VoiceRssLanguage
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
import javazoom.spi.mpeg.sampled.convert.MpegFormatConversionProvider
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader
import javazoom.spi.vorbis.sampled.convert.VorbisFormatConversionProvider
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader
import org.apache.commons.io.IOUtils
import tornadofx.*
import java.io.File
import java.io.FileOutputStream
import java.net.UnknownHostException

fun main(args: Array<String>) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<SpeechApp>(args)
}

// static references to mp3 and ogg decoders to keep them in the final jar
private val IGNORE = arrayOf(MpegAudioFileReader::class.java, MpegFormatConversionProvider::class.java, VorbisAudioFileReader::class.java, VorbisFormatConversionProvider::class.java)

private var standalone = false

class SpeechApp : App() {
    override val primaryView = SpeechDialog::class

    init {
        standalone = true
    }
}

class SpeechDialog : View() {
    companion object {
        private const val PREFERRED_LANGUAGE = "preferredLanguage"
        private const val PREFERRED_FORMAT = "preferredFormat"
        private const val LAST_FOLDER = "lastFolder"
        private const val LAST_TEXT = "lastText"
        private const val EXT_WAV = "*.wav"
    }

    private val service = Text2SpeechService().apply {
        setOnFailed {
            onFail(exception)
        }
    }

    // Controls
    private var progressBar by singleAssign<ProgressBar>()
    private var textArea by singleAssign<TextArea>()
    private var languageComboBox by singleAssign<ComboBox<VoiceRssLanguage>>()
    private var formatComboBox by singleAssign<ComboBox<Format>>()
    private var volumeSlider by singleAssign<Slider>()
    private var speedSlider by singleAssign<Slider>()

    // Helpers
    private val language: VoiceRssLanguage
        get() = languageComboBox.selectionModel.selectedItem
    private val text: String
        get() = textArea.textProperty().value
    private val volume: Double
        get() = volumeSlider.value / 2.0
    private val speed: Int
        get() = speedSlider.value.toInt() * 2 - 10
    private val format: Format
        get() = formatComboBox.selectionModel.selectedItem

    override val root = vbox {
        spacing = 5.0
        padding = insets(all = 10)
        prefWidth = 800.0
        prefHeight = 200.0

        label(messages["descr_label"])

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

            label(messages["language_label"])

            languageComboBox = combobox {
                items = listOf(*VoiceRssLanguage.values()).observable()

                setOnAction {
                    preferences { put(PREFERRED_LANGUAGE, language.name) }
                }

                // get preferred language from prefs
                preferences {
                    selectionModel.select(VoiceRssLanguage.forString(get(PREFERRED_LANGUAGE, "de_de")))
                }
            }

            region {
                hgrow = Priority.SOMETIMES
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
            }
        }

        if (standalone) {
            hbox {
                alignment = Pos.CENTER_LEFT
                spacing = 10.0

                label(messages["format"])
                formatComboBox = combobox {
                    items = Format.values().filter { it.bits == Bits._16BIT && it.voiceRSSEncoding == VoiceRSSEncoding.PCM }.observable()

                    setOnAction {
                        preferences { put(PREFERRED_FORMAT, format.name) }
                    }

                    // get preferred language from prefs
                    preferences {
                        selectionModel.select(Format.valueOf(get(PREFERRED_FORMAT, "pcm_11khz_16bit_mono")))
                    }
                }
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

    init {
        title = messages["title"]
        preferences {
            textArea.text = get(LAST_TEXT, "")
        }

        textArea.requestFocus()
    }

    private fun onFail(t: Throwable) {
        if (t is UnknownHostException)
            MessageDialog.show(AlertType.ERROR, messages["error"], messages["unknown_Host"])
        else
            ExceptionDialog.show(t)

        onAbort()
    }

    private fun reStartService(op: () -> Unit = {}) {
        service.reset()
        service.text = text
        service.language = language

        if (standalone) {
            service.format = format
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

            FileOutputStream(this).use {
                IOUtils.copy(service.value, it)
            }
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
