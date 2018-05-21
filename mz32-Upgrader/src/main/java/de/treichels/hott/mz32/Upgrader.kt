package de.treichels.hott.mz32

import de.treichels.hott.util.ExceptionDialog
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import org.apache.logging.log4j.jcl.LogFactoryImpl
import org.apache.logging.log4j.util.EnvironmentPropertySource
import org.apache.logging.log4j.util.SystemPropertiesPropertySource
import tornadofx.*

private val ignored = listOf(LogFactoryImpl::class, EnvironmentPropertySource::class, SystemPropertiesPropertySource::class)

fun main(vararg args: String) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<Mz32UpgraderApp>(*args)
}

class Mz32UpgraderApp : App() {
    override val primaryView = Mz32Upgrader::class
}

class Mz32Upgrader : View() {
    // Controls
    private var reindex by singleAssign<CheckBox>()
    private var updateResources by singleAssign<CheckBox>()
    private var replaceHelpPages by singleAssign<CheckBox>()
    private var replaceVoiceFiles by singleAssign<CheckBox>()
    private var updateFirmware by singleAssign<CheckBox>()
    private var comboBox by singleAssign<ComboBox<Mz32>>()
    private var textArea by singleAssign<TextArea>()
    private var updateButton by singleAssign<Button>()

    // Background task
    private var task: Task<Unit>? = null

    // resources
    private val iconImage = resources.image("icon.png")

    // Helper
    private val doReindex
        get() = reindex.isSelected
    private val doUpdateResources
        get() = updateResources.isSelected
    private val doReplaceHelpPages
        get() = replaceHelpPages.isSelected
    private val doReplaceVoiceFiles
        get() = replaceVoiceFiles.isSelected
    private val doUpdateFirware
        get() = updateFirmware.isSelected
    private val mz32
        get() = comboBox.value

    // UI
    override val root = vbox {
        prefWidth = 600.0
        spacing = 5.0
        paddingAll = 5.0

        hbox {
            spacing = 5.0
            paddingAll = 5.0

            vbox {
                spacing = 5.0

                reindex = checkbox("Re-index files on radio") {
                    tooltip = tooltip("Re-calculate the MD5 checksums of all files on the internal SD-card of the radio. This process will take a long time.")
                }

                updateResources = checkbox("Update resource files") {
                    isSelected = true
                    tooltip = tooltip("Update auxilary resource files like help pages, voice files and utilities.")
                }

                vbox {
                    padding = insets(left = 20.0)

                    replaceHelpPages = checkbox("Replace custom help pages.") {
                        enableWhen { updateResources.selectedProperty() }
                        isSelected = true
                        tooltip = tooltip("Updates help pages with the latest online version. WARNING! This will replace all custom help pages.")
                    }

                    replaceVoiceFiles = checkbox("Replace custom voice files.") {
                        enableWhen { updateResources.selectedProperty() }
                        isSelected = true
                        tooltip = tooltip("Updates voice files with the latest online version. WARNING! This will replace all custom voice files.")
                    }
                }

                updateFirmware = checkbox("Update firmware") {
                    isSelected = true
                    tooltip = tooltip("Copies the latest firmware file to the internal SD-card. Installation has to be performed afterwards on the radio in the \"Info & Update\" menu via \"SD Card update\".")
                }
            }

            region {
                hgrow = Priority.SOMETIMES
            }

            vbox {
                hbox {
                    alignment = Pos.CENTER_RIGHT
                    spacing = 5.0
                    label("Drive:")
                    comboBox = combobox {
                        prefWidth = 275.0
                    }
                }
            }
        }

        label("Messages:")

        textArea = textarea {
            isEditable = false
            prefHeight = 200.0
            font = Font.font("Monospaced Regular", 12.0)
            vgrow = Priority.SOMETIMES
        }

        buttonbar {
            paddingAll = 5.0

            updateButton = button("Online Update") {
                disableWhen { comboBox.valueProperty().isNull }
                action {
                    textArea.text = ""

                    task = runAsync {
                        if (doReindex) {
                            mz32.scan(this)
                        }
                        mz32.update(this, doUpdateResources, doReplaceHelpPages, doReplaceVoiceFiles, doUpdateFirware)
                    }.apply {
                        disableWhen { runningProperty() }
                        messageProperty().addListener { _, _, newValue ->
                            if (textArea.text != "") textArea.appendText("\n")
                            textArea.appendText(newValue)
                        }
                    }
                }
            }
        }
    }

    init {
        setStageIcon(iconImage)
        title = "mz-32 Upgrader"

        comboBox.items.addAll(Mz32.find())
        comboBox.value = comboBox.items.firstOrNull()

        primaryStage.setOnCloseRequest {
            task?.cancel()
        }

        primaryStage.setOnShown { updateButton.requestFocus() }
    }
}