/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.mdlviewer.javafx

import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.model.serial.JSSCSerialPort
import de.treichels.hott.util.ExceptionDialog
import javafx.beans.binding.BooleanBinding
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.ComboBox
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.text.Font
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import java.util.concurrent.Callable

/**
 * Abstract base class for modal dialogs that read some data from the transmitter.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
abstract class SelectFromTransmitter : View() {
    // controls
    private var portCombo by singleAssign<ComboBox<String>>()
    private var startButton by singleAssign<Button>()

    // asynchronous result - updated when the start button was pressed
    private var result: Callable<Model>? = null

    // the current serial port - updated when the com port changes
    protected var serialPort: HoTTSerialPort? = null

    // UI
    override val root = borderpane {
        top {
            hbox {
                padding = insets(5.0)
                alignment = Pos.CENTER_RIGHT

                label(messages["serial_port"]) {
                    font = Font.font(14.0)
                    padding = insets(right = 5.0)
                }

                portCombo = combobox {
                    setOnAction { portChanged() }
                }
            }
        }

        bottom {
            buttonbar {
                padding = insets(5.0)

                startButton = button(messages["load"], ButtonData.OK_DONE) {
                    action { start() }
                }

                button(messages["cancel"], ButtonData.CANCEL_CLOSE) {
                    action { close() }
                }
            }
        }
    }

    /**
     * Subclass contract: A [Task] that refreshes the UI when the com port changes
     */
    protected abstract fun refreshUITask(): Task<*>

    /**
     * Subclass contract: A [Callable] that computes the [Model] result of the dialog in background
     */
    protected abstract fun getResultCallable(): Callable<Model>?

    /**
     * Subclass contract: A [Boolean] that tells whether the user has selected something that cam be computed as [Model]
     */
    protected abstract fun isReady(): BooleanBinding

    /**
     * The com port was changed, update bindings.
     */
    private fun portChanged() {
        val name = portCombo.value
        if (name != null) {
            if (!serialPort?.portName.equals(name)) {
                preferences { put("comPort", name) }

                serialPort = HoTTSerialPort(JSSCSerialPort(name))
                refreshUITask().apply {
                    portCombo.disableWhen(runningProperty())
                    startButton.disableWhen(runningProperty().or(isReady().not()))
                }.fail {
                    ExceptionDialog.show(it)
                    close()
                }
            } else {
                startButton.disableWhen(isReady().not())
            }
        } else {
            startButton.disableWhen(portCombo.valueProperty().isNull.or(isReady().not()))
        }
    }

    /**
     * Start result computation in background and close dialog.
     */
    private fun start() {
        result = getResultCallable()
        close()
    }

    /**
     * Handle double click on an item. If a suitable item was selected close dialog as if the load button was pressed.
     */
    protected fun handleDoubleClick(evt: MouseEvent) {
        if (evt.button == MouseButton.PRIMARY && evt.clickCount == 2 && !startButton.isDisabled) start()
    }

    /**
     * Open a new dialog and return it's [Stage].
     */
    fun openDialog(): Callable<Model>? {
        result = null

        // disable start button - will be re-enabled in portChanged()
        startButton.disableProperty().unbind()
        startButton.isDisable = true

        // update port combo
        with(portCombo) {
            value = null
            items.clear()

            // (re-) load available com ports
            runAsync {
                JSSCSerialPort.availablePorts
            }.success { portNames ->
                items.addAll(portNames)
                preferences {
                    val prefPort: String? = get("comPort", null)
                    portCombo.value = if (prefPort != null && portCombo.items.contains(prefPort)) prefPort else null
                }
            }.fail {
                ExceptionDialog.show(it)
                close()
            }
        }

        // open model dialog
        openModal(stageStyle = StageStyle.UTILITY, modality = Modality.APPLICATION_MODAL, block = true)?.apply {
            setOnCloseRequest { close() }
        }

        return result
    }
}
