package de.treichels.hott.vdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.javafx.collections.ObservableListWrapper;

import de.treichels.hott.HoTTDecoder;
import gde.mdl.messages.Messages;
import gde.model.HoTTException;
import gde.model.enums.TransmitterType;
import gde.model.voice.Announcements.VDFType;
import gde.model.voice.VoiceData;
import gde.model.voice.VoiceFile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class Controller {
    private static final String LAST_LOAD_DIR = "lastLoadDir"; //$NON-NLS-1$
    private static final String LAST_SAVE_DIR = "lastSaveDir"; //$NON-NLS-1$
    private static final Preferences PREFS = Preferences.userNodeForPackage(Controller.class);

    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem contextMenuDelete;
    @FXML
    private MenuItem deleteSoundMenuItem;
    @FXML
    private Menu editMenu;
    @FXML
    private ListView<VoiceData> listView;
    @FXML
    private MenuItem moveDownMenuItem;
    @FXML
    private MenuItem moveUpMenuItem;
    @FXML
    private MenuItem playMenuItem;
    @FXML
    private MenuItem renameMenuItem;
    @FXML
    private MenuItem saveVDFMenuItem;
    @FXML
    private ComboBox<TransmitterType> transmitterTypeCombo;
    @FXML
    private ComboBox<VDFType> vdfTypeCombo;

    private final ObjectProperty<VoiceFile> voiceFileProperty = new SimpleObjectProperty<>();
    private boolean dirty = false;
    private File vdfFile = null;

    boolean askSave() {
        if (!dirty) return true;

        final Alert alert = new Alert(AlertType.WARNING, "Do you whant to save changes?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("File was modified!");
        final Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.NO) return true;

        return onSave();
    }

    @FXML
    public void initialize() {
        listView.setCellFactory(lv -> new VoiceDataListCell());
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // accept a drop into empty area
        listView.setOnDragOver(ev -> {
            if (ev.getGestureSource() == null && ev.getDragboard().hasFiles())
                // allow only copy from desktop (import of .wav file)
                ev.acceptTransferModes(TransferMode.COPY);
            ev.consume();
        });

        listView.setOnDragEntered(ev -> listView.setOpacity(0.3d));
        listView.setOnDragEntered(ev -> listView.setOpacity(1.0d));

        // perform drop actions
        listView.setOnDragDropped(ev -> {
            if (voiceFileProperty.get() == null) onNew();
            final Dragboard dragboard = ev.getDragboard();
            final ObservableList<VoiceData> items = listView.getItems();

            if (dragboard.hasContent(VoiceDataListCell.DnD_DATA_FORMAT)) {
                // DnD between VDFEditor instances
                @SuppressWarnings("unchecked")
                final ArrayList<VoiceData> droppedItems = (ArrayList<VoiceData>) dragboard.getContent(VoiceDataListCell.DnD_DATA_FORMAT);
                for (final VoiceData vd : droppedItems)
                    items.add(vd);
                ev.setDropCompleted(true);
            } else if (ev.getDragboard().hasFiles()) {
                // import .wav or .vdf files from desktop
                final List<File> files = ev.getDragboard().getFiles();

                // import first .vdf file
                final Optional<File> vdf = files.stream().filter(f -> f.getName().endsWith(".vdf")).findFirst();
                if (vdf.isPresent() && askSave()) {
                    vdfFile = vdf.get();
                    dirty = false;
                    try {
                        open(HoTTDecoder.decodeVDF(vdfFile));
                    } catch (final IOException e) {
                        ExceptionDialog.show(e);
                    }
                }

                // import any .wav files
                files.stream().filter(f -> f.getName().endsWith(".wav")).forEach(f -> {
                    try {
                        items.add(VoiceData.readWav(f));
                    } catch (UnsupportedAudioFileException | IOException e) {
                        ExceptionDialog.show(e);
                    }
                });
                ev.setDropCompleted(true);
            }

            ev.consume();
        });

        editMenu.disableProperty().bind(voiceFileProperty.isNull());
        saveVDFMenuItem.disableProperty().bind(voiceFileProperty.isNull());
        vdfTypeCombo.getItems().addAll(VDFType.values());
        vdfTypeCombo.disableProperty().bind(voiceFileProperty.isNull());
        transmitterTypeCombo.getItems().addAll(TransmitterType.values());
        transmitterTypeCombo.getItems().remove(TransmitterType.mz12);
        transmitterTypeCombo.getItems().remove(TransmitterType.mz18);
        transmitterTypeCombo.getItems().remove(TransmitterType.mz24);
        transmitterTypeCombo.getItems().remove(TransmitterType.mz24Pro);
        transmitterTypeCombo.getItems().remove(TransmitterType.unknown);
        transmitterTypeCombo.disableProperty().bind(voiceFileProperty.isNull());

        contextMenuDelete.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        moveDownMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        moveUpMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        playMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        renameMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        deleteSoundMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    public void onAbout() {
        // TODO
    }

    @FXML
    public void onAddSound() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Sound File");
        final File dir = new File(PREFS.get(LAST_LOAD_DIR, System.getProperty("user.home")));
        if (dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);
        chooser.getExtensionFilters().add(new ExtensionFilter("Wave Files", "*.wav"));

        final File wav = chooser.showOpenDialog(listView.getScene().getWindow());
        if (wav != null) try {
            final VoiceData newVoiceData = VoiceData.readWav(wav);
            final int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            listView.getItems().add(selectedIndex, newVoiceData);
        } catch (UnsupportedAudioFileException | IOException e) {
            ExceptionDialog.show(e);
        }
    }

    @FXML
    public void onClose() {
        if (askSave()) System.exit(0);
    }

    @FXML
    public void onDeleteSound() {
        final ObservableList<Integer> selectedIndices = listView.getSelectionModel().getSelectedIndices();
        // sort indices in reverse order and remove from high to low
        selectedIndices.stream().sorted((i, j) -> j - i).forEach(i -> listView.getItems().remove(i.intValue()));
    }

    @FXML
    public void onMoveDown() {
        final MultipleSelectionModel<VoiceData> selectionModel = listView.getSelectionModel();
        final VoiceData selectedItem = selectionModel.getSelectedItem();
        final int selectedIndex = selectionModel.getSelectedIndex();
        final ObservableList<VoiceData> items = listView.getItems();

        items.remove(selectedIndex);
        items.add(selectedIndex + 1, selectedItem);
        selectionModel.clearSelection();
        selectionModel.select(selectedIndex + 1);
    }

    @FXML
    public void onMoveUp() {
        final MultipleSelectionModel<VoiceData> selectionModel = listView.getSelectionModel();
        final VoiceData selectedItem = selectionModel.getSelectedItem();
        final int selectedIndex = selectionModel.getSelectedIndex();
        final ObservableList<VoiceData> items = listView.getItems();

        items.remove(selectedIndex);
        items.add(selectedIndex - 1, selectedItem);
        selectionModel.clearSelection();
        selectionModel.select(selectedIndex - 1);
    }

    @FXML
    public void onNew() {
        if (askSave()) {
            vdfFile = null;
            dirty = false;
            open(new VoiceFile());
        }
    }

    @FXML
    public void onOpen() throws IOException {
        if (askSave()) {
            final FileChooser chooser = new FileChooser();
            chooser.setTitle("Load VDF File");
            final File dir = new File(PREFS.get(LAST_LOAD_DIR, System.getProperty("user.home")));
            if (dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);
            chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("VdfFileDescription"), "*.vdf"));

            final File vdf = chooser.showOpenDialog(listView.getScene().getWindow());
            if (vdf != null) {
                PREFS.put(LAST_LOAD_DIR, vdf.getParentFile().getAbsolutePath());
                vdfFile = vdf;
                dirty = false;
                open(HoTTDecoder.decodeVDF(vdf));
            }
        }
    }

    @FXML
    public void onPlay() {
        listView.getSelectionModel().getSelectedItems().stream().forEach(vd -> {
            try {
                vd.play();
            } catch (LineUnavailableException | InterruptedException | IOException e) {
                ExceptionDialog.show(e);
            }
        });
    }

    @FXML
    public void onRename() {
        listView.edit(listView.getSelectionModel().getSelectedIndex());
    }

    @FXML
    public boolean onSave() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Save VDF File");
        final File dir = new File(PREFS.get(LAST_SAVE_DIR, System.getProperty("user.home")));
        if (dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);
        chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("VdfFileDescription"), "*.vdf"));

        final File vdf = chooser.showSaveDialog(listView.getScene().getWindow());
        if (vdf != null) {
            ((Stage) listView.getScene().getWindow()).setTitle(vdf.getName());
            PREFS.put(LAST_SAVE_DIR, vdf.getParentFile().getAbsolutePath());

            try {
                HoTTDecoder.encodeVDF(voiceFileProperty.get(), vdf);
                dirty = false;
            } catch (final IOException e) {
                ExceptionDialog.show(e);
            }
        }

        return vdf != null;
    }

    @FXML
    public void onTransmitterTypeChanged(final ActionEvent ev) {
        final VoiceFile voiceFile = voiceFileProperty.get();
        final TransmitterType oldTtransmitterType = voiceFile.getTransmitterType();
        voiceFile.setTransmitterType(transmitterTypeCombo.getValue());

        try {
            HoTTDecoder.verityVDF(voiceFile);
            setTitle();
        } catch (final HoTTException e) {
            voiceFile.setTransmitterType(oldTtransmitterType);
            transmitterTypeCombo.setValue(oldTtransmitterType);
            ExceptionDialog.show(e);
        }

        ev.consume();
    }

    @FXML
    public void onVDFTypeChanged(final ActionEvent ev) {
        final VoiceFile voiceFile = voiceFileProperty.get();
        final VDFType oldVDFType = voiceFile.getVdfType();
        voiceFile.setVdfType(vdfTypeCombo.getValue());

        try {
            HoTTDecoder.verityVDF(voiceFile);
            setTitle();
        } catch (final HoTTException e) {
            voiceFile.setVdfType(oldVDFType);
            vdfTypeCombo.setValue(oldVDFType);
            ExceptionDialog.show(e);
        }

        ev.consume();
    }

    private void open(final VoiceFile voiceFile) {
        voiceFileProperty.set(voiceFile);
        vdfTypeCombo.setValue(voiceFile.getVdfType());
        transmitterTypeCombo.setValue(voiceFile.getTransmitterType());

        final ObservableListWrapper<VoiceData> items = new ObservableListWrapper<>(voiceFile.getVoiceData());
        items.addListener((ListChangeListener<VoiceData>) c -> {
            dirty = true;
            try {
                HoTTDecoder.verityVDF(voiceFileProperty.get());
            } catch (final HoTTException e) {
                while (c.next())
                    items.removeAll(c.getAddedSubList());
                ExceptionDialog.show(e);
            }
            setTitle();
        });
        dirty = false;
        listView.setItems(items);
        setTitle();
    }

    private void setTitle() {
        final StringBuilder sb = new StringBuilder();

        if (dirty) sb.append("*");
        if (vdfFile == null)
            sb.append("<empty>");
        else
            sb.append(vdfFile.getName());

        if (voiceFileProperty.isNotNull().get()) {
            final VoiceFile voiceFile = voiceFileProperty.get();
            try {
                final int maxDataSize = HoTTDecoder.getMaxDataSize(voiceFile);
                final int dataSize = voiceFile.getDataSize();

                sb.append(String.format(" - %d kb / %d kb (%d%%)", dataSize / 1024, maxDataSize / 1024, dataSize * 100 / maxDataSize));
            } catch (final HoTTException e) {
                ExceptionDialog.show(e);
            }
        }

        ((Stage) listView.getScene().getWindow()).setTitle(sb.toString());
    }
}
