package de.treichels.hott.vdfeditor.ui;

import de.treichels.hott.vdfeditor.actions.RenameAction;
import gde.model.voice.VDFType;
import gde.model.voice.VoiceData;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class VoiceDataListCell extends ListCell<VoiceData> {
    private static final String STYLESHEET_LOCATION = VoiceDataListCell.class.getResource("style.css").toString();

    private final Controller controller;
    private final Label indexLabel = new Label();
    private final Button playButton = new Button();
    private TextField textField = null;
    private final HBox hBox = new HBox(indexLabel, playButton);

    VoiceDataListCell(final Controller controller) {
        this.controller = controller;

        getStylesheets().add(STYLESHEET_LOCATION);

        // consume mouse events to prevent listView from processing them
        setOnMouseClicked(Event::consume);

        playButton.setOnAction(ev -> getItem().play());

        indexLabel.setMinWidth(25);
        indexLabel.setAlignment(Pos.CENTER_RIGHT);
        hBox.setAlignment(Pos.CENTER_LEFT);

        // disable dnd on empty cells to prevent IndexOutOfBoundsExcetions
        disableProperty().bind(itemProperty().isNull());
    }

    @Override
    public void cancelEdit() {
        if (textField != null) {
            setText(getItem().getName());
            hBox.getChildren().remove(textField);
            textField = null;
        }
        super.cancelEdit();
    }

    @Override
    public void startEdit() {
        if (controller.voiceFile.getVdfType() == VDFType.System)
            // strip index number from name
            textField = new TextField(getText().substring(getIndex() > 98 ? 4 : 3));
        else
            textField = new TextField(getText());

        textField.setOnAction(ev -> {
            final String newName = textField.getText();
            controller.undoBuffer.push(new RenameAction(getIndex(), newName));
            commitEdit(getItem());
        });
        setText(null);
        hBox.getChildren().add(textField);
        HBox.setHgrow(textField, Priority.SOMETIMES);
        super.startEdit();
    }

    @Override
    public void updateItem(final VoiceData item, final boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            indexLabel.setText(Integer.toString(getIndex() + 1));
            setText(item.getName());
            setGraphic(hBox);
        }
    }
}
