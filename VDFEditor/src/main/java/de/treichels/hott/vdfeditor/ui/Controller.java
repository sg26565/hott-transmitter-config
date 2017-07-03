package de.treichels.hott.vdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.javafx.collections.ObservableListWrapper;

import de.treichels.hott.HoTTDecoder;
import gde.mdl.messages.Messages;
import gde.model.enums.TransmitterType;
import gde.model.voice.Announcements.VDFType;
import gde.model.voice.VoiceData;
import gde.model.voice.VoiceFile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
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
    private Button saveVDFButton;
    @FXML
    private MenuItem saveVDFMenuItem;
    @FXML
    private ComboBox<TransmitterType> transmitterTypeCombo;
    @FXML
    private ComboBox<VDFType> vdfTypeCombo;
    private final ObjectProperty<VoiceFile> voiceFileProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        listView.setCellFactory(lv -> new VoiceDataListCell());
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        editMenu.disableProperty().bind(voiceFileProperty.isNull());
        saveVDFButton.disableProperty().bind(voiceFileProperty.isNull());
        saveVDFMenuItem.disableProperty().bind(voiceFileProperty.isNull());

        contextMenuDelete.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        moveDownMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        moveUpMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        playMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        renameMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        deleteSoundMenuItem.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());

        vdfTypeCombo.getItems().addAll(VDFType.values());
        transmitterTypeCombo.getItems().addAll(TransmitterType.values());
        transmitterTypeCombo.getItems().remove(TransmitterType.unknown);
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
        System.exit(0);
    }

    @FXML
    public void onDeleteSound() {
        final ObservableList<VoiceData> selectedItems = listView.getSelectionModel().getSelectedItems();
        listView.getItems().removeAll(selectedItems);
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
    public void onOpen() throws IOException {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Load VDF File");
        final File dir = new File(PREFS.get(LAST_LOAD_DIR, System.getProperty("user.home")));
        if (dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);
        chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("VdfFileDescription"), "*.vdf"));

        final File vdf = chooser.showOpenDialog(listView.getScene().getWindow());
        if (vdf != null) {
            ((Stage) listView.getScene().getWindow()).setTitle(vdf.getName());
            PREFS.put(LAST_LOAD_DIR, vdf.getParentFile().getAbsolutePath());
            final VoiceFile vdfFile = HoTTDecoder.decodeVDF(vdf);
            listView.setItems(new ObservableListWrapper<>(vdfFile.getVoiceData()));
            voiceFileProperty.set(vdfFile);
            vdfTypeCombo.setValue(vdfFile.getVDFType());
            transmitterTypeCombo.setValue(vdfFile.getTransmitterType());
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
    public void onSave() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Save VDF File");
        final File dir = new File(PREFS.get(LAST_SAVE_DIR, System.getProperty("user.home")));
        if (dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);
        chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("VdfFileDescription"), "*.vdf"));

        final File vdf = chooser.showSaveDialog(listView.getScene().getWindow());
        if (vdf != null) {
            ((Stage) listView.getScene().getWindow()).setTitle(vdf.getName());
            PREFS.put(LAST_SAVE_DIR, vdf.getParentFile().getAbsolutePath());

            // TODO: Save VoiceFile
        }
    }

    @FXML
    public void onTransmitterTypeChanged() {
        // TODO: check limits of new transmitter
    }

    @FXML
    public void onVDFTypeChanged() {
        // TODO: check limits of new vdf type
    }
}
