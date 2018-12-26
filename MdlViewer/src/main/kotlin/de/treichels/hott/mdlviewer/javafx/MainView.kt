/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.mdlviewer.javafx

import de.treichels.hott.mdlviewer.javafx.Model.Companion.loadModel
import de.treichels.hott.voice.Announcements
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.concurrent.Task
import javafx.concurrent.Worker
import javafx.scene.Cursor
import javafx.scene.control.CheckBox
import javafx.scene.control.ContextMenu
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.web.WebView
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import org.apache.commons.io.FilenameUtils
import tornadofx.*
import java.io.File
import java.io.FileWriter
import java.util.concurrent.Callable

class MainView : View() {
    companion object {
        // Constants
        private const val LAST_LOAD_DIR = "lastLoadDir"
        private const val LAST_SAVE_DIR = "lastSaveDir"
        private const val LAST_VDF_DIR = "lastVdfDir"
    }

    // Dialogs
    private val selectFromMemory by inject<SelectFromMemory>()
    private val selectFromSDCard by inject<SelectFromSdCard>()

    // Controls
    private var contextMenu by singleAssign<ContextMenu>()
    private var webView by singleAssign<WebView>()

    // Current model
    private val showHiddenMenus = SimpleBooleanProperty(false)
    private val modelProperty = SimpleObjectProperty<Model?>(null)
    private var model
        get() = modelProperty.get()
        set(model) {
            modelProperty.set(model)
            refresh()
        }

    // Misc.
    private lateinit var tempFile: File

    // UI
    override val root = borderpane {
        prefWidth = 800.0
        prefHeight = 600.0

        top {
            menubar {
                menu(messages["file"]) {
                    item(messages["load_from_file"], "Shortcut+F1") {
                        action { loadFromFile() }
                    }
                    item(messages["load_from_memory"], "Shortcut+F2") {
                        action { loadFromMemory() }
                    }
                    item(messages["load_from_sdcard"], "Shortcut+F3") {
                        action { loadFromSdCard() }
                    }
                    separator()
                    item(messages["load_system_vdf"]) {
                        disableWhen { modelProperty.isNull }
                        action { loadSystemVDF() }
                    }
                    item(messages["load_user_vdf"]) {
                        disableWhen { modelProperty.isNull }
                        action { loadUserVDF() }
                    }
                    separator()
                    item(messages["save"], "Shortcut+S") {
                        disableWhen { modelProperty.isNull }
                        action { saveFile() }
                    }
                    separator()
                    item(messages["refresh"], "F5") {
                        disableWhen { modelProperty.isNull }
                        action { refresh() }
                    }
                    separator()
                    item(messages["exit"], "Alt+F4") {
                        action { exit() }
                    }
                }

                menu(messages["view"]) {
                    item(messages["show_hidden_menus"]) {
                        checkbox {
                            showHiddenMenus.bindBidirectional(selectedProperty())
                            preferences {
                                isSelected = get("showHiddenMenus", "false")!!.toBoolean()
                            }
                            action {
                                preferences {
                                    put("showHiddenMenus", isSelected.toString())
                                }
                                refresh()
                            }
                        }
                    }
                }
            }
        }

        center {
            webView = webview {
                // delete temp file after loding
                engine.loadWorker.stateProperty().addListener { _, _, value ->
                    if (value == Worker.State.SUCCEEDED)
                        tempFile.delete()
                }
                setOnMouseClicked { mouseClicked(it) }
                contextMenu = contextmenu {
                    item(messages["load_from_file"]) { action { loadFromFile() } }
                    item(messages["load_from_memory"]) { action { loadFromMemory() } }
                    item(messages["load_from_sdcard"]) { action { loadFromSdCard() } }
                    separator()
                    item(messages["load_system_vdf"]) {
                        disableWhen { modelProperty.isNull }
                        action { loadSystemVDF() }
                    }
                    item(messages["load_user_vdf"]) {
                        disableWhen { modelProperty.isNull }
                        action { loadUserVDF() }
                    }
                    separator()
                    item(messages["save"]) {
                        disableWhen { modelProperty.isNull }
                        action { saveFile() }
                    }
                    separator()
                    item(messages["refresh"]) {
                        disableWhen { modelProperty.isNull }
                        action { refresh() }
                    }
                    separator()
                    item(messages["exit"]) { action { exit() } }
                }
                isContextMenuEnabled = false
            }
        }

    }

    /**
     * Disable UI while some background [Task] is running. Pass the runningProperty of the [Task] as parameter.
     */
    private fun disableUI(readOnlyBooleanProperty: ReadOnlyBooleanProperty) {
        primaryStage.scene.cursorProperty().bind(Bindings.`when`(readOnlyBooleanProperty).then(Cursor.WAIT).otherwise(Cursor.DEFAULT))
        root.disableProperty().bind(readOnlyBooleanProperty)
    }

    init {
        // load mdl file passed as first parameter (e.g. when associating .mdl file extension with this program)
        val params = app.parameters.unnamed
        if (params != null && params.size > 0 && params[0].endsWith(".mdl")) {
            Platform.runLater { load(File(params[0])) }
        }

        title = "${messages["title"]} ${MdlViewer.version}"
        setStageIcon(resources.image("icon.png"))
    }

    /**
     * Close the program and shutdown the [Platform]
     */
    private fun exit() {
        Platform.exit()
    }

    /**
     * Load a [Model] from the specified [File]
     */
    private fun load(file: File) {
        preferences { put(LAST_LOAD_DIR, file.parentFile.absolutePath) }
        model = loadModel(file)
    }

