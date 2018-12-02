package de.treichels.hott.vdfeditor.ui

import de.treichels.hott.decoder.HoTTDecoder
import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.messages.Messages
import de.treichels.hott.model.HoTTException
import de.treichels.hott.model.enums.TransmitterType
import de.treichels.hott.voice.CountryCode
import de.treichels.hott.voice.VDFType
import de.treichels.hott.voice.VoiceData
import de.treichels.hott.voice.VoiceFile
import de.treichels.hott.report.html.HTMLReport
import de.treichels.hott.report.pdf.PDFReport
import de.treichels.hott.tts.SpeechDialog
import de.treichels.hott.ui.ExceptionDialog
import de.treichels.hott.ui.MessageDialog
import de.treichels.hott.util.Util
import de.treichels.hott.vdfeditor.actions.*
import de.treichels.hott.vdfeditor.ui.transmitter.LoadVoiceFileTask
import de.treichels.hott.vdfeditor.ui.transmitter.SendVoiceFileTask
import de.treichels.hott.vdfeditor.ui.transmitter.TransmitterDialogView
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ListChangeListener
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.input.*
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Screen
import javafx.stage.Stage
import org.apache.commons.io.IOUtils
import tornadofx.*
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalStateException
import java.util.regex.Pattern
import javax.sound.sampled.AudioInputStream

// constants
private const val EXT_WAV = "*.wav"
private const val EXT_MP3 = "*.mp3"
private const val EXT_OGG = "*.ogg"
private const val EXT_PDF = "*.pdf"
private const val EXT_VDF = "*.vdf"
private const val WAV = ".wav"
private const val MP3 = ".mp3"
private const val OGG = ".ogg"
private const val PDF = ".pdf"
private const val VDF = ".vdf"
private const val LAST_LOAD_VDF_DIR = "lastLoadVdfDir"
private const val LAST_SAVE_VDF_DIR = "lastSaveVdfDir"
private const val LAST_LOAD_SOUND_DIR = "lastLoadSoundDir"
private const val USER_HOME = "user.home"

private val dndDataFormat = DataFormat(VoiceDataListCell::class.java.name)
private val tempFiles = mutableListOf<File>()
private val namePattern = Pattern.compile("^[0-9]+([._])(.*)$")
private val userHome = System.getProperty(USER_HOME)

/**
 * Check if the file is a supported sound file.
 */
private fun isSoundFormat(file: File): Boolean = file.name.endsWith(WAV) or file.name.endsWith(MP3) or file.name.endsWith(OGG)

/**
 * Check if the file is a VDF.
 */
private fun isVDF(file: File): Boolean = file.name.endsWith(VDF)

/**
 * Delete temporary file that were created during drag&drop.
 */
private fun deleteTempFiles() {
    tempFiles.forEach { it.delete() }
    tempFiles.clear()
}

class MainView : View() {
    val voiceFile = VoiceFile()
    var undoBuffer = UndoBuffer(voiceFile.voiceList)
    private var mute = false

    // resources
    private val iconImage = resources.image("icon.png")

    // child views
    private val speechDialog by inject<SpeechDialog>()
    private val transmitterDialogView by inject<TransmitterDialogView>()

    // properties
    private val serialPortProperty = SimpleObjectProperty<HoTTSerialPort>(null)
    private var serialPort by serialPortProperty
    private val vdfFileProperty = SimpleObjectProperty<File>(null)
    private var vdfFile by vdfFileProperty
    private val isSystemVDF by voiceFile.systemVDFProperty
    private val isDirty by voiceFile.dirtyBinding

    // controls
    private var listView: ListView<VoiceData> by singleAssign()
    private var transmitterTypeCombo: ComboBox<TransmitterType> by singleAssign()
    private var countryCodeCombo: ComboBox<CountryCode> by singleAssign()
    private var vdfVersionCombo: ComboBox<Float> by singleAssign()
    private var restoreMenu: Menu by singleAssign()

    // helpers
    private val selectionModel
        get() = listView.selectionModel
    private val selectedIndex
        get() = selectionModel.selectedIndex
    private val noSelection
        get() = selectionModel.selectedItemProperty().isNull

