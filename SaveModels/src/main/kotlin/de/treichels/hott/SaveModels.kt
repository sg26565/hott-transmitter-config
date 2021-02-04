package de.treichels.hott

import tornadofx.launch
import tornadofx.App
import tornadofx.*
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import java.time.LocalDate
import java.time.Period

class SaveModels : App(MyView::class)

fun main(args: Array<String>) {
    launch<SaveModels>(args)
}

// Example from https://github.com/edvin/tornadofx/wiki/Type-Safe-Builders
// https://edvin.gitbooks.io/tornadofx-guide/content/part1/1_Why_TornadoFX.html
// https://stackoverflow.com/questions/50976849/invoke-function-when-item-in-combobox-is-selected-tornadofx


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


class MyView : View() {
    override val root = VBox()

    private val texasCities = FXCollections.observableArrayList(
        "Austin",
        "Dallas", "Midland", "San Antonio", "Fort Worth"
    )

    private val selectedCity = SimpleStringProperty("Hugo")

    private val persons = FXCollections.observableArrayList(
        Person(1, "Samantha Stuart", LocalDate.of(1981, 12, 4)),
        Person(2, "Tom Marks", LocalDate.of(2011, 1, 23)),
        Person(3, "Stuart Gilles", LocalDate.of(1989, 5, 23)),
        Person(4, "Nicole Williams", LocalDate.of(1998, 8, 11))
    )

    init {
        with(root) {
            borderpane {
                top = hbox {
                    togglebutton {
                        val stateText = selectedProperty().stringBinding {
                            if (it == true) "ON" else "OFF"
                        }
                        textProperty().bind(stateText)
                        setOnAction {
                            println("Button 0 Pressed")
                        }
                    }
                    button("Button 1") {
                        setOnAction {
                            println("Button 1 Pressed")
                        }
                        hboxConstraints {
                            marginRight = 10.0
                            marginLeft = 10.0
                        }
                    }
                    button("Button 2").setOnAction {
                        println("Button 2 Pressed")
                    }
                    combobox(selectedCity, texasCities)
                    selectedCity.onChange {
                        println("City changed to: $it")
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
            }
        }
    }
}