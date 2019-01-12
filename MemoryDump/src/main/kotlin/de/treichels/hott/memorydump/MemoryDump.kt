/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.memorydump

import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.serial.ResponseCode.*
import de.treichels.hott.serial.SerialPort
import de.treichels.hott.ui.ExceptionDialog
import de.treichels.hott.util.Util
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.FileChooser
import tornadofx.*
import java.io.FileWriter

fun main(vararg args: String) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<MemoryDumpApp>(*args)
}

class MemoryDumpApp : App() {
    override val primaryView = MemoryDumpView::class
}

class MemoryDumpView : View() {
    companion object {
        private const val prefPort = "PREF_PORT"
        private const val blockSize = 0x800
    }

    private var comboBox by singleAssign<ComboBox<String>>()

    private val service = object : Service<String>() {
        override fun createTask() = object : Task<String>() {
            override fun call(): String {
                val text = StringBuilder()
                val message = StringBuilder()
                val portName: String? = comboBox.value

                if (portName?.isEmpty() == false) {
                    message.append(messages["startMessage"]).append('\n')

                    HoTTSerialPort(SerialPort.getPort(portName)).use { port ->
                        port.open()
                        port.turnRfOutOff()

                        var i = 0
                        loop@ while (!isCancelled) {
                            val address = blockSize * i
                            if (i % 128 == 0) updateMessage("\n")

                            val (responseCode, data) = port.readMemoryBlock(address, blockSize)

                            when (responseCode) {
                                ACK -> {
                                    text.append(Util.dumpData(data, address))
                                    updateValue(text.toString())
                                    message.append(".")
                                    i++
                                }

                                BUSY -> {
                                    message.append("B")
                                    Thread.sleep(1000)
                                }

                                CRC_ERROR -> message.append("C")

                                ERROR -> message.append("E")

                                NACK -> {
                                    message.append("N\n${messages["endMessage"]}")
                                    break@loop
                                }

                                Unknown -> message.append("U")
                            }

                            updateMessage(message.toString())
                        }
                    }
                }

                if (isCancelled) message.append('\n').append(messages["abortMessage"])

                return text.toString()
            }
        }
    }

    override val root = borderpane {
        top {
            hbox {
                alignment = Pos.CENTER_RIGHT
                spacing = 5.0
                label(messages["port"])
                comboBox = combobox {
                    disableWhen { service.runningProperty() }
                    setOnAction {
                        preferences { put(prefPort, value) }
                    }
                }

                button(messages["start"]) {
                    disableWhen { service.runningProperty().or(comboBox.valueProperty().isNull) }
                    action {
                        service.restart()
                    }
                }

                button(messages["cancel"]) {
                    enableWhen { service.runningProperty() }
                    action {
                        service.cancel()
                    }
                }

                button(messages["save"]) {
                    disableWhen { service.runningProperty().or(service.valueProperty().isNull) }
                    action {
                        FileChooser().apply {
                            title = messages["saveTitle"]
                            extensionFilters.add(FileChooser.ExtensionFilter(messages["textFile"], "*.txt"))
                        }.showSaveDialog(primaryStage)?.apply {
                            FileWriter(this).use {
                                it.write(service.valueProperty().get())
                            }
                        }
                    }
                }
            }
        }

        center {
            textarea(service.valueProperty()) {
                isEditable = false
                font = Font.font("monospaced", 14.0)
                hgrow = Priority.ALWAYS

                service.valueProperty().onChange { v ->
                    if (service.runningProperty().get())
                        positionCaret(v?.length ?: 0)
                }
            }
        }

        bottom {
            textarea(service.messageProperty()) {
                isEditable = false
                font = Font.font("monospaced", 10.0)
                prefRowCount = 6
            }
        }
    }

    init {
        title = messages["title"]

        runAsync {
            comboBox.items.addAll(SerialPort.getAvailablePorts())
        }.success {
            preferences {
                val portName: String? = get(prefPort, null)
                if (comboBox.items.contains(portName))
                    comboBox.value = portName
            }
        }
    }
}
