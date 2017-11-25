package de.treichels.hott.vdfeditor.ui.transmitter

import de.treichels.hott.HoTTSerialPort
import de.treichels.hott.vdfeditor.ui.ExceptionDialog
import de.treichels.hott.vdfeditor.ui.MessageDialog
import gde.model.serial.JSSCSerialPort
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.StageStyle
import tornadofx.*

class TransmitterDialogView : View() {
    // rf off confirmation dialog
    private val dialog = MessageDialog(AlertType.CONFIRMATION, messages["rf_off_header"], messages["rf_off_message"])

    private var task: TransmitterTask? = null
    private var bgTask: Task<Unit?>? = null
    private var serialPort: HoTTSerialPort? = null

    // controls
    private var portCombo: ComboBox<String> by singleAssign()
    private var progressBar: ProgressBar by singleAssign()
    private var message: Label by singleAssign()
    private var startButton: Button by singleAssign()

    override val root = vbox {
        spacing = 10.0
        padding = insets(10.0)

        // label and combobox
        hbox {
            spacing = 5.0
            alignment = Pos.CENTER_RIGHT

            label(messages["serial_port"])

            portCombo = combobox()
        }

        // progressbar with text
        anchorpane {
            prefHeight = 35.0
            prefWidth = 300.0

            progressBar = progressbar {
                progress = 0.5
                anchorpaneConstraints {
                    topAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }
            }

            message = label {
                alignment = Pos.CENTER
                textAlignment = TextAlignment.CENTER
                font = Font.font("System Bold", 12.0)
                anchorpaneConstraints {
                    topAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }
            }
        }

        // buttons
        hbox {
            spacing = 10.0
            alignment = Pos.CENTER_RIGHT

            startButton = button {
                text = messages["start_button"]
                action { onStart() }
            }

            button(messages["abort_button"]) {
                action { onAbort() }
            }
        }
    }

    private fun onStart() {
        preferences { put("comPort", portCombo.value) }
        dialog.showAndWait().ifPresent {
            when (it) {
                ButtonType.OK -> {
                    if (serialPort?.isOpen == true) serialPort?.close()
                    serialPort = HoTTSerialPort(JSSCSerialPort(portCombo.value))
                    task?.port = serialPort
                    bgTask = runAsync {
                        task?.run()
                    } success {
                        close()
                    } fail (this@TransmitterDialogView::onFail)
                }
                else -> onAbort()
            }
        }
    }

    private fun onAbort() {
        task?.cancel()
        bgTask?.cancel()
        close()
    }

    private fun onFail(t: Throwable) {
        ExceptionDialog.show(t)
        onAbort()
    }

    fun openDialog(task: TransmitterTask) {
        // cleanup
        serialPort = null
        portCombo.value = null
        portCombo.items.clear()

        // bind task properties
        this.task = task
        task.fail (this::onFail)
        portCombo.disableWhen(task.runningProperty())
        startButton.disableWhen(task.runningProperty().or(portCombo.valueProperty().isNull))
        progressBar.progressProperty().bind(task.progressProperty())
        message.textProperty().bind(task.messageProperty())

        // (re-) load available com ports
        runAsync {
            JSSCSerialPort.getAvailablePorts()
        } success { ports ->
            portCombo.items.addAll(ports)

            // restore last used com port
            preferences {
                val prefPort: String? = get("comPort", null)
                portCombo.value = if (prefPort != null && portCombo.items.contains(prefPort)) prefPort else null
            }
        } fail (this::onFail)

        // open model dialog
        openModal(stageStyle = StageStyle.UTILITY, modality = Modality.APPLICATION_MODAL)?.apply {
            titleProperty()?.bind(task.titleProperty())
            setOnCloseRequest { onAbort() }
        }
    }
}