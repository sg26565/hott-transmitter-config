package de.treichels.hott.upgrade

import de.treichels.hott.util.ExceptionDialog
import de.treichels.hott.util.MessageDialog
import de.treichels.hott.serial.SerialPort
import de.treichels.hott.serial.SerialPortBase
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

fun main(vararg args: String) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<FirmwareUpgraderApp>(*args)
}

class FirmwareUpgraderApp : App() {
    override val primaryView = FirmwareUpgrader::class
}

class FirmwareUpgrader : View() {
    companion object {
        const val PREFERRED_PORT = "preferredPort"
        const val LAST_DIR = "lastDir"

        private fun isFirmware(file: File) = file.exists() && file.isFile && file.canRead() && file.name.endsWith(".bin")
        private fun getFile(ev: DragEvent) = ev.dragboard.files.firstOrNull(::isFirmware)
    }

    // resources
    private val iconImage = resources.image("icon.png")

    // controls
    private var textField by singleAssign<TextField>()
    private var portCombo by singleAssign<ComboBox<String>>()
    private var progressBar by singleAssign<ProgressBar>()
    private var progressLabel by singleAssign<Label>()
    private var startButton by singleAssign<Button>()

    // properties
    private val serialPortProperty = SimpleObjectProperty<SerialPort?>(null)
    private var serialPort by serialPortProperty

    // background activity
    private val service = FirmwareUpgradeService()

    // UI
    override val root = vbox {
        spacing = 10.0
        paddingAll = 5.0

        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0

            label(messages["file"])
            textField = textfield(messages["selectFile"]) {
                hgrow = Priority.ALWAYS
                disableWhen { service.runningProperty().or(textProperty().isEqualTo(messages["selectFile"])) }
            }
            button(messages["select"]) {
                disableWhen { service.runningProperty() }
                action { selectFile() }
            }

            region {
                hgrow = Priority.SOMETIMES
            }

            label(messages["port"])
            portCombo = combobox {
                disableWhen { service.runningProperty() }
                setOnAction {
                    val portName = value

                    if (portName != null) {
                        preferences { put(PREFERRED_PORT, portName) }
                        serialPort = SerialPortBase.getPort(portName)
                    }
                }
            }
        }

        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0

            startButton = button(messages["start"]) {
                disableWhen {
                    serialPortProperty.isNull
                            .or(textField.textProperty().isEmpty)
                            .or(textField.textProperty().isEqualTo(messages["selectFile"]))
                }
                action {
                    if (service.isRunning)
                        doCancel()
                    else
                        doUpgrade()
                }
            }

            anchorpane {
                hgrow = Priority.ALWAYS

                progressBar = progressbar {
                    progress = 0.0
                    anchorpaneConstraints {
                        topAnchor = 0.0
                        leftAnchor = 0.0
                        rightAnchor = 0.0
                        bottomAnchor = 0.0
                    }
                }

                progressLabel = label {
                    alignment = Pos.CENTER
                    anchorpaneConstraints {
                        topAnchor = 0.0
                        leftAnchor = 0.0
                        rightAnchor = 0.0
                        bottomAnchor = 0.0
                    }
                }
            }
        }
    }

    private fun doCancel() {
        service.cancel()
    }

    private fun doUpgrade() {
        progressBar.progressProperty().bind(service.progressProperty())
        progressLabel.textProperty().bind(service.messageProperty())

        startButton.text = messages["cancel"]

        service.setOnFailed { ev ->
            ExceptionDialog.show(ev.source.exception)
            reset()
        }

        service.setOnCancelled {
            MessageDialog.show(Alert.AlertType.WARNING, messages["cancelled"], messages["upgradeCancelled"])
            reset()
        }

        service.setOnSucceeded {
            MessageDialog.show(Alert.AlertType.INFORMATION, messages["success"], messages["upgradeFinished"])
            reset()
        }

        service.fileName = textField.text
        service.serialPort = serialPort!!
        service.start()
    }

    private fun reset() {
        service.reset()
        progressBar.progressProperty().unbind()
        progressBar.progress = 0.0
        progressLabel.textProperty().unbind()
        progressLabel.text = null
        startButton.text = messages["start"]
    }

    private fun selectFile() {
        FileChooser().apply {
            title = messages["selectTitle"]
            extensionFilters.add(FileChooser.ExtensionFilter(messages["firmwareFiles"], "*.bin"))

            preferences {
                val folder = File(get(LAST_DIR, System.getProperty("user.home")))
                if (folder.exists() && folder.isDirectory)
                    initialDirectory = folder
            }
        }.showOpenDialog(primaryStage)?.apply {
            preferences { put(LAST_DIR, parentFile.absolutePath) }
            textField.text = absolutePath
        }
    }

    init {
        setStageIcon(iconImage)
        title = messages["title"]

        with(root) {
            setOnDragEntered { ev ->
                opacity = 0.3
                ev.consume()
            }

            setOnDragExited { ev ->
                opacity = 1.0
                ev.consume()
            }

            setOnDragOver { ev ->
                if (getFile(ev) != null) ev.acceptTransferModes(TransferMode.COPY)
                ev.consume()
            }

            setOnDragDropped {ev->
                val file = getFile(ev)
                if (file != null) {
                    textField.text = file.absolutePath
                    preferences { put(LAST_DIR, file.parentFile.absolutePath) }
                }
                ev.consume()
            }
        }


        // load preferred port from preferences
        runAsync {
            portCombo.items.addAll(SerialPortBase.getAvailablePorts())
        } success {
            preferences {
                val prefPort: String? = get(PREFERRED_PORT, null)
                if (prefPort != null && portCombo.items.contains(prefPort)) {
                    portCombo.value = prefPort
                }
            }
        }
    }
}