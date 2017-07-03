package de.treichels.hott.vdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import gde.model.voice.VoiceData;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class VoiceDataListCell extends ListCell<VoiceData> {
    private static final String STYLESHEET_LOCATION = VoiceDataListCell.class.getResource("style.css").toString();
    private static final List<File> TEMP_FILE_LIST = new ArrayList<>();

    private final Label indexLabel = new Label();
    private final Button playButton = new Button();
    private TextField textField = null;
    private final HBox hBox;

    public VoiceDataListCell() {
        getStylesheets().add(STYLESHEET_LOCATION);

        playButton.setOnAction(event -> {
            try {
                getItem().play();
            } catch (LineUnavailableException | InterruptedException | IOException e) {
                ExceptionDialog.show(e);
            }
        });

        indexLabel.setMinWidth(25);
        indexLabel.setAlignment(Pos.CENTER_RIGHT);

        hBox = new HBox(indexLabel, playButton);
        hBox.setAlignment(Pos.CENTER_LEFT);
        setGraphic(hBox);

        // Start a drag operation
        setOnDragDetected(ev -> {
            deleteTempFiles();

            // generate temporary .wav files for each selected item for DnD to desktop (eport of .wav file)
            for (final VoiceData vd : getListView().getSelectionModel().getSelectedItems())
                try {
                    final File wav = new File(System.getProperty("java.io.tmpdir"), vd.getName() + ".wav");
                    vd.writeWav(wav);
                    TEMP_FILE_LIST.add(wav);
                } catch (final IOException e) {
                    ExceptionDialog.show(e);
                }

            final ClipboardContent content = new ClipboardContent();
            content.putFiles(TEMP_FILE_LIST);
            content.putString(getItem().getName());

            final Dragboard db = startDragAndDrop(TransferMode.COPY_OR_MOVE);
            db.setContent(content);

            ev.consume();
        });

        // accept a drop
        setOnDragOver(ev -> {
            if (ev.getGestureSource() == null && ev.getDragboard().hasFiles())
                // allow only copy from desktop (import of .wav file)
                ev.acceptTransferModes(TransferMode.COPY);
            else if (ev.getGestureSource() instanceof VoiceDataListCell && ev.getGestureSource() != this)
                // allow copy and move within the list
                ev.acceptTransferModes(TransferMode.COPY_OR_MOVE);

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
            if (ev.getGestureSource() == null && ev.getDragboard().hasFiles()) {
                // import .wav file from desktop
                final int toIndex = getIndex();
                final ObservableList<VoiceData> items = getListView().getItems();
                for (final File file : ev.getDragboard().getFiles())
                    try {
                        final VoiceData voiceData = VoiceData.readWav(file);
                        items.add(toIndex, voiceData);
                    } catch (UnsupportedAudioFileException | IOException e) {
                        ExceptionDialog.show(e);
                    }
            } else if (ev.getGestureSource() instanceof VoiceDataListCell && ev.getGestureSource() != this) {
                final VoiceDataListCell other = (VoiceDataListCell) ev.getGestureSource();

                if (getListView() == other.getListView()) {
                    final ListView<VoiceData> listView = getListView();

                    int fromIndex = other.getIndex();
                    int toIndex = getIndex();

                    if (toIndex < fromIndex)
                        fromIndex++;
                    else
                        toIndex++;

                    final ObservableList<VoiceData> items = listView.getItems();
                    items.add(toIndex, other.getItem());
                    if (ev.getTransferMode() == TransferMode.MOVE) {
                        items.remove(fromIndex);
                        listView.getSelectionModel().clearSelection();
                    }

                    ev.setDropCompleted(true);
                }
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
            getItem().setName(textField.getText());
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

        if (!empty && item != null) {
            indexLabel.setText(Integer.toString(getIndex() + 1));
            setText(item.getName());
        }
    }
}
