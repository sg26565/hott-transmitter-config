import tornadofx.*

fun main(args: Array<String>) = launch<PersonEditorApp>(args)

class Person(name: String, title: String) {
    val nameProperty = name.toProperty()
    var name: String by nameProperty

    val titleProperty = title.toProperty()
    var title: String by titleProperty
}

private val persons = mutableListOf(Person("John", "Manager"), Person("Jay", "Worker bee")).asObservable()

class PersonModel : ItemViewModel<Person>() {
    val name = bind(Person::nameProperty)
    val title = bind(Person::titleProperty)
}

class PersonEditorApp : App() {
    override val primaryView = Top::class
}

class Top : View() {
    private val list: PersonList by inject()
    private val editor: PersonEditor by inject()

    override val root = borderpane {
        center = list.root
        right = editor.root
    }
}

class PersonList : View("Person List") {
    private val model: PersonModel by inject()

    override val root = tableview(persons) {
        column("Name", Person::nameProperty)
        column("Title", Person::titleProperty)

        bindSelected(model)
    }
}

class PersonEditor : View("Person Editor") {
    private val model: PersonModel by inject()

    override val root = form {
        fieldset("Edit person") {
            //enableWhen(model.empty.not())
            field("Name") {
                textfield(model.name).required()
            }
            field("Title") {
                textfield(model.title).required()
            }
            button("Save") {
                enableWhen(model.valid.and(model.dirty))
                action {
                    save()
                }
            }
            button("Reset") {
                enableWhen(model.dirty)
                action {
                    model.rollback()
                }
            }
        }
    }

    private fun save() {
        // Flush changes from the text fields into the model
        model.commit()

        // The edited person is contained in the model
        val person = model.item

        // A real application would persist the person here
        println("Saving ${person.name} / ${person.title}")
    }
}
