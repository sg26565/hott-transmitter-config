package de.treichels.hott

import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.messages.Messages
import de.treichels.hott.serial.SerialPort
import de.treichels.hott.serial.ModelInfo
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.TableCell
import tornadofx.*
import javax.swing.table.AbstractTableModel

class MyView : View() {

    private val dummyPort : String = "COM?"
    private val selectedPort = SimpleStringProperty(dummyPort)
    private val portList = FXCollections.observableArrayList(dummyPort)

    private val warriorModel : WarriorModel by inject()
    private val persons = FXCollections.observableArrayList(
        Warrior(1,"Tyrion Lannister", "M"),
        Warrior(2,"Ned Stark", "M"),
        Warrior(3,"Daenerys Targaryen", "F"),
        Warrior(4,"Arya Stark", "F")
    )

    override val root = borderpane {
        top {
            form {
            fieldset {
                field("Select Port") {
                    combobox(selectedPort, portList)
                    button("Add").setOnAction {
                        println("Add Togo")

                    }
                    button("Remove Last").setOnAction {
                        println("Remove ")
                    }
                }
            }
        }
        }

        center {
            tableview(persons) {
                isEditable = true
                column("ID", Warrior::idProperty)
                column("Name", Warrior::nameProperty).makeEditable()
                column("Gender", Warrior::genderProperty).useComboBox(FXCollections.observableArrayList("M", "F"))
                column("Action", Warrior::dummyProperty).setCellFactory {DeleteButton<Warrior>()}
                column("Action", Warrior::dummyProperty).setCellFactory {AddButton<Warrior>()}
                bindSelected(warriorModel)
                subscribe<DeleteEvent> { event ->
                    items.removeAt(event.index)
                }
                subscribe<AddEvent> {
                    items.add(Warrior(persons.size+1,"hugo","F"))
                }
            }
        }

        bottom {
            hbox {
                button("Button 1") {
                    hboxConstraints {
                        marginRight = 20.0

                    }
                }
                button("Button 2")
            }
        }
    }

    init {
        for ( s in SerialPort.getAvailablePorts()) {
            println("port: $s")
            portList.add(s)
            portList.removeIf {portList -> portList == dummyPort}
        }

        selectedPort.onChange {
            println("Port changed to: $it" )
            updateTableData(it.toString())

            for (i in 0 until tableModel.rowCount){
                val line = tableModel.getModelInfo(i)
                println("$line")
            }
        }
    }
}

private var port: HoTTTransmitter? = null
private val tableModel =  ModelInfoTableModel()

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

private fun updateTableData(portName: String) {
    port = HoTTTransmitter(SerialPort.getPort(portName))
    tableModel.setModelInfos(port!!.allModelInfos)
}

class DeleteButton<Warrior> : TableCell<Warrior, String?>() {
    private val btn = Button("Delete")
    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            graphic = null
            text = null
        } else {
            btn.setOnAction {
                //tableView.items.removeAt(index)
                FX.eventbus.fire(DeleteEvent(index))
            }
            graphic = btn
            text = null
        }
    }
}
class AddButton<Warrior> : TableCell<Warrior, String?>(){
    private val btn = Button("Add")
    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty){
            graphic = null
            text = null
        } else{
            btn.setOnAction {
                FX.eventbus.fire(AddEvent(index))
            }
            graphic = btn
            text = null
        }
    }
}
class Warrior(id: Int, name: String, gender: String) {

    val idProperty = SimpleIntegerProperty(id)
    val nameProperty = SimpleStringProperty(name)
    val genderProperty = SimpleStringProperty(gender)
    val dummyProperty = SimpleStringProperty("")
}
class WarriorModel : ItemViewModel<Warrior>()
class AddEvent(val index:Int) : FXEvent()
class DeleteEvent(val index: Int) : FXEvent()