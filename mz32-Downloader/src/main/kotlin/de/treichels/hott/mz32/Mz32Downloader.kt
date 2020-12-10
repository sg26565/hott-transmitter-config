package de.treichels.hott.mz32

import de.treichels.hott.ui.ExceptionDialog
import de.treichels.hott.util.Util
import javafx.beans.property.SimpleBooleanProperty
import javafx.concurrent.Task
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import kotlin.reflect.KClass

fun main(vararg args: String) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<Mz32DownloaderApp>(*args)
}

class Mz32DownloaderApp : App() {
    override val primaryView: KClass<Mz32Downloader> = Mz32Downloader::class
}

class Mz32Downloader : View() {
    // Controls
    private var options by singleAssign<VBox>()
    private var reindex by singleAssign<CheckBox>()
    private var updateResources by singleAssign<CheckBox>()
    private var updateHelpPages by singleAssign<CheckBox>()
    private var updateVoiceFiles by singleAssign<CheckBox>()
    private var updateManuals by singleAssign<CheckBox>()
    private var language by singleAssign<CheckComboBox<Language>>()
    private var updateFirmware by singleAssign<CheckBox>()
    private var comboBox by singleAssign<ComboBox<Mz32>>()
    private var textArea by singleAssign<TextArea>()
    private var progressBarTotal by singleAssign<ProgressBar>()
    private var progressBarStep by singleAssign<ProgressBar>()
    private var updateButton by singleAssign<Button>()

    // Properties
    private val disableUI = SimpleBooleanProperty(false)

    // Background task
    private var task: Task<Unit>? = null

    // resources
    private val iconImage = resources.image("icon.png")

    // Helper
    private val doReindex
        get() = reindex.isSelected
    private val doUpdateResources
        get() = updateResources.isSelected
    private val doupdateHelpPages
        get() = updateHelpPages.isSelected
    private val doupdateVoiceFiles
        get() = updateVoiceFiles.isSelected
    private val doupdateManulals
        get() = updateManuals.isSelected
    private val doUpdateFirware
        get() = updateFirmware.isSelected
    private val mz32
        get() = comboBox.value
    private val selectedLanguages
        get() = language.selectedItems

    private val version: String by lazy {
        Util.sourceVersion(Mz32Downloader::class)
    }

    // UI
    override val root: VBox = vbox {
        prefWidth = 800.0
        spacing = 5.0
        paddingAll = 5.0

        hbox {
            spacing = 5.0
            paddingAll = 5.0

            options = vbox {
                spacing = 10.0
                disableProperty().bind(disableUI)

                reindex = checkbox(messages["reindex"]) {
                    tooltip = tooltip(messages["reindex_tooltip"])
                }

                vbox {
                    updateResources = checkbox(messages["update_resources"]) {
                        isSelected = true
                        tooltip = tooltip(messages["update_resources_tooltip"])

                    }

                    vbox {
                        enableWhen { updateResources.selectedProperty() }
                        padding = insets(left = 20.0)

                        updateHelpPages = checkbox(messages["update_help"]) {
                            preferences { isSelected = get("updateHelpPages", "true")!!.toBoolean() }
                            action { preferences { put("updateHelpPages", isSelected.toString()) } }

                            tooltip = tooltip(messages["update_help_tooltip"])
                        }

                        updateVoiceFiles = checkbox(messages["update_voice"]) {
                            preferences { isSelected = get("updateVoiceFiles", "true")!!.toBoolean() }
                            action { preferences { put("updateVoiceFiles", isSelected.toString()) } }

                            tooltip = tooltip(messages["update_voice_tooltip"])
                        }

                        updateManuals = checkbox(messages["update_manuals"]) {
                            preferences { isSelected = get("updateManuals", "true")!!.toBoolean() }
                            action { preferences { put("updateManuals", isSelected.toString()) } }

                            tooltip = tooltip(messages["update_manuals_tooltip"])
                        }
                    }
                }

                updateFirmware = checkbox(messages["update_firmware"]) {
                    isSelected = true
                    tooltip = tooltip(messages.getString("update_firmware_tooltip"))
                }
            }

            region {
                hgrow = Priority.SOMETIMES
            }

            vbox {
                spacing = 10.0
                disableProperty().bind(disableUI)

                vbox {
                    label(messages["drive"])
                    comboBox = combobox {
                        prefWidth = 275.0
                        setOnMousePressed { load() }
                    }
                }

                vbox {
                    enableWhen { updateResources.selectedProperty() }

                    label(messages["select_languages"])
                    language = opcr(this, CheckComboBox()) {
                        items.addAll(Language.values())

                        preferences {
                            get("languages", "en").split(',').forEach { lang ->
                                selectItem(Language.valueOf(lang))
                            }
                        }

                        selectedItems.onChange { change ->
                            preferences { put("languages", change.list.joinToString(",") { it.name }) }
                            layout()
                        }
                    }
                }
            }
        }

        label(messages["messages"])

        textArea = textarea {
            isEditable = false
            prefHeight = 200.0
            style = "-fx-font-family: monospace"
            vgrow = Priority.SOMETIMES
        }

        progressBarTotal = progressbar {
            prefWidthProperty().bind(textArea.widthProperty())
            visibleWhen(disableUI)
        }

        progressBarStep = progressbar {
            prefWidthProperty().bind(textArea.widthProperty())
            visibleWhen(disableUI)
        }

        buttonbar {
            updateButton = button(messages["download"])
        }
    }

    private fun armTask() {
        updateButton.text = messages["download"]
        updateButton.action { startTask() }
        updateButton.disableWhen { comboBox.valueProperty().isNull }
        disableUI.value = false
    }

    private fun startTask() {
        task = runAsync {
            if (doReindex) {
                mz32.scan(this, selectedLanguages)
            }
            mz32.update(
                this,
                selectedLanguages,
                doUpdateResources,
                doupdateHelpPages,
                doupdateVoiceFiles,
                doupdateManulals,
                doUpdateFirware
            )
            runLater { armTask() }
        }.apply {
            updateButton.text = messages["cancel"]
            updateButton.action { stopTask() }
            disableUI.value = true
            progressBarTotal.progressProperty().bind(progressProperty())
            progressBarStep.progressProperty().bind(progressProperty().divide(1000))
            messageProperty().addListener { _, _, newValue -> textArea.appendText(newValue) }
            root.layout()
        }
    }

    private fun stopTask() {
        updateButton.disableProperty().unbind()
        updateButton.isDisable = true
        task?.fail { armTask() }
        task?.success { armTask() }
        task?.cancel { armTask() }
        task?.cancel()
    }

    private fun ComboBox<Mz32>.load() {
        items.clear()
        items.addAll(Mz32.find())
        value = items.firstOrNull()
    }

    init {
        Util.enableLogging()

        setStageIcon(iconImage)
        title = "mz-32 Downloader ($version)"

        runLater {
            comboBox.load()
            armTask()
        }

        primaryStage.setOnCloseRequest { stopTask() }
        primaryStage.setOnShown { updateButton.requestFocus() }
    }
}
