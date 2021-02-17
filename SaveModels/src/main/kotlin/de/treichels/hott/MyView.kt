package de.treichels.hott

import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.serial.SerialPort
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.paint.Color
import tornadofx.*
import java.time.LocalDate
import java.time.Period

class MyView : View() {

    private val cities = FXCollections.observableArrayList("Dallas", "New York", "Sacramento")
    private val selectedPort = SimpleStringProperty("COM?")
    private val portList = FXCollections.observableArrayList("COM?")

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

        center = tableview(persons) {
            column("ID", Person::idProperty)
            column("Name", Person::nameProperty)
            column("Birthday", Person::birthdayProperty)
            readonlyColumn("Age", Person::age).cellFormat {
                text = it.toString()
                style {
                    if (it < 18) {
                        backgroundColor += c("#8b0000")
                        textFill = Color.WHITE
                    }
                }
            }
        }

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
            portList.removeIf {portList -> portList == "COM?"}
        }

        selectedPort.onChange {
            println("Port changed to: $it" )

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