    /**
     * Show file open dialog and load a [Model] from the selected [File]
     */
    private fun loadFromFile() {
        FileChooser().apply {
            title = messages["load_from_file"]
            preferences {
                val dir = File(get(LAST_LOAD_DIR, MdlViewer.programDir))
                if (dir.exists() && dir.isDirectory) initialDirectory = dir
            }
            extensionFilters.add(ExtensionFilter(messages["mdl_file"], "*.mdl"))
        }.showOpenDialog(primaryStage)?.apply {
            load(this)
        }
    }

    /**
     * Show [SelectFromMemory] dialog and load a [Model] from the selected entry.
     */
    private fun loadFromMemory() {
        selectFromMemory.openDialog()?.apply { refresh(this) }
    }

    /**
     * Show [SelectFromSdCard] dialog and load a [Model] from the selected entry.
     */
    private fun loadFromSdCard() {
        selectFromSDCard.openDialog()?.apply { refresh(this) }
    }

    /**
     * Show file open dialog and load the selected system VDF. The information is later used to display the correct names for system announcements.
     */
    private fun loadSystemVDF() {
        FileChooser().apply {
            title = messages["load_system_vdf"]
            preferences {
                val dir = File(get(LAST_VDF_DIR, MdlViewer.programDir))
                if (dir.exists() && dir.isDirectory) initialDirectory = dir
            }
            extensionFilters.add(ExtensionFilter(messages["vdf_file"], "*.vdf"))
        }.showOpenDialog(primaryStage)?.apply {
            preferences { put(LAST_VDF_DIR, parentFile.absolutePath) }
            val vdfFile = de.treichels.hott.decoder.HoTTDecoder.decodeVDF(this)
            val names = vdfFile.voiceList.map { it.name }.toList()
            Announcements.saveSystemPrefs(model!!.model.transmitterType, names)
            refresh()
        }
    }

    /**
     * Show file open dialog and load the selected user VDF. The information is later used to display the correct names for user announcements.
     */
    private fun loadUserVDF() {
        FileChooser().apply {
            title = messages["load_user_vdf"]
            preferences {
                val dir = File(get(LAST_VDF_DIR, MdlViewer.programDir))
                if (dir.exists() && dir.isDirectory) initialDirectory = dir
            }
            extensionFilters.add(ExtensionFilter(messages["vdf_file"], "*.vdf"))
        }.showOpenDialog(primaryStage)?.apply {
            preferences { put(LAST_VDF_DIR, parentFile.absolutePath) }
            val vdfFile = de.treichels.hott.decoder.HoTTDecoder.decodeVDF(this)
            val names = vdfFile.voiceList.map { it.name }.toList()
            Announcements.saveUserPrefs(model!!.model.transmitterType, names)
            refresh()
        }
    }

    /**
     * Handle left mouse button clicks to show or hide the context menu. As of JavaFX 8, context menu handling is broken in [WebView].
     */
    private fun mouseClicked(e: MouseEvent) {
        if (e.button == MouseButton.SECONDARY)
            contextMenu.show(webView, e.screenX, e.screenY)
        else
            contextMenu.hide()
    }

    /**
     * Refresh [WebView] with current [Model]
     */
    private fun refresh() {
        val model = this.model
        if (model != null) {
            model.model.showHiddenMenus = showHiddenMenus.value
            refresh(model)
        }
    }

    /**
     * Refresh [WebView] with specified [Model]
     */
    private fun refresh(model: Model) {
        if (this::tempFile.isInitialized && tempFile.exists()) tempFile.delete()

        tempFile = File.createTempFile("Model", ".html")
        tempFile.deleteOnExit()

        webView.runAsyncWithOverlay {
            FileWriter(tempFile).use { it.write(model.html) }
        }.apply {
            disableUI(runningProperty())
        }.ui {
            webView.engine.load("file:/${tempFile.absolutePath}")
        }
    }

    /**
     * Refresh [WebView] with the result of the specified [Callable]
     */
    private fun refresh(result: Callable<Model>) {
        webView.runAsyncWithOverlay {
            result.call()
        }.apply {
            disableUI(runningProperty())
        }.success {
            model = it
        }
    }

    /**
     * Show file save dialog and save the current model to the selected file in the specified format.
     */
    private fun saveFile() {
        FileChooser().apply {
            title = messages["save"]
            preferences {
                val dir = File(get(LAST_SAVE_DIR, MdlViewer.programDir))
                if (dir.exists() && dir.isDirectory) initialDirectory = dir
            }
            initialFileName = model?.fileName
            extensionFilters.add(ExtensionFilter(messages["html_file"], "*.html"))
            extensionFilters.add(ExtensionFilter(messages["pdf_file"], "*.pdf"))
            extensionFilters.add(ExtensionFilter(messages["xml_file"], "*.xml"))
            extensionFilters.add(ExtensionFilter(messages["mdl_file"], "*.mdl"))
            extensionFilters.add(ExtensionFilter(messages["txt_file"], "*.txt"))
        }.showSaveDialog(primaryStage)?.apply {
            preferences { put(LAST_SAVE_DIR, parentFile.absolutePath) }
            val extension = FilenameUtils.getExtension(name).toLowerCase()

            runAsync {
                when (extension) {
                    "html" -> model?.saveHtml(this@apply)

                    "pdf" -> model?.savePdf(this@apply)

                    "xml" -> model?.saveXml(this@apply)

                    "mdl" -> model?.saveMdl(this@apply)

                    "txt" -> model?.saveTxt(this@apply)

                    else -> {
                    }
                }
            }
        }
    }
}
