package de.treichels.hott.vdfeditor.ui

import de.treichels.hott.HoTTSerialPort
import gde.model.serial.JSSCSerialPort
import gde.model.voice.VoiceFile
import javafx.beans.property.ObjectProperty
import javafx.concurrent.WorkerStateEvent
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.StageStyle
import tornadofx.*
import java.util.*

class DialogController(private val serialPort: ObjectProperty<HoTTSerialPort?>) : View() {
    companion object {
        private val RES = ResourceBundle.getBundle(DialogController::class.java.name)
        private val dialog = MessageDialog(AlertType.CONFIRMATION, RES.getString("rf_off_header"), RES.getString("rf_off_message"))
    }

    private var portImpl by serialPort
    private var portCombo: ComboBox<String> by singleAssign()
    private var progressBar: ProgressBar by singleAssign()
    private var message: Label by singleAssign()
    private var task: TransmitterTask? = null
    private var thread: Thread? = null

    override val root = vbox {
        spacing = 10.0
        padding = insets(10.0)

        hbox {
            spacing = 5.0
            alignment = Pos.CENTER_RIGHT
            label("%serial_port")
            portCombo = combobox {
                onAction = onPortChanged
            }
        }

        anchorpane {
            prefHeight = 35.0
            prefWidth = 300.0

            progressBar = progressbar {
                progress = 0.5

            }
            message = label {
                alignment = Pos.CENTER
                textAlignment = TextAlignment.CENTER
                font = Font.font("System Bold", 12.0)
            }
        }

        hbox {
            alignment = Pos.CENTER_RIGHT
            button {
                text = "%abort_button"
                action {
                    task?.cancel()
                }
            }
        }
    }

    val result: VoiceFile?
        get() = task?.value

    private val closeDialog = EventHandler<WorkerStateEvent> { ev ->
        ev.consume()

        if (ev is  WorkerStateEvent) {
            val t =ev.source?.exception
            if (t != null) ExceptionDialog.show(t)
        }

        thread?.interrupt()
        primaryStage.hide()
    }


    private val onPortChanged = EventHandler<ActionEvent> {
        val portName = portCombo.value

        if (portName != null) {
            if (portImpl != null && portImpl!!.isOpen) portImpl?.close()
            portImpl = HoTTSerialPort(JSSCSerialPort(portName))

            dialog.showAndWait().ifPresent { b: ButtonType ->
                if (b != ButtonType.OK)
                    closeDialog
                else {
                    task?.port = serialPort.get()
                    thread = Thread(task)
                    thread?.start()
                }
            }
        }
    }

    fun openDialog(task: TransmitterTask) {
        this.task = task

        task.onSucceeded = closeDialog
        task.onCancelled = closeDialog
        task.onFailed = closeDialog

        portImpl = null
        portCombo.value = null
        portCombo.items.clear()
        portCombo.items.addAll(JSSCSerialPort.getAvailablePorts())
        portCombo.disableProperty().bind(task.runningProperty())

        progressBar.progressProperty().bind(task.progressProperty())
        message.textProperty().bind(task.messageProperty())

        val stage= openModal(stageStyle = StageStyle.UTILITY, modality = Modality.APPLICATION_MODAL)
        stage?.titleProperty()?.bind(task.titleProperty())
        stage?.onCloseRequest = EventHandler { ev ->
            ev.consume()
            task.cancel()
        }
    }
}