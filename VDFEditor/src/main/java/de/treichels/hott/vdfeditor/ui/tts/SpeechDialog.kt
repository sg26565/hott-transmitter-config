package de.treichels.hott.vdfeditor.ui.tts

import de.treichels.hott.model.voice.Player
import de.treichels.hott.model.voice.VoiceRssLanguage
import de.treichels.hott.util.ExceptionDialog
import de.treichels.hott.vdfeditor.ui.MessageDialog
import javafx.geometry.Pos
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ComboBox
import javafx.scene.control.ProgressBar
import javafx.scene.control.Slider
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.stage.Modality
import javafx.stage.StageStyle
import tornadofx.*
import java.net.UnknownHostException

private const val PREFERRED_LANGUAGE = "preferredLanguage"
private const val LAST_TEXT = "lastText"

internal class SpeechDialog : View() {
    private val service = Text2SpeechService().apply {
        setOnFailed {
            onFail(exception)
        }
    }

    // Controls
    private var progressBar by singleAssign<ProgressBar>()
    private var textArea by singleAssign<TextArea>()
    private var languageComboBox by singleAssign<ComboBox<VoiceRssLanguage>>()
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

    override val root = vbox {
        spacing = 5.0
        padding = insets(all = 10)
        prefWidth = 850.0
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

            button(messages["start_button"]) {
                disableWhen { textArea.lengthProperty().isEqualTo(0).or(service.runningProperty()) }
                isDefaultButton = true
                action { onStart() }
            }

            button(messages["abort_button"]) {
                isCancelButton = true
                action { onAbort() }
            }
        }
    }

    init {
        title = messages["title"]
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
        service.volume = volume
        service.speed = speed
        service.start()
        service.setOnSucceeded { op() }
    }

    private fun onPlay() {
        reStartService {
            val stream = service.value
            runAsync {
                Player.play(stream)
            }
        }
    }

    private fun onAbort() {
        service.cancel()
        close()
    }

    private fun onStart() {
        reStartService()
        close()
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