    // root node
    override val root = borderpane {
        center {
            listView = listview {
                items = voiceFile.voiceList
                editableProperty().value = true
                prefHeight = 800.0
                prefWidth = 500.0
                borderpaneConstraints { alignment = Pos.CENTER }

                contextmenu {
                    item(messages["delete_sound"]) {
                        // cannot use nosSelection here because listView is not yet initialized
                        disableWhen(voiceFile.systemVDFProperty.or(this@listview.selectionModel.selectedItemProperty().isNull))
                        action { onDeleteSound() }
                    }
                }
            }
        }

        top {
            hbox {
                menubar {
                    menu(messages["file_menu"]) {
                        item(messages["new_vdf"], "Shortcut+N") {
                            action { onNew() }
                        }
                        item(messages["open_vdf"], "Shortcut+O") {
                            action { onOpen() }
                        }
                        restoreMenu = menu(messages["restore_menu"])
                        item(messages["save_vdf"], "Shortcut+S") {
                            enableWhen(vdfFileProperty.isNotNull.and(voiceFile.dirtyBinding))
                            action { onSave() }
                        }
                        item(messages["save_vdf_as"], "Shortcut+Shift+S") {
                            action { onSaveVDFAs() }
                        }
                        separator()
                        item(messages["print"], "Shortcut+Alt+P") {
                            action { onPrint() }
                        }
                        item(messages["close"], "Alt+F4") {
                            action { onClose() }
                        }
                    }
                    menu(messages["edit_menu"]) {
                        item(messages["undo"], "ShortCut+Z") {
                            enableWhen(undoBuffer.canUndoProperty())
                            action { onUndo() }
                        }
                        item(messages["redo"], "ShortCut+Y") {
                            action { onRedo() }
                            enableWhen(undoBuffer.canRedoProperty())
                        }
                        separator()
                        item(messages["move_up"], "ShortCut+U") {
                            disableWhen(noSelection.or(selectionModel.selectedIndexProperty().eq(0)).or(voiceFile.systemVDFProperty))
                            action { onMoveUp() }
                        }
                        item(messages["move_down"], "ShortCut+D") {
                            disableWhen(noSelection.or(selectionModel.selectedIndexProperty().eq(listView.itemsProperty().integerBinding(op = { it?.size ?: 0 }).subtract(1)).or(voiceFile.systemVDFProperty)))
                            action { onMoveDown() }
                        }
                        item(messages["rename"], "ShortCut+R") {
                            disableWhen(noSelection)
                            action { onRename() }
                        }
                        separator()
                        item(messages["play"], "ShortCut+P") {
                            disableWhen(noSelection)
                            action { onPlay() }
                        }
                        separator()
                        menu(messages["add_sound"]) {
                            disableWhen(voiceFile.systemVDFProperty)
                            item(messages["from_file"], "Insert") {
                                action { onAddSound() }
                            }
                            item(messages["from_text"], "ShortCut+T") {
                                action { onAddSoundFromText() }
                                accelerator = KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN)
                            }
                        }
                        menu(messages["replace_sound"]) {
                            disableWhen(noSelection)
                            item(messages["from_file"], "Shift+Insert") {
                                action { onReplaceSound() }
                            }
                            item(messages["from_text"], "Shortcut+Shift+T") {
                                action { onReplaceSoundFromText() }
                            }
                        }
                        item(messages["delete_sound"], "Delete") {
                            disableWhen(voiceFile.systemVDFProperty.or(noSelection))
                            action { onDeleteSound() }
                        }
                    }
                    menu(messages["transmitter_menu"]) {
                        item(messages["load_user_voicefiles"]) {
                            action { onLoadUserVoiceFile() }
                        }
                        item(messages["load_system_voicefiles"]) {
                            action { onLoadSystemVoiceFile() }
                        }
                        separator()
                        item(messages["play_on_transmitter"], "ShortCut+Shift+P") {
                            disableWhen(noSelection.or(serialPortProperty.isNull))
                            action { onPlayOnTransmitter() }
                        }
                        separator()
                        item(messages["write_to_transmitter"]) {
                            action { onWriteToTransmitter() }
                        }
                    }
                }

                region {
                    styleClass += "menu-bar"
                    hgrow = Priority.SOMETIMES
                }

                menubar {
                    menu(messages["help_menu"]) {
                        item(messages["about"]) {
                            action { onAbout() }
                        }
                        item(messages["update_check"]) {
                            visibleProperty().value = !Util.OFFLINE
                            action { onUpdateCheck() }
                        }
                        item(messages["user_manual"]) {
                            action { onUserManual() }
                        }
                    }
                }
            }
        }

