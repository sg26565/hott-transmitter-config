package de.treichels.hott

import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.messages.Messages
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.serial.ModelInfo
import de.treichels.hott.serial.SerialPort
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Cursor
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.prefs.Preferences
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

fun main() {
    SwingUtilities.invokeLater { ExportModels().showDialog() }
}

/**
 * HoTT Transmitter Config
 * Copyright (C) 2013  Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class ExportModels {
    private val comboBox = JComboBox<String>()
    private val saveAllButton = JButton(Messages.getString("SaveAll"))
    private val panel = JPanel()
    private val frame = JFrame(Messages.getString("ExportModels.Title"))
    private val status = JLabel()
    private val tableModel = ModelInfoTableModel()
    private val models = JTable(tableModel)
    private val scrollPane = JScrollPane(models)
    private var port: HoTTTransmitter? = null

    private inner class ButtonCellRenderer : AbstractCellEditor(), TableCellEditor, TableCellRenderer {
        override fun getCellEditorValue(): Any? {
            return null
        }

        override fun getTableCellEditorComponent(table: JTable, value: Any, isSelected: Boolean, row: Int, column: Int): Component {
            val button = JButton(Messages.getString("Save"))
            button.addActionListener {
                try {
                    save(tableModel.getModelInfo(row))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return button
        }

        override fun getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int,
                                                   column: Int): Component {
            return getTableCellEditorComponent(table, value, isSelected, row, column)
        }
    }

    private class ModelInfoTableModel : AbstractTableModel() {
        var modelInfos: Array<ModelInfo>? = null
            private set

        override fun getColumnCount(): Int {
            return COLUMNS.size
        }

        override fun getColumnName(column: Int): String {
            return COLUMNS[column]
        }

        fun getModelInfo(row: Int): ModelInfo {
            return modelInfos!![row]
        }

        override fun getRowCount(): Int {
            return if (modelInfos == null) 0 else modelInfos!!.size
        }

        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
            val (_, modelName, modelInfo, modelType, _, receiverClass, usedHours, usedMinutes) = modelInfos!![rowIndex]

            when (columnIndex) {
                0 -> return rowIndex + 1

                1 -> return modelType

                2 -> return modelName

                3 -> return modelInfo

                4 -> return receiverClass

                5 -> return if (usedHours == 0 && usedMinutes == 0) {
                    "-:-"
                } else {
                    String.format("%02d:%02d", usedHours, usedMinutes)
                }

                else -> return null
            }
        }

        override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
            return columnIndex == 6
        }

        fun setModelInfos(modelInfos: List<ModelInfo>) {
            this.modelInfos = modelInfos.toTypedArray()
            fireTableDataChanged()
        }

        companion object {
            private val COLUMNS = arrayOf(Messages.getString("ModelNumber"), Messages.getString("ModelType"), Messages.getString("ModelName"), Messages.getString("ModelInfo"), Messages.getString("ReceiverType"), Messages.getString("Usage"), Messages.getString("Actions"))
        }
    }

    private inner class PortSelectionListener : ActionListener {
        override fun actionPerformed(arg0: ActionEvent?) {
            val portName = comboBox.selectedItem as String
            if (portName.isNotEmpty()) {
                updateTableData(portName)
            }
        }
    }

    private fun save(info: ModelInfo) {
        val extension = "mdl"
        val description = Messages.getString("MdlFileDescription")
        val fc = JFileChooser()
        fc.fileSelectionMode = JFileChooser.FILES_ONLY
        fc.isMultiSelectionEnabled = false
        fc.isAcceptAllFileFilterUsed = false
        val dir = File(PREFS.get(LAST_SAVE_DIR, System.getProperty("user.dir")))
        fc.currentDirectory = dir
        fc.fileFilter = FileNameExtensionFilter(description, extension)
        fc.selectedFile = File(dir, String.format("%c%s.mdl", info.modelType.char, info.modelName))

        val result = fc.showSaveDialog(frame)

        if (result == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            PREFS.put(LAST_SAVE_DIR, file.parentFile.absolutePath)

            FileOutputStream(file).use { os -> os.write(port!!.getModelData(info)) }
        }
    }

    private fun saveAll() {
        val fc = JFileChooser()
        fc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        fc.isMultiSelectionEnabled = false
        val dir = File(PREFS.get(LAST_SAVE_DIR, System.getProperty("user.dir")))
        fc.currentDirectory = dir

        val result = fc.showSaveDialog(frame)

        if (result == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile
            PREFS.put(LAST_SAVE_DIR, file.absolutePath)

            frame.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
            frame.isEnabled = false
            val monitor = ProgressMonitor(frame, Messages.getString("ExportModels.SavingModels"), "", 0, tableModel.rowCount)
            monitor.millisToDecideToPopup = 30
            monitor.millisToPopup = 0

            object : SwingWorker<Void, Void>() {
                @Throws(Exception::class)
                override fun doInBackground(): Void? {
                    var progress = 0

                    for (info in tableModel.modelInfos!!) {
                        if (monitor.isCanceled) {
                            break
                        }

                        progress++
                        monitor.setProgress(progress)
                        monitor.note = info.modelName

                        if (info.modelType === ModelType.Unknown) {
                            continue
                        }

                        val type = info.modelType.char
                        var name = info.modelName
                        if (name.isEmpty()) {
                            name = String.format("Model%02d", info.modelNumber)
                        }

                        val fileName = String.format("%c%s.mdl", type, name)
                        FileOutputStream(File(file, fileName)).use { os -> os.write(port!!.getModelData(info)) }
                    }

                    return null
                }

                override fun done() {
                    monitor.close()
                    frame.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
                    frame.isEnabled = true
                }
            }.execute()
        }
    }

    internal fun showDialog() {
        for (s in SerialPort.getAvailablePorts()) {
            comboBox.addItem(s)
        }

        val listener = PortSelectionListener()
        comboBox.addActionListener(listener)
        listener.actionPerformed(null)

        panel.add(JLabel(Messages.getString("SerialPort")))
        panel.add(comboBox)
        panel.add(saveAllButton)

        saveAllButton.addActionListener { saveAll() }

        frame.layout = BorderLayout()
        frame.add(panel, BorderLayout.NORTH)
        frame.add(status, BorderLayout.SOUTH)
        frame.add(scrollPane, BorderLayout.CENTER)
        frame.pack()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
        frame.setSize(800, 600)

        val actions = models.getColumn(models.getColumnName(6))

        val renderer = ButtonCellRenderer()
        actions.cellRenderer = renderer
        actions.cellEditor = renderer
    }

    private fun updateTableData(portName: String) {
        port = HoTTTransmitter(SerialPort.getPort(portName))
        tableModel.setModelInfos(port!!.allModelInfos)
    }

    companion object {
        private const val LAST_SAVE_DIR = "lastSaveDir"
        private val PREFS = Preferences.userNodeForPackage(ExportModels::class.java)
    }
}
