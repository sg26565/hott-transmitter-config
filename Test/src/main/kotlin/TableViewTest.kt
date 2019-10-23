import de.treichels.hott.tts.Text2SpeechService
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.stage.Screen
import tornadofx.*

class Announcement(number: Int, directory: String, fileName: String, text: String, type: String = "Text") {
    val numberProperty = SimpleIntegerProperty(number)
    var number by numberProperty

    val directoryProperty = SimpleStringProperty(directory)
    var directory: String by directoryProperty

    val fileNameProperty = SimpleStringProperty(fileName)
    var fileName: String by fileNameProperty

    val textProperty = SimpleStringProperty(text)
    var text: String by textProperty

    val typeProperty = SimpleStringProperty(type)
    var type: String by typeProperty
}

class TableViewTest : View("TableView with Map") {
    private val types = listOf("Text", "Sound", "SSML").asObservable()

    private val map = resources.stream("Deutsch.csv").reader().readLines()
            .map { it.split(';').map { it.trim() } }
            .groupBy({ it[0] }, { Announcement(number = 0, directory = it[0], fileName = it[1], text = it[2]) })

    private val service = Text2SpeechService()

    override val root = accordion {
        map.keys.map {
            panes.add(titledpane(it) {
                isExpanded = false
                val items = map[it]
                if (items != null) {
                    tableview(items.toList().asObservable()) {
                        prefWidth = 800.0
                        smartResize()

                        column("Number", Announcement::numberProperty)
                                .contentWidth(useAsMin = true, useAsMax = true)
                                .cellFormat {
                                    text = String.format("%03d", it)
                                    style {
                                        alignment = Pos.TOP_RIGHT
                                        fontWeight = FontWeight.BOLD
                                    }
                                }

                        column("File Name", Announcement::fileNameProperty)
                                .makeEditable()
                                .contentWidth(useAsMin = true, useAsMax = true)

                        column("Type", Announcement::typeProperty)
                                .makeEditable()
                                .useChoiceBox(types)
                                .contentWidth(useAsMin = true, useAsMax = true)

                        column<Announcement, String>("Play") { ReadOnlyStringWrapper("") }
                                .contentWidth(useAsMin = true, useAsMax = true)
                                .cellFormat {
                                    graphic = button {
                                        style {
                                            backgroundColor += Color.TRANSPARENT
                                            graphic = resources.url("play-button-icon_16.png").toURI()
                                        }

                                        action {
                                            val announcement = items[index]
                                            println("${announcement.directory}/${announcement.number}_${announcement.fileName}.wav: ${announcement.text}")
                                        }
                                    }
                                }

                        column("Text", Announcement::textProperty)
                                .makeEditable()
                                .remainingWidth()
                    }
                }
            })
        }
    }

    init {
        val bounds = Screen.getPrimary().visualBounds
        primaryStage.y = bounds.minY
        primaryStage.height = bounds.height

        map.keys.forEach {
            map[it]?.forEachIndexed { index, announcement -> announcement.number = index + 1 }
        }
    }
}
