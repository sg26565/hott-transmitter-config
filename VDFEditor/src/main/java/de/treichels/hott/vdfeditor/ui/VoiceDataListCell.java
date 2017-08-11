package de.treichels.hott.vdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gde.model.voice.VoiceData;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class VoiceDataListCell extends ListCell<VoiceData> {
    private static final String STYLESHEET_LOCATION = VoiceDataListCell.class.getResource("style.css").toString();
    private static final List<File> TEMP_FILE_LIST = new ArrayList<>();
    static final DataFormat DnD_DATA_FORMAT = new DataFormat(VoiceDataListCell.class.getName());

    private final Label indexLabel = new Label();
    private final Button playButton = new Button();
    private TextField textField = null;
    private final HBox hBox = new HBox(indexLabel, playButton);

    @SuppressWarnings("unchecked")
    public VoiceDataListCell() {
        getStylesheets().add(STYLESHEET_LOCATION);

        // consume mouse events to prevent listView from processing them
        setOnMouseClicked(Event::consume);

        playButton.setOnAction(ev -> getItem().play());

        indexLabel.setMinWidth(25);
        indexLabel.setAlignment(Pos.CENTER_RIGHT);
        hBox.setAlignment(Pos.CENTER_LEFT);

        // disable dnd on empty cells to prevent IndexOutOfBoundsExcetions
        disableProperty().bind(itemProperty().isNull());

        // Start a drag operation
        setOnDragDetected(ev -> {
            deleteTempFiles();

            // generate temporary .wav files for each selected item for DnD to desktop (export of .wav file)
            final ObservableList<VoiceData> selectedItems = getListView().getSelectionModel().getSelectedItems();

            for (final VoiceData vd : selectedItems)
                try {
                    final File wav = new File(System.getProperty("java.io.tmpdir"), vd.getName() + Controller.WAV);
                    vd.writeWav(wav);
                    TEMP_FILE_LIST.add(wav);
                } catch (final IOException e) {
                    ExceptionDialog.show(e);
                }

            final ClipboardContent content = new ClipboardContent();
            // files for DnD to Desktop
            content.putFiles(TEMP_FILE_LIST);

            // item name for DnD to editor or text field
            content.putString(getItem().getName());

            // serialize selected items for DnD to other VDFEditor instance
            content.put(DnD_DATA_FORMAT, new ArrayList<>(selectedItems));

            final Dragboard db = startDragAndDrop(TransferMode.COPY_OR_MOVE);
            db.setContent(content);

            ev.consume();
        });

        // accept a drop
        setOnDragOver(ev -> {
            if (ev.getGestureSource() instanceof VoiceDataListCell && ev.getGestureSource() != this)
                // allow copy and move within the list
                ev.acceptTransferModes(TransferMode.COPY_OR_MOVE);

            else if (ev.getDragboard().hasContent(DnD_DATA_FORMAT))
                // allow copy between two VDFEditor instances
                ev.acceptTransferModes(TransferMode.COPY);

            else if (ev.getGestureSource() == null && ev.getDragboard().hasFiles())
                // allow only copy from desktop (import of sound file)
                if (ev.getDragboard().getFiles().stream().allMatch(Controller::isSoundFormat)) ev.acceptTransferModes(TransferMode.COPY);

            ev.consume();
        });

        // highlight cell while dragging over it
        setOnDragEntered(ev -> {
            if (ev.getGestureSource() != this) setOpacity(0.3d);
            ev.consume();
        });

        // return to normal
        setOnDragExited(ev -> {
            setOpacity(1.0d);
            ev.consume();
        });

        // perform drop actions
        setOnDragDropped(ev -> {
            final ObservableList<VoiceData> items = getListView().getItems();
            final Object gestureSource = ev.getGestureSource();
            final int targetIndex = ((VoiceDataListCell) ev.getGestureTarget()).getIndex();
            final Dragboard dragboard = ev.getDragboard();

            if (gestureSource instanceof VoiceDataListCell && gestureSource != this) {
                // DnD within the same list
                final int sourceIndex = ((VoiceDataListCell) gestureSource).getIndex();
                final VoiceData item = ((VoiceDataListCell) gestureSource).getItem();

                // delete first to avoid hitting size limits
                if (ev.getTransferMode() == TransferMode.MOVE) {
                    items.remove(sourceIndex);
                    getListView().getSelectionModel().clearSelection();
                }

                items.add(targetIndex, item);
                ev.setDropCompleted(true);
            } else if (dragboard.hasContent(DnD_DATA_FORMAT)) {
                items.addAll(targetIndex, (ArrayList<VoiceData>) dragboard.getContent(DnD_DATA_FORMAT));
                ev.setDropCompleted(true);
            } else if (gestureSource == null && dragboard.hasFiles()) {
                try {
                    // import .wav file from desktop
                    items.addAll(targetIndex,
                            dragboard.getFiles().stream().filter(Controller::isSoundFormat).map(VoiceData::readSoundFile).collect(Collectors.toList()));
                } catch (final RuntimeException e) {
                    ExceptionDialog.show(e);
                }

                ev.setDropCompleted(true);
            }

            ev.consume();
        });

        // cleanup
        setOnDragDone(ev -> {
            deleteTempFiles();
        });
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

    private void deleteTempFiles() {
        TEMP_FILE_LIST.stream().forEach(f -> f.delete());
        TEMP_FILE_LIST.clear();
    }

    @Override
    public void startEdit() {
        textField = new TextField(getText());
        textField.setOnAction(ev -> {
            String newName = textField.getText();
            if (newName.length() > 17) newName = newName.substring(0, 17);

            getItem().setName(newName);
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