        bottom {
            hbox {
                alignment = Pos.CENTER
                spacing = 3.0
                padding = tornadofx.insets(5.0)
                borderpaneConstraints { alignment = Pos.CENTER }

                label(messages["vdf_version"])
                vdfVersionCombo = combobox {
                    setOnAction { onVdfVersionChanged() }
                }
                region {
                    hgrow = Priority.SOMETIMES
                }
                label(messages["country_code"])
                countryCodeCombo = combobox {
                    setOnAction { onCountryCodeChanged() }
                    items.addAll(*CountryCode.values())
                }
                region {
                    hgrow = Priority.SOMETIMES
                }
                label(messages["transmitter_type"])
                transmitterTypeCombo = combobox {
                    setOnAction { onTransmitterTypeChanged() }
                    items.addAll(*TransmitterType.values())
                    items.remove(TransmitterType.unknown)
                    items.remove(TransmitterType.mz32)
                    items.remove(TransmitterType.mz32beta)
                }
            }
        }
    }

    private fun addRestoreFiles() {
        TransmitterType.values().filter { !listOf(TransmitterType.unknown, TransmitterType.mz32, TransmitterType.mz32beta).contains(it) }.forEach { transmitterType ->
            restoreMenu.items.add(Menu(transmitterType.toString()).apply {
                for (variant in listOf(null, "MikroKopter", "Team", "Team-schnell")) {
                    // base name
                    val baseName = when (transmitterType) {
                        TransmitterType.mc26, TransmitterType.mc28 -> "Voice3_mc28"
                        else -> "Voice3"
                    }

                    for (language in Language.values()) {
                        // menu name
                        val menuName = when (variant) {
                            null -> "$language"
                            else -> "$language ($variant)"
                        }

                        // relative path to vdf
                        val path = when (variant) {
                            null -> "restore/${baseName}_${language.name}.vdf"
                            else -> "restore/${baseName}_${language.name}_$variant.vdf"
                        }

                        try {
                            // test if vdf exists - throws exception if path was not found
                            resources.url(path)

                            // add menu item (for language/variant) to submenu (for transmitter)
                            items.add(MenuItem(menuName).apply {
                                setOnAction {
                                    try {
                                        resources.stream(path).use {
                                            if (askSave()) {
                                                HoTTDecoder.decodeVDF(IOUtils.toByteArray(it)).apply {
                                                    this.transmitterType = transmitterType
                                                    open(this)
                                                }
                                            }
                                        }
                                    } catch (e: IOException) {
                                        ExceptionDialog.show(e)
                                    }
                                }
                            })
                        } catch (_: IllegalStateException) {
                            // vdf does not exist - ignore
                        }
                    }
                }

                items.sortBy { it.text }
            })
        }
    }

    private fun addSound(stream: AudioInputStream, name: String, volume: Double = 1.0) {
        // add to end of the list if no item was selected
        val index = if (selectedIndex == -1) listView.items.size else selectedIndex

        undoBuffer.push(InsertAction(index, VoiceData.forStream(stream, name, volume)))
    }

    private fun addSound(vararg files: File, volume: Double = 1.0) {
        // store dir in preferences
        files.filter(::isSoundFormat).map(File::getParentFile).map(File::getAbsolutePath).firstOrNull()?.apply {
            preferences { put(LAST_LOAD_SOUND_DIR, this@apply) }
        }

        // add to end of the list if no item was selected
        val index = if (selectedIndex == -1) listView.items.size else selectedIndex

        // insert all sound files at index
        files.filter(::isSoundFormat).map({ VoiceData.forFile(it, volume) }).forEach { undoBuffer.push(InsertAction(index, it)) }
    }

    /**
     * Ask to save modified VDFs
     *
     * @return <code>true</code> if the current VDF was saved or if changes can be discarded
     */
    private fun askSave(): Boolean {
        if (!isDirty) return true // no need to save

        // show waring dialog
        val discardButton = ButtonType(messages["discard_button"])
        val saveButton = ButtonType(messages["save_button"])
        val cancelButton = ButtonType.CANCEL
        val alert = Alert(AlertType.WARNING, messages["save_changes"], saveButton, discardButton, cancelButton)
        (alert.dialogPane.scene.window as Stage).icons.add(iconImage)
        alert.headerText = messages["modified"]
        val answer = alert.showAndWait()

        return when (answer.get()) {
            discardButton -> {
                voiceFile.clean()
                true
            }
            saveButton -> if (vdfFile == null) onSaveVDFAs() else onSaveVDF()
            else -> false
        }
    }

    /**
     * sanity check for system voice files to prevent transmitter malfunction
     */
    private fun checkSizeBeforeSave(): Boolean {
        try {
            HoTTDecoder.verityVDF(voiceFile)

            // display a disclaimer when saving a system vdf
            if (isSystemVDF) {
                val accept = ButtonType(messages["accept_button"])
                val alert = Alert(AlertType.WARNING, messages["system_vdf_disclaimer_body"], accept, ButtonType.CANCEL)
                (alert.dialogPane.scene.window as Stage).icons.add(iconImage)
                alert.headerText = messages["system_vdf_disclaimer_title"]
                val result = alert.showAndWait()
                return result.isPresent && result.get() == accept
            }
        } catch (e: HoTTException) {
            ExceptionDialog.show(e)
            return false
        }

        return true
    }

    /**
     * Factory method to create new [VoiceDataListCell] objects.
     */
    private fun createListCell(): VoiceDataListCell {
        val cell = VoiceDataListCell(this)

        // setup DnD
        cell.setOnDragDetected { onDragDetected(it) }
        cell.setOnDragOver { onDragOver(it, cell) }
        cell.setOnDragEntered { onDragEntered(it) }
        cell.setOnDragExited { onDragExited(it) }
        cell.setOnDragDropped { onDragDropped(it) }
        cell.setOnDragDone { deleteTempFiles() }

        return cell
    }

    /**
     * Initialize this view
     */
    init {
        setStageIcon(iconImage)
        val bounds = Screen.getPrimary().visualBounds
        primaryStage.maxHeight = bounds.height
        primaryStage.maxWidth = bounds.width

        // setup listview
        with(listView) {
            // set factory method
            setCellFactory { createListCell() }

            // enable multi-select
            selectionModel.selectionMode = SelectionMode.MULTIPLE

            // clear selection if clicked on an empty row
            setOnMouseClicked { selectionModel.clearSelection() }

            // clear selection if ESC key was pressed
            setOnKeyPressed { if (it.code == KeyCode.ESCAPE) selectionModel.clearSelection() }

            // accept a drop into empty area of listview
            setOnDragOver { onDragOver(it, listView) }

            // perform drop actions on listview
            setOnDragDropped { onDragDropped(it) }
        }

        // add a change listener to the list to prevent invalid VDFs
        voiceFile.voiceList.onChange { onVoiceListChanged(it) }

        // fill restore menu
        addRestoreFiles()

        // do update check on startup
        if (!Util.OFFLINE) updateCheck(false)

        // ask to save modified VDF and terminate vm on window close
        primaryStage.setOnCloseRequest { ev -> ev.consume(); onClose() }

        val params = app.parameters.unnamed
        if (params.size > 0) {
            val file = File(params[0])
            if (isVDF(file)) open(file) else onNew()
        } else {
            // always start with an empty vdf
            onNew()
        }
    }

    private fun onVoiceListChanged(change: ListChangeListener.Change<out VoiceData>) {
        val list = voiceFile.voiceList

        while (!mute && change.next())
        // if an item was added, check if the size limit was reached
            if (change.wasAdded()) {
                val fistIndex = change.from
                var lastIndex = change.to

                // check for duplicate entries
                for (i in fistIndex..minOf(lastIndex, list.size - 1)) {
                    val voiceData1 = list[i]
                    val name1 = voiceData1.name

                    for (j in list.indices) {
                        if (j == i) continue

                        val voiceData2 = list[j]
                        val name2 = voiceData2.name

                        if (name1 == name2) {
                            undoBuffer.pop()
                            lastIndex--
                            MessageDialog.show(AlertType.ERROR, messages["duplicate_entry"], messages["duplicate_name"], name1)
                            break
                        }
                    }

                    // enforce name for system VDFs
                    if (isSystemVDF) {
                        val matcher = namePattern.matcher(name1)
                        val newName = if (matcher.matches())
                            String.format("%02d%s%s", i + 1, matcher.group(1), matcher.group(2))
                        else
                            String.format("%02d.%s", i + 1, name1)
                        voiceData1.name = newName
                    }
                }

                // remove added entries until validation succeeds
                while (true)
                    try {
                        // validate VDF
                        HoTTDecoder.verityVDF(voiceFile)

                        // validation succeeded - no exception was thrown
                        break
                    } catch (e: HoTTException) {
                        ExceptionDialog.show(e)

                        // validation failed, remove last added item and try again
                        undoBuffer.pop()
                        lastIndex--
                    }

            }

        setStageTitle()
    }

    /**
     * Show about dialog.
     */
    private fun onAbout() {
        MessageDialog(AlertType.INFORMATION, VDFEditor.title, true, messages["about_text"]).showAndWait()
    }

    /**
     * Show file selector to add a new sound file.
     */
    private fun onAddSound() {
        addSound(*selectSound(true))
    }

    /**
     * Show a dialog to create a announcement from text, using a online speech-to-text service (VoiceRSS)
     */
    private fun onAddSoundFromText() {
        speechDialog.openDialog().apply {
            setOnSucceeded {
                addSound(stream = value, name = text, volume = volume)
            }
        }
    }

    /**
     * Exit the application.
     */
    private fun onClose() {
        if (askSave()) {
            deleteTempFiles()
            Platform.exit()
        }
    }

    /**
     * Check that VDF is still valid after changing the country pcmd
     */
    private fun onCountryCodeChanged() {
        val oldCountry = voiceFile.country

        try {
            voiceFile.country = countryCodeCombo.value
            verify()
        } catch (e: HoTTException) {
            if (e.cause != null) ExceptionDialog.show(e)
            voiceFile.country = oldCountry
            Platform.runLater { countryCodeCombo.setValue(oldCountry) }
        }
    }

    /**
     * Delete the selected sounds.
     */
    private fun onDeleteSound() {
        val selectedIndices = selectionModel.selectedIndices

        if (isSystemVDF) {
            // keep number of items constant in system VDFs by replacing selected item(s) with an empty place holder
            selectedIndices.forEach { undoBuffer.push(ReplaceAction(it, VoiceData(String.format("%02d.%s", it + 1, messages["empty"]), ByteArray(0)))) }
        } else {
            // sort indices in reverse order and remove from high to low
            selectedIndices.reverse()
            selectedIndices.forEach { undoBuffer.push(RemoveAction(it)) }
        }

        // fix selection - select the item after the last deleted one
        val index = selectionModel.selectedIndex + 1
        val maxIndex = listView.items.size - 1
        selectionModel.clearSelection()
        selectionModel.select(Math.min(maxIndex, index))
    }

    /**
     * Handle DnD start event.
     *
     * Three drop targets are supported:
     *
     *  * **Drop on desktop**: Create temporary .wav files for any selected item and add the path to the clipboard.
     *  * **Another VDFEditor instance**: Serialize any selected item into the clipboard.
     *  * **other (e.g. text editor)**: Add the name of any selected item as comma separated list to the clipboard.
     */
    private fun onDragDetected(ev: MouseEvent) {
        deleteTempFiles()

        val content = ClipboardContent()

        // selected items
        val selectedItems = selectionModel.selectedItems

        // DnD to desktop: generate temporary .wav files for each selected item for DnD to desktop (export of .wav file)
        val pattern = "[^a-zA-Z0-9.-_]".toRegex()
        selectedItems.filter { it.rawData.isNotEmpty() }.forEach {
            try {
                val wav = File(System.getProperty("java.io.tmpdir"), it.name.replace(pattern, "_") + WAV)
                it.writeWav(wav)
                tempFiles.add(wav)
            } catch (e: IOException) {
                ExceptionDialog.show(e)
            }
        }
        content.putFiles(tempFiles)

        // VDFEditor: serialize selected items for DnD to other VDFEditor instance
        content[dndDataFormat] = selectedItems.toList()

        // other: item name for DnD to editor or text field
        content.putString(selectedItems.joinToString(",", transform = VoiceData::name))

        // add content to dragboard and start DnD
        (ev.source as Node).startDragAndDrop(*TransferMode.COPY_OR_MOVE).setContent(content)

        ev.consume()
    }

    /**
     * Handle Dnd drop event.
     *
     * There are three use cases:
     *
     *  * **DnD within the same list**: Copy (with CTRL) or move items within the list.
     *  * **DnD between VDFEditor instances**: Deserialize items from the clipboard.
     *  * **DnD from desktop**: import any sound or vdf files.
     */
    private fun onDragDropped(ev: DragEvent) {
        try {
            val dragboard = ev.dragboard
            val source = ev.gestureSource
            val target = ev.gestureTarget

            // if dropped on a list cell use it's index, otherwise the end of the list
            val targetIndex = (target as? VoiceDataListCell)?.index ?: listView.items.size

            if (source is VoiceDataListCell && target is VoiceDataListCell && source !== target) {
                // DnD within the same list

                // only for user VDFs
                if (!isSystemVDF) {
                    val sourceIndex = source.index

                    undoBuffer.push(MoveAction(sourceIndex, targetIndex))
                    selectionModel.clearSelection()
                }
            } else if (source == null) {
                // DnD from external source

                if (dragboard.hasContent(dndDataFormat)) {
                    // external source is another VDFEditor

                    @Suppress("UNCHECKED_CAST")
                    val list = dragboard.getContent(dndDataFormat) as List<VoiceData>

                    if (isSystemVDF)
                    // replace items beginning at target index with list items
                        for (i in list.indices)
                            undoBuffer.push(ReplaceAction(targetIndex + i, list[i]))
                    else
                    // insert all items at target index
                        list.forEach { item -> undoBuffer.push(InsertAction(targetIndex, item)) }

                } else if (ev.dragboard.hasFiles()) {
                    // DnD from desktop
                    val files = dragboard.files

                    // import first .vdf file
                    if (files.stream().allMatch(::isVDF)) open(files[0])

                    // import any sound files
                    if (files.stream().allMatch(::isSoundFormat))
                        if (isSystemVDF)
                        // replace items beginning at target index with files
                            for (i in files.indices)
                                undoBuffer.push(ReplaceAction(targetIndex + i, VoiceData.forFile(files[i])))
                        else
                            files.stream().map({ VoiceData.forFile(it) }).forEach { undoBuffer.push(InsertAction(targetIndex, it)) }
                }
            }
        } catch (e: RuntimeException) {
            ExceptionDialog.show(e)
        }

        ev.isDropCompleted = true
        ev.consume()
    }

    /**
     * Handle DnD event when the mouse pointer enters a node.
     */
    private fun onDragEntered(ev: DragEvent) {
        if ((!isSystemVDF || ev.gestureSource == null) && ev.target != ev.gestureSource)
            (ev.target as Node).opacity = 0.3
        ev.consume()
    }

    /**
     * Handle DnD event when the mouse pointer leaves a node.
     */
    private fun onDragExited(ev: DragEvent) {
        (ev.target as Node).opacity = 1.0
        ev.consume()
    }

    /**
     * Report supported transfer modes for the node under the mouse pointer.
     *
     * @param ev
     * The event.
     * @param target
     * The node that triggered the event. Note, that we can't use [DragEvent.getGestureTarget] here, because the drag is still in progress
     * and [DragEvent.getGestureTarget] would return null. We also can't use [DragEvent.getTarget], as this could be a cild of the
     * node triggering the event (e.g. an internal label).
     */
    private fun onDragOver(ev: DragEvent, target: Node) {
        val dragboard = ev.dragboard
        val itemCount = listView.items.size
        val source = ev.gestureSource
        val targetIndex = (target as? VoiceDataListCell)?.index ?: itemCount

        if (source is VoiceDataListCell) {
            // DnD within the same list

            if (!isSystemVDF && target is VoiceDataListCell && source !== target) {
                // allow only for user VDFs and if source != target
                ev.acceptTransferModes(TransferMode.MOVE)
            }
        } else if (source == null)
        // DnD from external source

            if (dragboard.hasContent(dndDataFormat)) {
                // external source is another VDFEditor

                if (isSystemVDF) {
                    // allow multiple items to replace an existing items for system VDFs if they fit in the list
                    @Suppress("UNCHECKED_CAST")
                    val list = dragboard.getContent(dndDataFormat) as List<VoiceData>
                    if (targetIndex + list.size <= itemCount) ev.acceptTransferModes(TransferMode.COPY)
                } else {
                    // allow multiple items for user VDFs
                    ev.acceptTransferModes(TransferMode.COPY)
                }
            } else if (dragboard.hasFiles()) {
                // DnD from desktop
                val files = dragboard.files

                if (files.stream().allMatch(::isVDF) && files.size == 1)
                // only one VDF file was dragged
                    ev.acceptTransferModes(TransferMode.COPY)
                else if (files.stream().allMatch(::isSoundFormat))
                // only sound files were dragged
                    if (isSystemVDF) {
                        if (targetIndex + files.size <= itemCount)
                        // allow only multiple sound files to replace existing sounds for system VDFs it they fit in the existing list
                            ev.acceptTransferModes(TransferMode.COPY)
                    } else
                    // allow multiple sound files for user VDFs
                        ev.acceptTransferModes(TransferMode.COPY)
            }

        ev.consume()
    }

    /**
     * Load system VDF from transmitter
     */
    private fun onLoadSystemVoiceFile() {
        if (askSave()) {
            LoadVoiceFileTask(messages["load_system_voicefiles"], false).apply {
                transmitterDialogView.openDialog(this)
                success { voiceFile ->
                    this@MainView.serialPort = serialPort
                    if (voiceFile != null) open(voiceFile)
                }
            }
        }
    }

    /**
     * Load user VDF from transmitter
     */
    private fun onLoadUserVoiceFile() {
        if (askSave()) {
            LoadVoiceFileTask(messages["load_user_voicefiles"], true).apply {
                transmitterDialogView.openDialog(this)
                success { voiceFile ->
                    this@MainView.serialPort = serialPort
                    if (voiceFile != null) open(voiceFile)
                }
            }
        }
    }

    /**
     * Move the selected item down in the list.
     */
    private fun onMoveDown() {
        if (!isSystemVDF) {
            val index = selectedIndex
            undoBuffer.push(MoveDownAction(index))
            selectionModel.clearSelection()
            selectionModel.select(index + 1)
        }
    }

    /**
     * Move the selected item up in the list.
     */
    private fun onMoveUp() {
        if (!isSystemVDF) {
            val index = selectedIndex
            undoBuffer.push(MoveUpAction(index))
            selectionModel.clearSelection()
            selectionModel.select(index - 1)
        }
    }

    /**
     * Create a new empty VDF. Ask to save the old VDF if it was modified.
     */
    private fun onNew() {
        if (askSave()) {
            voiceFile.voiceList.clear()
            voiceFile.vdfType = VDFType.User
            voiceFile.transmitterType = TransmitterType.mc28
            voiceFile.vdfVersion = 3000
            vdfFileProperty.set(null)
            open(voiceFile)
        }
    }

    /**
     * Show file selector to open a VDF.
     *
     * The method will remember the directory being used.
     */
    private fun onOpen() {
        if (askSave()) {
            val chooser = FileChooser()
            chooser.title = messages["open_vdf"]

            // use dir from preferences
            preferences {
                val dir = File(get(LAST_LOAD_VDF_DIR, userHome))
                if (dir.exists() && dir.isDirectory) chooser.initialDirectory = dir
            }

            // setup file name filter
            chooser.extensionFilters.add(ExtensionFilter(messages["vdf_files"], EXT_VDF))

            val vdf = chooser.showOpenDialog(primaryStage)
            if (vdf != null) {
                // store dir in preferences
                preferences {
                    put(LAST_LOAD_VDF_DIR, vdf.parentFile.absolutePath)
                }
                open(vdf)
            }
        }
    }

    /**
     * Playback the sound of the selected item.
     */
    private fun onPlay() {
        selectionModel.selectedItems.forEach(VoiceData::play)
    }

    private fun onPlayOnTransmitter() {
        var offset = 0

        if (!isSystemVDF) {
            // temporarily switch to system vdf
            voiceFile.vdfType = VDFType.System
            offset = HoTTDecoder.getMaxVoiceCount(voiceFile)
            voiceFile.vdfType = VDFType.User
        }

        serialPort?.playSound(selectedIndex + offset)
    }

    /**
     * Generate a PDF report for the current VDF
     */
    private fun onPrint() {
        val chooser = FileChooser()
        chooser.title = messages["print"]

        // use dir from preferences
        preferences {
            val dir = File(get(LAST_SAVE_VDF_DIR, userHome))
            if (dir.exists() && dir.isDirectory) chooser.initialDirectory = dir
        }

        // setup file name filter
        chooser.extensionFilters.add(ExtensionFilter(messages["pdf_files"], EXT_PDF))

        // preset report name
        if (vdfFile != null) {
            val fileName = vdfFile.name.replace(VDF, PDF)
            chooser.initialFileName = fileName
        }

        val pdf = chooser.showSaveDialog(primaryStage)
        if (pdf != null) {
            val name = vdfFile?.name ?: ""
            val title = "${voiceFile.vdfType} VDF V${voiceFile.vdfVersion / 1000.0f}"
            val version = VDFEditor.title
            val html = HTMLReport.generateHTML(name, title, version, voiceFile)
            PDFReport.save(pdf, html)
            Desktop.getDesktop().open(pdf)
        }
    }

    /**
     * Redo the last undone change
     */
    private fun onRedo() {
        undoBuffer.redo()
        listView.refresh()
        setStageTitle()
    }

    /**
     * Rename the selected item.
     */
    private fun onRename() {
        listView.edit(selectedIndex)
    }

    /**
     * Replace the selected item.
     */
    private fun onReplaceSound() {
        replaceSound(selectSound(false)[0])
    }

    /**
     * Replace the selected item with a generated sound file
     */
    private fun onReplaceSoundFromText() {
        speechDialog.openDialog().apply {
            setOnSucceeded {
                replaceSound(stream = value, name = text, volume = volume)
            }
        }
    }

    /**
     * Show dialog to save the currentAction VDF.
     */
    private fun onSaveVDF(): Boolean = isDirty and checkSizeBeforeSave() and save(vdfFile)

    /**
     * Show dialog to save the currentAction VDF under a new name.
     *
     * This method will remember the directory being used.
     */
    private fun onSaveVDFAs(): Boolean {
        if (!checkSizeBeforeSave()) return false

        val chooser = FileChooser()
        chooser.title = messages["save_vdf"]

        // use dir from preferences
        preferences {
            val dir = File(get(LAST_SAVE_VDF_DIR, userHome))
            if (dir.exists() && dir.isDirectory) chooser.initialDirectory = dir
        }

        // setup file name filter
        chooser.extensionFilters.add(ExtensionFilter(messages["vdf_files"], EXT_VDF))

        val vdf = chooser.showSaveDialog(listView.scene.window)
        return save(vdf)
    }

    /**
     * Handle event for change in transmitter type.
     */
    private fun onTransmitterTypeChanged() {
        val newTransmitterType = transmitterTypeCombo.value ?: return

        val oldTransmitterType = voiceFile.transmitterType
        val oldVdfVersion = voiceFile.vdfVersion

        if (oldTransmitterType != newTransmitterType)
            try {
                voiceFile.transmitterType = newTransmitterType
                updateVdfVersion()
                verify()
                setStageTitle()
                undoBuffer.clear()
            } catch (e: Exception) {
                if (e.cause != null) ExceptionDialog.show(e)
                voiceFile.transmitterType = oldTransmitterType
                voiceFile.vdfVersion = oldVdfVersion
                Platform.runLater { transmitterTypeCombo.setValue(oldTransmitterType) }
            }

    }

    /**
     * Undo last change
     */
    private fun onUndo() {
        undoBuffer.undo()
        listView.refresh()
        setStageTitle()
    }

    /**
     * Check for new version
     */
    private fun onUpdateCheck() {
        updateCheck(true)
    }

    /**
     * Show user manual
     */
    private fun onUserManual() {
        val inputStream: InputStream = try {
            // try translated manual first
            resources.stream("VDFEditor_${System.getProperty("user.language").toUpperCase()}.pdf")
        } catch (e: IllegalStateException) {
            // fall-back to English
            resources.stream("VDFEditor_EN.pdf")
        }

        val tempFile = File(System.getProperty("java.io.tmpdir"), "VDFEditor.pdf")
        tempFile.deleteOnExit()
        FileOutputStream(tempFile).use { IOUtils.copy(inputStream, it) }
        Desktop.getDesktop().open(tempFile)
    }

    /**
     * Handle changes of VDF version
     */
    private fun onVdfVersionChanged() {
        val vdfVersion = vdfVersionCombo.value ?: return

        val oldVdfVersion = voiceFile.vdfVersion
        val newVdfVersion = (vdfVersion * 1000).toInt()

        if (oldVdfVersion != newVdfVersion)
            try {
                voiceFile.vdfVersion = newVdfVersion
                updateVdfVersion()
                verify()
                setStageTitle()
                undoBuffer.clear()
            } catch (e: HoTTException) {
                if (e.cause != null) ExceptionDialog.show(e)
                voiceFile.vdfVersion = oldVdfVersion
                Platform.runLater { updateVdfVersion() }
            }
    }

    /**
     * Upload VDF to transmitter
     */
    private fun onWriteToTransmitter() {
        SendVoiceFileTask(messages["write_to_transmitter"], voiceFile).apply {
            transmitterDialogView.openDialog(this)
            success {
                this@MainView.serialPort = serialPort
            }
        }
    }

    /**
     * Load a new VDF from file. Ask for save if currentAction VDF was modified.
     */
    private fun open(vdf: File) {
        if (askSave())
            try {
                open(HoTTDecoder.decodeVDF(vdf))
                vdfFileProperty.set(vdf)
            } catch (e: IOException) {
                ExceptionDialog.show(e)
            }
    }

    /**
     * Load a new VDF from data model.
     */
    private fun open(other: VoiceFile) {
        mute {
            // don't complain about invalid VDFs - will be fixed later with verify()
            voiceFile.copy(other)
        }

        transmitterTypeCombo.value = voiceFile.transmitterType
        countryCodeCombo.value = voiceFile.country
        updateVdfVersion()

        undoBuffer.clear()
        voiceFile.clean()
        verify(false)
        setStageTitle()
    }

    /**
     * Replace selected sound from file
     */
    private fun replaceSound(file: File?, volume: Double = 1.0) {
        if (file != null && file.exists()) {
            undoBuffer.push(ReplaceAction(selectedIndex, VoiceData.forFile(file, volume)))
        }
    }

    /**
     * Replace selected sound from stream
     */
    private fun replaceSound(stream: AudioInputStream, name: String, volume: Double = 1.0) {
        undoBuffer.push(ReplaceAction(selectedIndex, VoiceData.forStream(stream, name, volume)))
    }

    /**
     * Save current VDF to file
     */
    private fun save(vdf: File?): Boolean {
        if (vdf != null) {
            // store dir in preferences
            preferences { put(LAST_SAVE_VDF_DIR, vdf.parentFile.absolutePath) }

            try {
                HoTTDecoder.encodeVDF(voiceFile, vdf)
                vdfFileProperty.set(vdf)
                voiceFile.clean()
                setStageTitle()

                // everything ok
                return true
            } catch (e: IOException) {
                ExceptionDialog.show(e)
            }
        }

        // something went wrong
        return false
    }

    /**
     * Select sound files for import/replace
     */
    private fun selectSound(multiSelect: Boolean): Array<File> {
        val chooser = FileChooser()
        chooser.title = messages["load_sound"]

        // use dir from preferences
        preferences {
            val dir = File(get(LAST_LOAD_SOUND_DIR, userHome))
            if (dir.exists() && dir.isDirectory) chooser.initialDirectory = dir
        }

        // setup file name filter
        chooser.extensionFilters.add(ExtensionFilter(messages["sound_files"], EXT_WAV, EXT_MP3, EXT_OGG))
        chooser.extensionFilters.add(ExtensionFilter(messages["wav_files"], EXT_WAV))
        chooser.extensionFilters.add(ExtensionFilter(messages["mp3_files"], EXT_MP3))
        chooser.extensionFilters.add(ExtensionFilter(messages["ogg_files"], EXT_OGG))

        if (multiSelect) {
            val files: List<File>? = chooser.showOpenMultipleDialog(primaryStage)
            if (files != null)
                return files.toTypedArray()
        } else {
            val file: File? = chooser.showOpenDialog(primaryStage)
            if (file != null)
                return arrayOf(file)
        }

        // return empty array
        return arrayOf()
    }

    /**
     * Set stage title with information about the current VDF.
     */
    private fun setStageTitle() {
        val sb = StringBuilder()

        if (isDirty) sb.append("*")

        if (vdfFile == null)
            sb.append(messages["empty"])
        else
            sb.append(vdfFile.name)

        try {
            val maxDataSize = HoTTDecoder.getMaxDataSize(voiceFile)
            val dataSize = voiceFile.rawDataSize

            sb.append(String.format(" - %d kb / %d kb (%d%%) - %s VDF V%s", dataSize / 1024, maxDataSize / 1024, dataSize * 100 / maxDataSize,
                    voiceFile.vdfType, voiceFile.vdfVersion / 1000.0f))
        } catch (e: HoTTException) {
            ExceptionDialog.show(e)
        }

        title = sb.toString()
    }

    /**
     * Check for new versions
     */
    private fun updateCheck(verbose: Boolean) {
        // skipp if offline
        if (Util.OFFLINE) return

        val currentVersion = VDFEditor.version
        val latestVersion = Util.getLatestVersion("VDFEditor")

        if (currentVersion == Messages.getString("Launcher.Unknown")) {
            if (verbose) MessageDialog.show(AlertType.WARNING, messages["unknown_version"], messages["unknown_version_text"])
        } else if (latestVersion == null) {
            if (verbose) MessageDialog.show(AlertType.WARNING, messages["offline"], messages["offline_text"])
        } else if (latestVersion == currentVersion) {
            if (verbose) MessageDialog.show(AlertType.INFORMATION, messages["uptodate"], messages["uptodate_text"])
        } else
            MessageDialog.show(AlertType.INFORMATION, messages["update_available"], true, messages["update_available_text"], latestVersion)
    }

    /**
     * Set the VDF version depending on transmitter type and VDF type
     */
    private fun updateVdfVersion() {
        val items = vdfVersionCombo.items
        val transmitterType = voiceFile.transmitterType
        val version: Int

        when (transmitterType) {
            // V3 only transmitters
            TransmitterType.mc26, TransmitterType.mc28 -> {
                version = 3000

                // add missing values
                if (!items.contains(3.0f)) items.add(3.0f)

                // remove invalid values
                items.remove(2.0f)
                items.remove(2.5f)
            }

            // V2 only transmitters
            TransmitterType.mz12, TransmitterType.mz18, TransmitterType.mz24, TransmitterType.x8e, TransmitterType.x8n -> {
                version = 2000

                // add missing values
                if (!items.contains(2.0f)) items.add(2.0f)

                // remove invalid values
                items.remove(2.5f)
                items.remove(3.0f)
            }

            // V2 or V2.5 transmitters
            // mc16, mc20, mc32, mx12, mx16, mx20, mz12Pro, mz24Pro
            else ->
                when (voiceFile.vdfType) {
                    VDFType.System -> {
                        // change 3.0 to 2.5 or keep existing version
                        version = if (voiceFile.vdfVersion == 3000) 2500 else voiceFile.vdfVersion

                        // add missing values
                        if (!items.contains(2.0f)) items.add(2.0f)
                        if (!items.contains(2.5f)) items.add(2.5f)

                        // remove invalid values
                        items.remove(3.0f)
                    }

                    // User VDF
                    else -> {
                        version = 2500

                        // add missing values
                        if (!items.contains(2.5f)) items.add(2.5f)

                        // remove invalid values
                        items.remove(2.0f)
                        items.remove(3.0f)
                    }
                }
        }

        voiceFile.vdfVersion = version
        vdfVersionCombo.value = voiceFile.vdfVersion / 1000f
    }

    /**
     * Verify the voice count of the VDF depending on transmitter type and VDF type
     *
     * @param showDialog Show a confirmation dialog if entries are to be deleted.
     */
    private fun verify(showDialog: Boolean = true) {
        val maxVoiceCount = HoTTDecoder.getMaxVoiceCount(voiceFile)
        var voiceCount = voiceFile.size
        val voiceData = listView.items

        // show confirmation dialog and throw exception if used does not agree
        if (voiceCount > maxVoiceCount && showDialog && MessageDialog(AlertType.CONFIRMATION, messages["delete_entries_header"],
                        messages["delete_entries_message"], voiceCount - maxVoiceCount).showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK)
            throw HoTTException()

        // do not show messages while cleaning up
        mute {
            // remove extra items
            while (voiceCount-- > maxVoiceCount)
                voiceData.removeAt(voiceCount)

            // add empty entries for system VDFs
            if (isSystemVDF)
                while (++voiceCount < maxVoiceCount)
                    voiceData.add(VoiceData(String.format("%02d.%s", voiceCount + 1, messages["empty"]), ByteArray(0)))
        }

        HoTTDecoder.verityVDF(voiceFile)
    }

    // run some code while muted (i.e. suppress consistency checks)
    @Synchronized
    private fun mute(op: () -> Unit) {
        if (mute)
        // already muted
            op()
        else {
            mute = true
            op()
            mute = false
        }
    }
}
