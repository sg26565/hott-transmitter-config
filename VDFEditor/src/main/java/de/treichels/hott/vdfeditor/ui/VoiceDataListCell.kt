package de.treichels.hott.vdfeditor.ui

import de.treichels.hott.vdfeditor.actions.RenameAction
import de.treichels.hott.model.voice.VDFType
import de.treichels.hott.model.voice.VoiceData
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class VoiceDataListCell internal constructor(private val controller: MainView) : ListCell<VoiceData?>() {
    private var indexLabel by singleAssign<Label>()
    private var playButton by singleAssign<Button>()
    private var textField: TextField? = null
    private val hBox = hbox {
        alignment = Pos.CENTER_LEFT

        indexLabel = label {
            minWidth = 25.0
            alignment = Pos.CENTER_RIGHT
            style {
                fontWeight = FontWeight.BOLD
            }
        }
        playButton = button {
            action { item?.play() }
            style {
                backgroundColor += Color.TRANSPARENT
                graphic = VoiceDataListCell::class.java.getResource("play-button-icon_16.png").toURI()
            }
        }
    }

    init {
        // consume mouse events to prevent listView from processing them
        setOnMouseClicked { it.consume() }

        // disable dnd on empty cells to prevent IndexOutOfBoundsExcetions
        disableWhen(itemProperty().isNull)
    }

    override fun cancelEdit() {
        if (textField != null) {
            text = item?.name
            hBox.children.remove(textField)
            textField = null
        }
        super.cancelEdit()
    }

    override fun startEdit() {
        val cellText = text
        text = null

        textField = textfield {
            text = when (controller.voiceFile.vdfType) {
            // strip index number from portName
                VDFType.System -> cellText.substring(if (index > 98) 4 else 3)
                else -> cellText
            }

            action {
                textField?.apply {
                    controller.undoBuffer.push(RenameAction(index, text))
                }
                commitEdit(item)
            }

            hgrow = Priority.SOMETIMES
        }

        hBox.children.add(textField)
        super.startEdit()
    }

    public override fun updateItem(item: VoiceData?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty) {
            text = null
            graphic = null
        } else {
            indexLabel.text = Integer.toString(index + 1)
            text = item?.name
            graphic = hBox
        }
    }
}
