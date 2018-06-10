package de.treichels.hott.mz32

import de.treichels.hott.util.ExceptionDialog
import javafx.beans.property.SimpleBooleanProperty
import javafx.concurrent.Task
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import org.apache.logging.log4j.jcl.LogFactoryImpl
import org.apache.logging.log4j.util.EnvironmentPropertySource
import org.apache.logging.log4j.util.SystemPropertiesPropertySource
import org.controlsfx.control.CheckComboBox
import tornadofx.*
import java.io.File
import java.util.jar.JarFile

@Suppress("unused")
private val ignored = listOf(LogFactoryImpl::class, EnvironmentPropertySource::class, SystemPropertiesPropertySource::class)

fun main(vararg args: String) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<Mz32DownloaderApp>(*args)
}

class Mz32DownloaderApp : App() {
    override val primaryView = Mz32Downloader::class
}

class Mz32Downloader : View() {
    // Controls
    private var options by singleAssign<VBox>()
    private var reindex by singleAssign<CheckBox>()
    private var updateResources by singleAssign<CheckBox>()
    private var replaceHelpPages by singleAssign<CheckBox>()
    private var language by singleAssign<CheckComboBox<Language>>()
    private var replaceVoiceFiles by singleAssign<CheckBox>()
    private var updateFirmware by singleAssign<CheckBox>()
    private var comboBox by singleAssign<ComboBox<Mz32>>()
    private var textArea by singleAssign<TextArea>()
    private var progressBar by singleAssign<ProgressBar>()
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
    private val doReplaceHelpPages
        get() = replaceHelpPages.isSelected
    private val doReplaceVoiceFiles
        get() = replaceVoiceFiles.isSelected
    private val doUpdateFirware
        get() = updateFirmware.isSelected
    private val mz32
        get() = comboBox.value
    private val selectedLanguages: List<Language>
        get() = language.checkModel.checkedItems

    private val version: String by lazy {
        val source = File(Mz32Downloader::class.java.protectionDomain.codeSource.location.toURI())
        if (source.name.endsWith(".jar") || source.name.endsWith(".exe"))
            JarFile(source).use { jarFile ->
                val attributes = jarFile.manifest.mainAttributes
                val version = attributes.getValue("Implementation-Version")
                val build = attributes.getValue("Implementation-Build")

                "v$version.$build"
            }
        else "Unknown"
    }

    // UI
    override val root = vbox {
        prefWidth = 600.0
        spacing = 5.0
        paddingAll = 5.0

        hbox {
            spacing = 5.0
            paddingAll = 5.0

            options = vbox {
                spacing = 10.0
                disableProperty().bind(disableUI)

                reindex = checkbox("Re-index files on radio") {
                    tooltip = tooltip("Re-calculate the MD5 checksums of all files on the internal SD-card of the radio. This process will take a long time.")
                }

                vbox {
                    updateResources = checkbox("Update resource files") {
                        isSelected = true
                        tooltip = tooltip("Update auxilary resource files like help pages, voice files and utilities.")

                    }

                    vbox {
                        enableWhen { updateResources.selectedProperty() }
                        padding = insets(left = 20.0)

                        replaceHelpPages = checkbox("Update help pages.") {
                            preferences { isSelected = get("replaceHelpPages", "true")!!.toBoolean() }
                            action { preferences { put("replaceHelpPages", isSelected.toString()) } }

                            tooltip = tooltip("Updates help pages with the latest online version. WARNING! This will replace all custom help pages.")
                        }

                        replaceVoiceFiles = checkbox("Update voice files.") {
                            preferences { isSelected = get("replaceVoiceFiles", "true")!!.toBoolean() }
                            action { preferences { put("replaceVoiceFiles", isSelected.toString()) } }

                            tooltip = tooltip("Updates voice files with the latest online version. WARNING! This will replace all custom voice files.")
                        }
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
                spacing = 10.0
                disableProperty().bind(disableUI)

                vbox {
                    label("Drive:")
                    comboBox = combobox {
                        prefWidth = 275.0
                        setOnMousePressed { load() }
                    }
                }

                vbox {
                    enableWhen { updateResources.selectedProperty() }

                    label("Select Languages:")
                    language = opcr(this, CheckComboBox()) {
                        items.addAll(Language.values())

                        preferences {
                            get("languages", "en").split(',').forEach { lang ->
                                checkModel.check(Language.valueOf(lang))
                            }
                        }

                        checkModel.checkedItems.onChange { selected ->
                            preferences { put("languages", selected.list.joinToString(",") { it.name }) }
                            layout()
                        }
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

        progressBar = progressbar {
            isVisible = false
            prefWidthProperty().bind(textArea.widthProperty())
        }

        buttonbar {
            updateButton = button("Download")
        }
    }

    private fun armTask() {
        updateButton.text = "Download"
        updateButton.action { startTask() }
        updateButton.disableWhen { comboBox.valueProperty().isNull }
        disableUI.value = false
    }

    private fun startTask() {
        task = runAsync {
            if (doReindex) {
                mz32.scan(this, selectedLanguages)
            }
            mz32.update(this, selectedLanguages, doUpdateResources, doReplaceHelpPages, doReplaceVoiceFiles, doUpdateFirware)
            runLater { armTask() }
        }.apply {
            updateButton.text = "Cancel"
            updateButton.action { stopTask() }
            disableUI.value = true
            progressBar.progressProperty().bind(progressProperty())
            messageProperty().addListener { _, _, newValue ->
                textArea.text = newValue
                textArea.scrollTop = Double.MAX_VALUE
            }
            root.layout()
        }
    }

    private fun stopTask() {
        updateButton.disableProperty().unbind()
        updateButton.isDisable = true
        task?.fail { armTask() }
        task?.success { armTask() }
        task?.cancel()
    }

    private fun ComboBox<Mz32>.load() {
        items.clear()
        items.addAll(Mz32.find())
        value = items.firstOrNull()
    }

    init {
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