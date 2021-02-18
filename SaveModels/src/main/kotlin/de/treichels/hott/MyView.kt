package de.treichels.hott

import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.messages.Messages
import de.treichels.hott.serial.SerialPort
import de.treichels.hott.serial.ModelInfo
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.paint.Color
import tornadofx.*
import java.time.LocalDate
import java.time.Period
import javax.swing.table.AbstractTableModel

class MyView : View() {

    private val cities = FXCollections.observableArrayList("Dallas", "New York", "Sacramento")
    private val dummyPort : String = "COM?"
    private val selectedPort = SimpleStringProperty(dummyPort)
    private val portList = FXCollections.observableArrayList(dummyPort)

    override val root = borderpane {
        top = form {
            fieldset {
                field("Select Port") {
                    combobox(selectedPort, portList)
                    button("Add").setOnAction {
                        println("Add Togo")
                        cities.add("Togo")
                    }
                    button("Remove Last").setOnAction {
                        println("Remove " + cities.elementAt(cities.size - 1))
                        cities.removeLast()
                    }
                }
            }
        }

//        center = tableview(ModelInfoTableModel) {
//            column(AbstractTableModel.)
//
//
//        }

        bottom = hbox {
            button("Button 1") {
                hboxConstraints {
                    marginRight = 20.0

                }
            }
            button("Button 2")
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

class Person(id: Int, name: String, birthday: LocalDate) {
    private var id: Int by property<Int>()
    fun idProperty() = getProperty(Person::id)

    private var name: String by property<String>()
    fun nameProperty() = getProperty(Person::name)

    private var birthday: LocalDate by property<LocalDate>()
    fun birthdayProperty() = getProperty(Person::birthday)

    val age: Int get() = Period.between(birthday, LocalDate.now()).years

    init {
        this.id = id
        this.name = name
        this.birthday = birthday
    }
}

private val persons = FXCollections.observableArrayList(
    Person(1, "Samantha Stuart", LocalDate.of(1981, 12, 4)),
    Person(2, "Tom Marks", LocalDate.of(2011, 1, 23)),
    Person(3, "Stuart Gilles", LocalDate.of(1989, 5, 23)),
    Person(4, "Nicole Williams", LocalDate.of(1998, 8, 11))
)

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
