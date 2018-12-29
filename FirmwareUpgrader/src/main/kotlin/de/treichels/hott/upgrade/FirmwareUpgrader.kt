package de.treichels.hott.upgrade

import com.fazecast.jSerialComm.SerialPort
import de.treichels.hott.firmware.Firmware
import de.treichels.hott.firmware.getFirmware
import de.treichels.hott.model.enums.ReceiverType
import de.treichels.hott.ui.ExceptionDialog
import de.treichels.hott.ui.MessageDialog
import de.treichels.hott.util.Util
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File
import java.io.IOException
import java.util.*
import java.util.logging.LogManager

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
        const val LAST_FILE = "lastFile"

        private fun isFirmware(file: File) = file.exists() && file.isFile && file.canRead() && file.name.endsWith(".bin")
        private fun getFile(ev: DragEvent) = ev.dragboard.files.firstOrNull(::isFirmware)
    }

    // resources
    private val iconImage = resources.image("icon.png")

    // controls
    private var receiverType by singleAssign<ComboBox<ReceiverType>>()
    private var textField by singleAssign<TextField>()
    private var portCombo by singleAssign<ComboBox<String>>()
    private var textArea by singleAssign<TextArea>()
    private var progressBar by singleAssign<ProgressBar>()
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

            vbox {
                label(messages["receiverType"])
                hbox {
                    spacing = 5.0
                    receiverType = combobox(values = ReceiverType.values().asList()) {
                        disableWhen { service.runningProperty() }
                        buttonCell = ReceiverListCell()
                        setCellFactory { ReceiverListCell() }
                        minHeight = 48.0
                    }
                    button(messages["detectReceiver"]) {
                        disableWhen { service.runningProperty() }
                        minHeight = 48.0
                        disableWhen { serialPortProperty.isNull }
                        action { autoDetectReceiver() }
                    }
                }
            }
            region {
                hgrow = Priority.SOMETIMES
            }

            vbox {
                label(messages["port"])
                portCombo = combobox {
                    disableWhen { service.runningProperty() }
                    setOnAction {
                        val portName = value

                        if (portName != null) {
                            preferences { put(PREFERRED_PORT, portName) }
                            serialPort = SerialPort.getCommPort(portName)
                        }
                    }
                }
            }
        }

        hbox {
            vbox {
                hgrow = Priority.ALWAYS

                label(messages["file"])
                hbox {
                    alignment = Pos.CENTER_LEFT
                    spacing = 5.0

                    textField = textfield {
                        hgrow = Priority.ALWAYS
                        isEditable = false
                        promptText = messages["selectFile"]
                        prefWidth = 400.0
                        disableWhen { service.runningProperty().or(textProperty().isEqualTo(messages["selectFile"])) }
                        textProperty().addListener { _ -> fileNameChangeListener() }
                    }
                    button(messages["select"]) {
                        disableWhen { service.runningProperty() }
                        action { selectFile() }
                    }
                    button(messages["download"]) {
                        disableWhen { receiverType.valueProperty().isNull.or(service.runningProperty()) }
                        action { download() }
                    }
                }
            }
        }

        vbox {
            vgrow = Priority.ALWAYS

            label(messages["messages"])
            textArea = textarea {
                prefHeight = 100.0
                isEditable = false
                style = "-fx-font-family: monospace"
                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
                service.messageProperty().addListener { _, _, newValue -> appendText(newValue) }
            }
        }

        hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0

            startButton = button(messages["start"]) {
                disableWhen { serialPortProperty.isNull.or(textField.textProperty().isEmpty) }
                action { if (service.isRunning) doCancel() else doUpgrade() }
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
            }
        }
    }

    private fun autoDetectReceiver() {
        val dialog = Alert(Alert.AlertType.INFORMATION, messages["connectReceiver"], ButtonType.CANCEL).apply {
            headerText = messages["detectReceiver"]
            title = messages["receiverType"]
        }
        val task = runAsync {
            ReceiverFirmware.detectReceiver(this, serialPort!!)
        }.ui {
            receiverType.value = it
            dialog.close()
        }.fail {
            dialog.close()
        }

        dialog.showAndWait()
        if (task.isRunning) task.cancel()
    }

    private fun fileNameChangeListener() {
        val file = File(textField.text)
        if (file.exists()) {
            preferences { put(LAST_FILE, file.absolutePath) }

            try {
                // read receiver type from firmware file and update combobox
                receiverType.value = ReceiverFirmware.load(file).receiverType
            } catch (e: IOException) {
                // ignore invalid firmware file
            }
        }
    }

    private fun download() {
        root.runAsyncWithOverlay {
            receiverType.value.getFirmware()
        }.ui { firmware ->
            when (firmware.size) {
                // there is only one file available online - select it
                1 -> download(firmware[0])

                // no files found - do nothing
                0 -> {
                }

                // show choice dialog and let the use choose
                else -> {
                    val optional = ChoiceDialog(null, firmware.map { it.file.name }.toSet().sorted()).apply {
                        title = messages["downloadTitle"]
                        headerText = messages["downloadHeaderText"]
                    }.showAndWait()

                    // if there is a result, download the file and use it
                    if (optional.isPresent) download(firmware.find { it.file.name == optional.get() })
                }
            }

            textField.selectEnd()
        }
    }

    private fun download(firmware: Firmware<ReceiverType>?) {
        if (firmware != null)
            root.runAsyncWithOverlay {
                firmware.download()
            }.ui {
                textField.text = firmware.file.absolutePath
            }
    }

    private fun doCancel() {
        service.cancel()
    }

    private fun doUpgrade() {
        textArea.clear()
        progressBar.progressProperty().bind(service.progressProperty())
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
        startButton.text = messages["start"]
    }

    private fun selectFile() {
        FileChooser().apply {
            title = messages["selectTitle"]
            extensionFilters.add(FileChooser.ExtensionFilter(messages["firmwareFiles"], "*.bin"))

            if (textField.text.isNotEmpty()) {
                initialDirectory = File(textField.text).parentFile
            } else {
                preferences {
                    val folder = File(get(LAST_DIR, System.getProperty("user.home")))
                    if (folder.exists() && folder.isDirectory)
                        initialDirectory = folder
                }
            }

        }.showOpenDialog(primaryStage)?.apply {
            preferences { put(LAST_DIR, parentFile.absolutePath) }
            textField.text = absolutePath
            textField.selectEnd()
        }
    }

    init {
        Util.enableLogging()

        title = messages["title"] + " " + Util.sourceVersion(FirmwareUpgrader::class)
        setStageIcon(iconImage)
        currentStage?.apply {
            minHeight = 300.0
            minWidth = 400.0
        }

        // setup drag/drop
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

            setOnDragDropped { ev ->
                val file = getFile(ev)
                if (file != null) {
                    textField.text = file.absolutePath
                    textField.selectEnd()
                    preferences { put(LAST_DIR, file.parentFile.absolutePath) }
                }
                ev.consume()
            }
        }


        // load preferred port from preferences
        runAsync {
            portCombo.items.addAll(SerialPort.getCommPorts().map { it.systemPortName })
        } success {
            preferences {
                val prefPort: String? = get(PREFERRED_PORT, null)
                if (prefPort != null && portCombo.items.contains(prefPort)) {
                    portCombo.value = prefPort
                }

                get(LAST_FILE, null)?.apply {
                    File(this).apply {
                        if (exists()) textField.text = absolutePath
                    }
                }
            }
        }
    }
}

/**
 * A ListCell that show an image of the receiver and it's name
 */
class ReceiverListCell : ListCell<ReceiverType>() {
    private val images = HashMap<ReceiverType, ImageView>()

    init {
        // load images for all receivers
        ReceiverType.values().forEach { receiverType ->
            val stream = ReceiverListCell::class.java.getResourceAsStream("images/${receiverType.orderNo}.jpg")
                    ?: ReceiverListCell::class.java.getResourceAsStream("images/missing.png")

            // scale to 40x50 pixels
            images[receiverType] = ImageView(Image(stream, 50.0, 40.0, true, true))
        }
    }

    override fun updateItem(item: ReceiverType?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty || item == null) {
            graphic = null
            text = null
        } else {
            graphic = images[item]
            text = item.toString()

        }
    }
}
