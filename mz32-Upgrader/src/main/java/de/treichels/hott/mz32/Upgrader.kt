package de.treichels.hott.mz32

import de.treichels.hott.util.ExceptionDialog
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import org.apache.logging.log4j.jcl.LogFactoryImpl
import org.apache.logging.log4j.util.EnvironmentPropertySource
import org.apache.logging.log4j.util.SystemPropertiesPropertySource
import tornadofx.*

private val ignored = listOf( LogFactoryImpl::class, EnvironmentPropertySource::class, SystemPropertiesPropertySource::class )

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
    private var updateFirmware by singleAssign<CheckBox>()
    private var comboBox by singleAssign<ComboBox<Mz32>>()
    private var textArea by singleAssign<TextArea>()

    // Background task
    private var task: Task<Unit>? = null

    // resources
    private val iconImage = resources.image("icon.png")

    // Helper
    private val doReindex
        get() = reindex.isSelected
    private val doUpdateResources
        get() = updateResources.isSelected
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

                reindex = checkbox("Re-index files on radio")
                updateResources = checkbox("Update resource files") {
                    isSelected = true
                }
                updateFirmware = checkbox("Update firmware") {
                    isSelected = true
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
            prefHeight = 200.0
            font = Font.font("Monospaced Regular", 12.0)
            vgrow = Priority.SOMETIMES
        }

        buttonbar {
            paddingAll = 5.0

            button("Online Update") {
                disableWhen { comboBox.valueProperty().isNull }
                action {
                    textArea.text = ""

                    task = runAsync {
                        if (doReindex) mz32.md5.scan(this)
                        mz32.update(this, doUpdateResources, doUpdateFirware)
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
    }
}