package de.treichels.hott.vdfeditor.ui.tts

import de.treichels.hott.vdfeditor.ui.ExceptionDialog
import de.treichels.hott.vdfeditor.ui.MessageDialog
import gde.model.voice.VoiceRssLanguage
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.stage.Modality
import javafx.stage.StageStyle
import tornadofx.*
import java.net.UnknownHostException

private const val PREFERRED_LANGUAGE = "preferredLanguage"

internal class SpeechDialog : View() {
    private var task: Text2SpeechTask? = null
    private var bgTask: Task<Unit?>? = null

    // Controls
    private var progressBar by singleAssign<ProgressBar>()
    private var textArea by singleAssign<TextArea>()
    private var languageComboBox by singleAssign<ComboBox<VoiceRssLanguage>>()
    private var startButton by singleAssign<Button>()
    private var abortButton by singleAssign<Button>()

    // Helpers
    private val language: VoiceRssLanguage
        get() = languageComboBox.selectionModel.selectedItem
    private val text: String
        get() = textArea.textProperty().value

    override val root = vbox {
        spacing = 5.0
        padding = insets(all = 10)
        prefWidth = 600.0
        prefHeight = 300.0

        label(messages["descr_label"])

        anchorpane {
            textArea = textarea {
                isFocusTraversable = false
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
                setOnAction { onLanguageChanged() }
            }

            region {
                hgrow = Priority.SOMETIMES
            }

            startButton = button(messages["start_button"]) {
                isDefaultButton = true
                action { onStart() }
            }

            abortButton = button(messages["abort_button"]) {
                isCancelButton = true
                action { onAbort() }
            }
        }

        anchorpane {
            prefHeight = 20.0
            progressBar = progressbar {
                progress = 0.0
                anchorpaneConstraints {
                    topAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }
            }
        }
    }

    init {
        title = messages["title"]

        // get preferred language from prefs
        preferences {
            val prefLanguage = VoiceRssLanguage.forString(get(PREFERRED_LANGUAGE, "de_de"))
            languageComboBox.items = listOf(*VoiceRssLanguage.values()).observable()
            languageComboBox.selectionModel.select(prefLanguage)
        }
    }

    private fun onFail(t: Throwable) {
        if (t is UnknownHostException)
            MessageDialog.show(AlertType.ERROR, messages["error"], messages["unknown_Host"])
        else
            ExceptionDialog.show(t)

        onAbort()
    }

    private fun onAbort() {
        progressBar.progress = 0.0
        task?.cancel()
        bgTask?.cancel()
        close()
    }

    private fun onLanguageChanged() {
        // save selected language as new preferred language
        preferences { put(PREFERRED_LANGUAGE, language.name) }
    }

    private fun onStart() {
        task?.text = text
        task?.language = language
        progressBar.progress = -1.0

        bgTask = runAsync {
            task?.run()
        } success {
            close()
        } fail (this::onFail)
    }

    fun openDialog(task: Text2SpeechTask) {
        this.task = task
        task.fail(this::onFail)

        progressBar.progress = 0.0

        textArea.text = ""
        textArea.requestFocus()

        // disable start button if text area contains no text or the task is already running
        startButton.disableProperty().bind(textArea.lengthProperty().isEqualTo(0).or(task.runningProperty()))

        openModal(stageStyle = StageStyle.UTILITY, modality = Modality.APPLICATION_MODAL)?.setOnCloseRequest {
            onAbort()
        }
    }
}
