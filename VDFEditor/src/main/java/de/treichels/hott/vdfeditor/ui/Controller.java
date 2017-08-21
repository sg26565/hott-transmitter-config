package de.treichels.hott.vdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.sun.javafx.collections.ObservableListWrapper;

import de.treichels.hott.HoTTDecoder;
import gde.model.HoTTException;
import gde.model.enums.TransmitterType;
import gde.model.voice.CountryCode;
import gde.model.voice.VDFType;
import gde.model.voice.VoiceData;
import gde.model.voice.VoiceFile;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class Controller {
    private static final DataFormat DnD_DATA_FORMAT = new DataFormat(VoiceDataListCell.class.getName());
    private static final String USER_HOME = "user.home"; //$NON-NLS-1$
    private static final String _WAV = "*.wav"; //$NON-NLS-1$
    private static final String _MP3 = "*.mp3"; //$NON-NLS-1$
    private static final String _OGG = "*.ogg"; //$NON-NLS-1$
    private static final String _VDF = "*.vdf"; //$NON-NLS-1$
    private static final String WAV = ".wav"; //$NON-NLS-1$
    private static final String MP3 = ".mp3"; //$NON-NLS-1$
    private static final String OGG = ".ogg"; //$NON-NLS-1$
    private static final String VDF = ".vdf"; //$NON-NLS-1$
    private static final String LAST_LOAD_VDF_DIR = "lastLoadVdfDir"; //$NON-NLS-1$
    private static final String LAST_SAVE_VDF_DIR = "lastSaveVdfDir"; //$NON-NLS-1$
    private static final String LAST_LOAD_SOUND_DIR = "lastLoadSoundDir"; //$NON-NLS-1$
    private static final Preferences PREFS = Preferences.userNodeForPackage(Controller.class);
    private static final ResourceBundle RES = ResourceBundle.getBundle(Controller.class.getName());
    private static final List<File> TEMP_FILE_LIST = new ArrayList<>();
    static final Image ICON = new Image(Controller.class.getResource("icon.png").toString());

    /**
     * Delete temporary file that were created during drag&drop.
     *
     * @param ev
     */
    private static void deleteTempFiles(final DragEvent ev) {
        TEMP_FILE_LIST.stream().forEach(f -> f.delete());
        TEMP_FILE_LIST.clear();
    }

    /**
     * Check if the file can be used for DnD (i.e. if it is a sound file or a VDF).
     *
     * @param file
     * @return
     */
    public static boolean isDnDFile(final File file) {
        return isSoundFormat(file) || isVDF(file);
    }

    /**
     * Check if the file is a supported sound file.
     *
     * @param file
     * @return
     */
    public static boolean isSoundFormat(final File file) {
        final String name = file.getName();
        return name.endsWith(WAV) | name.endsWith(MP3) | name.endsWith(OGG);
    }

    /**
     * Check if the file is a VDF.
     *
     * @param file
     * @return
     */
    public static boolean isVDF(final File file) {
        return file.getName().endsWith(VDF);
    }

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
    private MenuItem addSoundMenuItem;
    @FXML
    private ComboBox<TransmitterType> transmitterTypeCombo;
    @FXML
    private ComboBox<VDFType> vdfTypeCombo;
    @FXML
    private ComboBox<Float> vdfVersionCombo;
    @FXML

    private ComboBox<CountryCode> countryCodeCombo;
    private final ObjectProperty<VoiceFile> voiceFileProperty = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty systemVDFProperty = new SimpleBooleanProperty() {
        @Override
        public boolean get() {
            return voiceFileProperty.get() != null && voiceFileProperty.get().getVdfType() == VDFType.System;
        }
    };

    private final SimpleBooleanProperty dirtyProperty = new SimpleBooleanProperty(false);
    private File vdfFile = null;

    public boolean askSave() {
        // no need to save
        if (!dirtyProperty.get()) return true;

        // show waring dialog
        final ButtonType discardButton = new ButtonType(RES.getString("discard_button"));
        final ButtonType saveButton = new ButtonType(RES.getString("save_button"));
        final Alert alert = new Alert(AlertType.WARNING, RES.getString("save_changes"), saveButton, discardButton, ButtonType.CANCEL); //$NON-NLS-1$
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(ICON);
        alert.setHeaderText(RES.getString("modified")); //$NON-NLS-1$
        final Optional<ButtonType> answer = alert.showAndWait();

        // yes, discard changes
        if (answer.get() == discardButton) {
            dirtyProperty.set(false);
            return true;
        }

        // cancel, do nothing
        if (answer.get() == ButtonType.CANCEL) return false;

        // call save dialog and load new file only if save succeeds
        return onSave();
    }

    /**
     * sanity check for system voice files to prevent transmitter malfunction
     *
     * @return
     */
    private boolean checkSize() {
        final VoiceFile voiceFile = voiceFileProperty.get();
        final VDFType vdfType = voiceFile.getVdfType();
        final int vdfVersion = voiceFile.getVdfVersion();
        final int voiceCount = voiceFile.getVoiceData().size();
        final CountryCode countryCode = voiceFile.getCountry();
        final TransmitterType transmitterType = voiceFile.getTransmitterType();
        boolean valid = true;

        // User VDFs are always ok
        if (vdfType == VDFType.User) {
            if (transmitterType == TransmitterType.mc26 || transmitterType == TransmitterType.mc28)
                valid = voiceCount <= 40;
            else
                valid = voiceCount <= 10;
        } else if (vdfVersion == 2000) {
            if (countryCode == CountryCode.eu)
                valid = voiceCount == 250;
            else
                valid = voiceCount == 253;
        } else if (vdfVersion == 2500)
            valid = voiceCount == 284;
        else if (vdfVersion == 3000) valid = voiceCount == 432;

        if (!valid) {
            final Alert alert = new Alert(AlertType.ERROR, RES.getString("system_vdf_too_small_body"));
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(ICON);
            alert.setHeaderText(RES.getString("system_vdf_too_small_title"));
            alert.showAndWait();
            return false;
        }

        // display a disclaimer when saving a system vdf
        if (valid && vdfType == VDFType.System) {
            final ButtonType accept = new ButtonType(RES.getString("accept_button"));
            final Alert alert = new Alert(AlertType.WARNING, RES.getString("system_vdf_disclaimer_body"), accept, ButtonType.CANCEL);
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(ICON);
            alert.setHeaderText(RES.getString("system_vdf_disclaimer_title"));
            final Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == accept;
        }

        return true;
    }

    /**
     * Factory method to create new {@link VoiceDataListCell} objects.
     *
     * @param listView
     * @return
     */
    private VoiceDataListCell createListCell(final ListView<VoiceData> listView) {
        final VoiceDataListCell cell = new VoiceDataListCell();

        // setup DnD
        cell.setOnDragDetected(this::onDragDetected);
        cell.setOnDragOver(ev -> onDragOver(ev, cell));
        cell.setOnDragEntered(this::onDragEntered);
        cell.setOnDragExited(this::onDragExited);
        cell.setOnDragDropped(this::onDragDropped);
        cell.setOnDragDone(Controller::deleteTempFiles);

        return cell;
    }

    @FXML
    public void initialize() {
        // set factory method
        listView.setCellFactory(this::createListCell);

        // enable multi-select
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // clear selection if clicked on an empty row
        listView.setOnMouseClicked(ev -> listView.getSelectionModel().clearSelection());

        // clear selection if ESC key was pressed
        listView.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ESCAPE) listView.getSelectionModel().clearSelection();
        });

        // accept a drop into empty area of listview
        listView.setOnDragOver(ev -> onDragOver(ev, listView));

        // perform drop actions on listview
        listView.setOnDragDropped(this::onDragDropped);

        // setup combo boxes
        vdfVersionCombo.getItems().addAll(2.0f, 2.5f, 3.0f);
        vdfTypeCombo.getItems().addAll(VDFType.values());
        countryCodeCombo.getItems().addAll(CountryCode.values());
        transmitterTypeCombo.getItems().addAll(TransmitterType.values());
        transmitterTypeCombo.getItems().remove(TransmitterType.unknown);

        // disable items if no vdf was loaded
        final BooleanBinding noVdf = voiceFileProperty.isNull();
        vdfVersionCombo.disableProperty().bind(noVdf);
        vdfTypeCombo.disableProperty().bind(noVdf);;
        countryCodeCombo.disableProperty().bind(noVdf);
        transmitterTypeCombo.disableProperty().bind(noVdf);
        editMenu.disableProperty().bind(noVdf);
        saveVDFMenuItem.disableProperty().bind(noVdf.or(dirtyProperty.not()));
        addSoundMenuItem.disableProperty().bind(noVdf.or(systemVDFProperty));

        // disable menu items id no row was selected
        final BooleanBinding noSelection = listView.getSelectionModel().selectedItemProperty().isNull();

        contextMenuDelete.disableProperty().bind(noSelection);
        moveDownMenuItem.disableProperty().bind(noSelection.or(systemVDFProperty));
        moveUpMenuItem.disableProperty().bind(noSelection.or(systemVDFProperty));
        playMenuItem.disableProperty().bind(noSelection);
        renameMenuItem.disableProperty().bind(noSelection);
        deleteSoundMenuItem.disableProperty().bind(noSelection);

        // always start with an empty vdf
        onNew();
    }

    /**
     * Show about dialog.
     */
    @FXML
    public void onAbout() {
        final Alert alert = new Alert(AlertType.INFORMATION, RES.getString("about_text"), ButtonType.CLOSE); //$NON-NLS-1$
        alert.setTitle(RES.getString("about"));
        alert.setHeaderText(Launcher.getTitle());
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(ICON);
        alert.showAndWait();
    }

    /**
     * Show file selector to add a new sound file.
     * <p>
     * The method will remember the directory being used.
     */
    @FXML
    public void onAddSound() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle(RES.getString("load_sound")); //$NON-NLS-1$

        // use dir from preferences
        final File dir = new File(PREFS.get(LAST_LOAD_SOUND_DIR, System.getProperty(USER_HOME)));
        if (dir != null && dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);

        // setup file name filter
        chooser.getExtensionFilters().add(new ExtensionFilter(RES.getString("sound_files"), _WAV, _MP3, _OGG)); //$NON-NLS-1$
        chooser.getExtensionFilters().add(new ExtensionFilter(RES.getString("wav_files"), _WAV)); //$NON-NLS-1$
        chooser.getExtensionFilters().add(new ExtensionFilter(RES.getString("mp3_files"), _MP3)); //$NON-NLS-1$
        chooser.getExtensionFilters().add(new ExtensionFilter(RES.getString("ogg_files"), _OGG)); //$NON-NLS-1$

        final List<File> files = chooser.showOpenMultipleDialog(listView.getScene().getWindow());
        if (files != null) {
            // store dir in preferences
            files.stream().filter(Controller::isSoundFormat).findFirst().map(File::getParentFile).map(File::getAbsolutePath)
                    .ifPresent(s -> PREFS.put(LAST_LOAD_SOUND_DIR, s));

            int selectedIndex = listView.getSelectionModel().getSelectedIndex();

            // add to end of the list if no item was selected
            if (selectedIndex == -1) selectedIndex = listView.getItems().size();

            listView.getItems().addAll(selectedIndex,
                    files.stream().filter(Controller::isSoundFormat).map(VoiceData::readSoundFile).collect(Collectors.toList()));
        }
    }

    /**
     * Exit the application.
     */
    @FXML
    public void onClose() {
        if (askSave()) System.exit(0);
    }

    @FXML
    public void onCountryCodeChanged() {
        voiceFileProperty.get().setCountry(countryCodeCombo.getValue());
    }

    /**
     * Delete the selected sounds.
     */
    @FXML
    public void onDeleteSound() {
        final MultipleSelectionModel<VoiceData> selectionModel = listView.getSelectionModel();
        final ObservableList<VoiceData> items = listView.getItems();

        final ObservableList<Integer> selectedIndices = selectionModel.getSelectedIndices();
        if (systemVDFProperty.get())
            selectedIndices.stream().forEach(i -> items.set(i, new VoiceData(String.format("%d.%s", i + 1, RES.getString("empty")), null)));
        else
        // sort indices in reverse order and remove from high to low
            selectedIndices.stream().sorted((i, j) -> j - i).forEach(i -> items.remove(i.intValue()));

        // fix selection - select the item after the last deleted one
        final int index = selectionModel.getSelectedIndex() + 1;
        final int maxIndex = listView.getItems().size() - 1;
        selectionModel.clearSelection();
        selectionModel.select(Math.min(maxIndex, index));
    }

    /**
     * Handle DnD start event.
     * <p>
     * Three drop targets are supported:
     * <ul>
     * <li><b>Drop on desktop</b>: Create temporary .wav files for any selected item and add the path to the clipboard.</li>
     * <li><b>Another VDFEditor instance</b>: Serialize any selected item into the clipboard.</li>
     * <li><b>other (e.g. text editor)</b>: Add the name of any selected item as comma separated list to the clibboard.</li>
     * </ul>
     *
     * @param ev
     */
    private void onDragDetected(final MouseEvent ev) {
        deleteTempFiles(null);

        final ClipboardContent content = new ClipboardContent();

        // selected items
        final ObservableList<VoiceData> selectedItems = listView.getSelectionModel().getSelectedItems();

        // DnD to desktop: generate temporary .wav files for each selected item for DnD to desktop (export of .wav file)
        selectedItems.forEach(vd -> {
            try {
                final File wav = new File(System.getProperty("java.io.tmpdir"), vd.getName() + Controller.WAV);
                vd.writeWav(wav);
                TEMP_FILE_LIST.add(wav);
            } catch (final IOException e) {
                ExceptionDialog.show(e);
            }
        });
        content.putFiles(TEMP_FILE_LIST);

        // VDFEditor: serialize selected items for DnD to other VDFEditor instance
        content.put(DnD_DATA_FORMAT, new ArrayList<>(selectedItems));

        // other: item name for DnD to editor or text field
        content.putString(StringUtils.join(selectedItems.stream().map(vd -> vd.getName()).toArray(), ","));

        final Dragboard db = ((Node) ev.getSource()).startDragAndDrop(TransferMode.COPY_OR_MOVE);
        db.setContent(content);

        ev.consume();
    }

    /**
     * Handle Dnd drop event.
     * <p>
     * There are three use cases:
     * <ul>
     * <li><b>DnD within the same list</b>: Copy (with CTRL) or move items within the list.</li>
     * <li><b>DnD between VDFEditor instances</b>: Deserialize items from the clipboard.</li>
     * <li><b>DnD from desktop</b>: import any sound or vdf files.</li>
     * </ul>
     *
     * @param ev
     */
    private void onDragDropped(final DragEvent ev) {
        try {
            if (voiceFileProperty.get() == null) onNew();

            final Dragboard dragboard = ev.getDragboard();
            final ObservableList<VoiceData> items = listView.getItems();
            final Object source = ev.getGestureSource();
            final Object target = ev.getGestureTarget();
            final int targetIndex = target instanceof VoiceDataListCell ? ((VoiceDataListCell) target).getIndex() : items.size();
            final boolean isSystemVDF = systemVDFProperty.get();

            if (source instanceof VoiceDataListCell && target instanceof VoiceDataListCell && source != target) {
                // DnD within the same list

                if (!isSystemVDF) {
                    // only for user VDFs
                final int sourceIndex = ((VoiceDataListCell) source).getIndex();
                final VoiceData item = ((VoiceDataListCell) source).getItem();

                items.remove(sourceIndex);
                items.add(targetIndex, item);
                listView.getSelectionModel().clearSelection();
                }
            } else if (source == null)
                // DnD from external source

                if (dragboard.hasContent(DnD_DATA_FORMAT)) {
                // external source is another VDFEditor

                @SuppressWarnings("unchecked")
                final ArrayList<VoiceData> list = (ArrayList<VoiceData>) dragboard.getContent(DnD_DATA_FORMAT);

                if (isSystemVDF)
                // replace item at target index with first item
                items.set(targetIndex, list.get(0));
                else
                // insert all items at target index
                items.addAll(targetIndex, list);

                } else if (ev.getDragboard().hasFiles()) {
                // DnD from desktop
                final List<File> files = dragboard.getFiles();

                // import first .vdf file
                if (files.stream().allMatch(Controller::isVDF)) open(files.get(0));

                // import any sound files
                if (files.stream().allMatch(Controller::isSoundFormat)) if (isSystemVDF)
                // replace item at target index with first file
                items.set(targetIndex, VoiceData.readSoundFile(files.get(0)));
                else
                items.addAll(targetIndex, files.stream().map(VoiceData::readSoundFile).collect(Collectors.toList()));
            }
        } catch (final RuntimeException e) {
            ExceptionDialog.show(e);
        }

        ev.setDropCompleted(true);
        ev.consume();
    }

    /**
     * Handle DnD event when the mouse pointer enters a node.
     *
     * @param ev
     */
    private void onDragEntered(final DragEvent ev) {
        if ((!systemVDFProperty.get() || ev.getGestureSource() == null) && !ev.getTarget().equals(ev.getGestureSource()))
            ((Node) ev.getTarget()).setOpacity(0.3d);
        ev.consume();
    }

    /**
     * Handle DnD event when the mouse pointer leaves a node.
     *
     * @param ev
     */
    private void onDragExited(final DragEvent ev) {
        ((Node) ev.getTarget()).setOpacity(1.0d);
        ev.consume();
    }

    /**
     * Report supported transfer modes for the node under the mouse pointer.
     *
     * @param ev
     *            The event.
     * @param target
     *            The node that triggered the event. Note, that we can't use {@link DragEvent#getGestureTarget()} here, because the drag is still in progress
     *            and {@link DragEvent#getGestureTarget()} would return null. We also can't use {@link DragEvent#getTarget()}, as this could be a cild of the
     *            node triggering the event (e.g. an internal label).
     */
    private void onDragOver(final DragEvent ev, final Node target) {
        final Dragboard dragboard = ev.getDragboard();
        final ObservableList<VoiceData> items = listView.getItems();
        final Object source = ev.getGestureSource();
        final int targetIndex = target instanceof VoiceDataListCell ? ((VoiceDataListCell) target).getIndex() : items.size();
        final boolean isSystemVDF = systemVDFProperty.get();

        if (source instanceof VoiceDataListCell) {
            // DnD within the same list

            if (!isSystemVDF && target instanceof VoiceDataListCell && source != target)
                // allow only for user VDFs and if source != target
                ev.acceptTransferModes(TransferMode.MOVE);

        } else if (source == null)
            // DnD from external source

            if (dragboard.hasContent(DnD_DATA_FORMAT)) {
            // external source is another VDFEditor

            if (isSystemVDF) {
            // allow only one item to replace an existing item for system VDFs
            @SuppressWarnings("unchecked")
            final ArrayList<VoiceData> list = (ArrayList<VoiceData>) dragboard.getContent(DnD_DATA_FORMAT);
            if (list.size() == 1 && targetIndex < items.size()) ev.acceptTransferModes(TransferMode.COPY);
            } else
            // allow multiple items for user VDFs
            ev.acceptTransferModes(TransferMode.COPY);

            } else if (dragboard.hasFiles()) {
            // DnD from desktop
            final List<File> files = dragboard.getFiles();

            if (files.stream().allMatch(Controller::isVDF) && files.size() == 1)
            // only one VDF file was dragged
            ev.acceptTransferModes(TransferMode.COPY);
            else if (files.stream().allMatch(Controller::isSoundFormat))
                // only sound files were dragged
                if (isSystemVDF) {
                if (files.size() == 1 && targetIndex < items.size())
                    // allow only one sound file to be dropped replacing an existing sound for system VDFs
                    ev.acceptTransferModes(TransferMode.COPY);
                } else
                // allow multiple sound files for user VDFs
                ev.acceptTransferModes(TransferMode.COPY);
            }

        ev.consume();
    }

    /**
     * Move the selected item down in the list.
     */
    @FXML
    public void onMoveDown() {
        if (!systemVDFProperty.get()) {
            // only for user VDFs
        final MultipleSelectionModel<VoiceData> selectionModel = listView.getSelectionModel();
        final VoiceData selectedItem = selectionModel.getSelectedItem();
        final int selectedIndex = selectionModel.getSelectedIndex();
        final ObservableList<VoiceData> items = listView.getItems();

        items.remove(selectedIndex);
        items.add(selectedIndex + 1, selectedItem);
        selectionModel.clearSelection();
        selectionModel.select(selectedIndex + 1);
    }
    }

    /**
     * Move the selected item up in the list.
     */
    @FXML
    public void onMoveUp() {
        if (!systemVDFProperty.get()) {
            // only for user VDFs
        final MultipleSelectionModel<VoiceData> selectionModel = listView.getSelectionModel();
        final VoiceData selectedItem = selectionModel.getSelectedItem();
        final int selectedIndex = selectionModel.getSelectedIndex();
        final ObservableList<VoiceData> items = listView.getItems();

        items.remove(selectedIndex);
        items.add(selectedIndex - 1, selectedItem);
        selectionModel.clearSelection();
        selectionModel.select(selectedIndex - 1);
    }
    }

    /**
     * Create a new empty VDF. Ask to save the old VDF if it was modified.
     */
    @FXML
    public void onNew() {
        if (askSave()) {
            vdfFile = null;
            open(new VoiceFile());
            updateVDFVersion();
        }
    }

    /**
     * Show file selector to open a VDF.
     * <p>
     * The method will remember the directory being used.
     *
     * @throws IOException
     */
    @FXML
    public void onOpen() throws IOException {
        if (askSave()) {
            final FileChooser chooser = new FileChooser();
            chooser.setTitle(RES.getString("open_vdf")); //$NON-NLS-1$

            // use dir from preferences
            final File dir = new File(PREFS.get(LAST_LOAD_VDF_DIR, System.getProperty(USER_HOME)));
            if (dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);

            // setup file name filter
            chooser.getExtensionFilters().add(new ExtensionFilter(RES.getString("vdf_files"), _VDF)); //$NON-NLS-1$

            final File vdf = chooser.showOpenDialog(listView.getScene().getWindow());
            if (vdf != null) {
                // store dir in preferences
                PREFS.put(LAST_LOAD_VDF_DIR, vdf.getParentFile().getAbsolutePath());
                open(vdf);
            }
        }
    }

    /**
     * Playback the sound of the selected item.
     */
    @FXML
    public void onPlay() {
        listView.getSelectionModel().getSelectedItems().forEach(VoiceData::play);
    }

    /**
     * Rename the selected item.
     */
    @FXML
    public void onRename() {
        listView.edit(listView.getSelectionModel().getSelectedIndex());
    }

    /**
     * Show dialog to save the current VDF.
     * <p>
     * This method will remember the directory being used.
     *
     * @return
     */
    @FXML
    public boolean onSave() {
        if (!checkSize() || !dirtyProperty.get()) return false;

        final FileChooser chooser = new FileChooser();
        chooser.setTitle(RES.getString("save_vdf")); //$NON-NLS-1$

        // use dir from preferences
        final File dir = new File(PREFS.get(LAST_SAVE_VDF_DIR, System.getProperty(USER_HOME)));
        if (dir.exists() && dir.isDirectory()) chooser.setInitialDirectory(dir);

        // set default file name
        if (vdfFile != null) chooser.setInitialFileName(vdfFile.getName());

        // setup file name filter
        chooser.getExtensionFilters().add(new ExtensionFilter(RES.getString("vdf_files"), _VDF)); //$NON-NLS-1$

        final File vdf = chooser.showSaveDialog(listView.getScene().getWindow());
        if (vdf != null) {
            // store dir in preferences
            PREFS.put(LAST_SAVE_VDF_DIR, vdf.getParentFile().getAbsolutePath());

            try {
                if (vdf.equals(vdfFile) && voiceFileProperty.get().getVdfType() == VDFType.System) {
                    ErrorDialog.show(RES.getString("will_not_overwrite_system_vdf"), RES.getString("system_vdf_exists"), vdf.getName());
                    return false;
                }

                HoTTDecoder.encodeVDF(voiceFileProperty.get(), vdf);
                vdfFile = vdf;
                dirtyProperty.set(false);
                setTitle();

                // everything ok
                return true;
            } catch (final IOException e) {
                ExceptionDialog.show(e);
            }
        }

        // something went wrong
        return false;
    }

    /**
     * Handle event for change in transmitter type.
     *
     * @param ev
     */
    @FXML
    public void onTransmitterTypeChanged() {
        final VoiceFile voiceFile = voiceFileProperty.get();
        final TransmitterType oldTtransmitterType = voiceFile.getTransmitterType();

        try {
            voiceFile.setTransmitterType(transmitterTypeCombo.getValue());
            HoTTDecoder.verityVDF(voiceFile);
            setTitle();
        } catch (final HoTTException e) {
            voiceFile.setTransmitterType(oldTtransmitterType);
            transmitterTypeCombo.setValue(oldTtransmitterType);
            ExceptionDialog.show(e);
        }

        updateVDFVersion();
    }

    /**
     * Handle event for change in VDF type.
     *
     * @param ev
     */
    @FXML
    public void onVDFTypeChanged() {
        final VoiceFile voiceFile = voiceFileProperty.get();
        final VDFType oldVDFType = voiceFile.getVdfType();

        try {
            voiceFile.setVdfType(vdfTypeCombo.getValue());
            HoTTDecoder.verityVDF(voiceFile);
            setTitle();
        } catch (final HoTTException e) {
            voiceFile.setVdfType(oldVDFType);
            vdfTypeCombo.setValue(oldVDFType);
            ExceptionDialog.show(e);
        }

        updateVDFVersion();
    }

    @FXML
    public void onVDFVersionChanged() {
        final Float vdfVersion = vdfVersionCombo.getValue();

        if (vdfVersion != null) {
            final VoiceFile voiceFile = voiceFileProperty.get();
            voiceFile.setVdfVersion((int) (vdfVersion * 1000));
        }
    }

    /**
     * Load a new VDF from file. Ask for save if current VDF was modified.
     *
     * @param vdf
     */
    public void open(final File vdf) {
        if (askSave()) {
            final File oldVdf = vdfFile;

            try {
                vdfFile = vdf;
                open(HoTTDecoder.decodeVDF(vdf));
            } catch (final IOException e) {
                vdfFile = oldVdf;
                ExceptionDialog.show(e);
            }
        }
    }

    /**
     * Load a new VDF from data model.
     *
     * @param voiceFile
     */
    private void open(final VoiceFile voiceFile) {
        voiceFileProperty.set(voiceFile);
        vdfTypeCombo.setValue(voiceFile.getVdfType());
        vdfVersionCombo.setValue(voiceFile.getVdfVersion() / 1000.0f);
        countryCodeCombo.setValue(voiceFile.getCountry());
        transmitterTypeCombo.setValue(voiceFile.getTransmitterType());

        final ObservableListWrapper<VoiceData> items = new ObservableListWrapper<>(voiceFile.getVoiceData());

        // add a change listener to the list to prevent invalid VDFs
        items.addListener((ListChangeListener<VoiceData>) c -> {
            dirtyProperty.set(true);

            while (c.next())
                // if an item was added, check if the size limit was reached
                if (c.wasAdded()) {
                    final int fistIndex = c.getFrom();
                    int lastIndex = c.getTo() - 1;

                    // check for duplicate entries
                    for (int i = fistIndex; i <= lastIndex && i < items.size(); i++) {
                        final VoiceData voiceData1 = items.get(i);
                        final String name1 = voiceData1.getName();
                        final int hash1 = Arrays.hashCode(voiceData1.getRawData());

                        for (int j = 0; j < items.size(); j++) {
                            if (j == i) continue;

                            final VoiceData voiceData2 = items.get(j);
                            final String name2 = voiceData2.getName();
                            final int hash2 = Arrays.hashCode(voiceData2.getRawData());

                            if (name1.equals(name2)) {
                                items.remove(i);
                                lastIndex--;
                                ErrorDialog.show(RES.getString("duplicate_entry"), RES.getString("duplicate_name"), name1);
                                break;
                            }

                            if (hash1 != 1 && hash1 == hash2) {
                                items.remove(i);
                                lastIndex--;
                                ErrorDialog.show(RES.getString("duplicate_entry"), RES.getString("duplicate_sound"), name1, name2);
                                break;
                            }
                        }
                    }

                    // remove added entries until validation succeeds
                    while (true)
                        try {
                            // validate VDF
                            HoTTDecoder.verityVDF(voiceFileProperty.get());

                            // validation succeeded - no exception was thrown
                            break;
                        } catch (final HoTTException e) {
                            // validation failed, remove last added item and try again
                            items.remove(lastIndex--);
                            ExceptionDialog.show(e);
                        }
                }

            setTitle();
        });

        dirtyProperty.set(false);
        listView.setItems(items);

        setTitle();
    }

    /**
     * Set stage title with information about the current VDF.
     */
    void setTitle() {
        final StringBuilder sb = new StringBuilder();

        if (dirtyProperty.get()) sb.append("*"); //$NON-NLS-1$

        if (vdfFile == null)
            sb.append(RES.getString("empty")); //$NON-NLS-1$
        else
            sb.append(vdfFile.getName());

        if (voiceFileProperty.isNotNull().get()) {
            final VoiceFile voiceFile = voiceFileProperty.get();
            try {
                final int maxDataSize = HoTTDecoder.getMaxDataSize(voiceFile);
                final int dataSize = voiceFile.getDataSize();

                sb.append(String.format(" - %d kb / %d kb (%d%%)", dataSize / 1024, maxDataSize / 1024, dataSize * 100 / maxDataSize)); //$NON-NLS-1$
            } catch (final HoTTException e) {
                ExceptionDialog.show(e);
            }
        }

        final Scene scene = listView.getScene();
        if (scene != null) ((Stage) scene.getWindow()).setTitle(sb.toString());
    }

    private void updateVDFVersion() {
        final VoiceFile voiceFile = voiceFileProperty.get();
        final ObservableList<Float> items = vdfVersionCombo.getItems();

        switch (voiceFile.getTransmitterType()) {
        case mc16:
        case mc20:
        case mc32:
        case mx12:
        case mx16:
        case mx20:
        case mz12:
        case mz12Pro:
        case mz18:
        case mz24:
        case mz24Pro:
            items.remove(3.0f);
            if (voiceFile.getVdfType() == VDFType.User)
                items.remove(2.0f);
            else if (!items.contains(2.0f)) items.add(2.0f);
            if (!items.contains(2.5f)) items.add(2.5f);
            break;

        case mc26:
        case mc28:
            items.removeAll(2.0f, 2.5f);
            if (!items.contains(3.0f)) items.add(3.0f);
            break;

        case unknown:
        default:
            items.clear();
            break;
        }

        if (!items.contains(vdfVersionCombo.getValue())) vdfVersionCombo.setValue(items.size() == 0 ? 0.0f : items.get(0));
    }
}
