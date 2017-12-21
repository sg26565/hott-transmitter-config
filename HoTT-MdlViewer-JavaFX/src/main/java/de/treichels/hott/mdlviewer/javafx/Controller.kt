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

import de.treichels.decoder.HoTTDecoder
import de.treichels.hott.model.voice.Announcements
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.web.WebView
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import kotlinx.coroutines.experimental.async
import org.apache.commons.io.FilenameUtils
import tornadofx.*
import java.io.File
import java.io.FileWriter

private const val LAST_LOAD_DIR = "lastLoadDir"
private const val LAST_SAVE_DIR = "lastSaveDir"

class Controller : View() {
    private val selectFromMemory by inject<SelectFromMemory>()
    private val selectFromSDCard by inject<SelectFromSdCard>()
    private var contextMenu by singleAssign<ContextMenu>()
    private var webView by singleAssign<WebView>()
    private val modelProperty = SimpleObjectProperty<Model?>(null)
    private var model by modelProperty

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
                        action { refreshModel() }
                    }
                    separator()
                    item(messages["exit"], "Alt+F4") {
                        action { exit() }
                    }
                }

                contextmenu {
                    this@Controller.contextMenu = this
                    item(messages["load_form_file"]) { action { loadFromFile() } }
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
                        action { refreshModel() }
                    }
                    separator()
                    item(messages["exit"]) { action { exit() } }
                }
            }
        }

        center {
            webView = webview {
                setOnMouseClicked { mouseClicked(it) }
                isContextMenuEnabled = false
            }
        }

    }

    private fun disableUI(readOnlyBooleanProperty: ReadOnlyBooleanProperty) {
        primaryStage.scene.cursorProperty().bind(Bindings.`when`(readOnlyBooleanProperty).then(Cursor.WAIT).otherwise(Cursor.DEFAULT))
        root.disableProperty().bind(readOnlyBooleanProperty)
    }

    init {
        log.info(this.javaClass.name)

        val params = app.parameters.unnamed
        if (params != null && params.size > 0 && params[0].endsWith(".mdl")) {
            Platform.runLater { load(File(params[0])) }
        }

        primaryStage.icons.add(Image(javaClass.getResource("/icon.png").toString()))
        primaryStage.title = messages["title"]// TODO, System.getProperty(MdlViewer.version))
    }

    private fun exit() {
        Platform.exit()
    }

    private fun load(file: File) {
        preferences { put(LAST_LOAD_DIR, file.parentFile.absolutePath) }
        model = loadModel(file)
        refreshModel()
    }

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

    private fun loadFromMemory() {
        selectFromMemory.openDialog()
        selectFromMemory.getResult()?.apply {
            disableUI(runningProperty())
            success {
                modelProperty.set(value)
                refreshModel()
            }
        }
    }

    private fun loadFromSdCard() {
        selectFromSDCard.openDialog()
        selectFromSDCard.getResult()?.apply {
            disableUI(runningProperty())
            success {
                modelProperty.set(value)
                refreshModel()
            }
        }
    }

    private fun loadSystemVDF() {
        FileChooser().apply {
            title = messages["load_system_vdf"]
            preferences {
                val dir = File(get(LAST_SAVE_DIR, MdlViewer.programDir))
                if (dir.exists() && dir.isDirectory) initialDirectory = dir
            }
            extensionFilters.add(ExtensionFilter(messages["vdf_file"], "*.vdf"))
        }.showOpenDialog(primaryStage)?.apply {
            preferences { put(LAST_LOAD_DIR, parentFile.absolutePath) }
            val vdfFile = HoTTDecoder.decodeVDF(this)
            val names = vdfFile.voiceList.map { it.name }.toList()
            Announcements.saveSystemPrefs(model!!.model.transmitterType, names)
            refreshModel()
        }
    }

    private fun loadUserVDF() {
        FileChooser().apply {
            title = messages["load_user_vdf"]
            preferences {
                val dir = File(get(LAST_SAVE_DIR, MdlViewer.programDir))
                if (dir.exists() && dir.isDirectory) initialDirectory = dir
            }
            extensionFilters.add(ExtensionFilter(messages["vdf_file"], "*.vdf"))
        }.showOpenDialog(primaryStage)?.apply {
            preferences { put(LAST_LOAD_DIR, parentFile.absolutePath) }
            val vdfFile = HoTTDecoder.decodeVDF(this)
            val names = vdfFile.voiceList.map { it.name }.toList()
            Announcements.saveUserPrefs(model!!.model.transmitterType, names)
            refreshModel()
        }
    }

    private fun mouseClicked(e: MouseEvent) {
        if (e.button == MouseButton.SECONDARY)
            contextMenu.show(webView, e.screenX, e.screenY)
        else
            contextMenu.hide()
    }

    private fun refreshModel() {
        val m = model
        if (m != null) {
            val tempFile = File.createTempFile("Model", ".html")
            tempFile.deleteOnExit()

            val task = webView.runAsyncWithOverlay {
                FileWriter(tempFile).use { it.write(m.html) }
            } ui {
                webView.engine.load("file:/${tempFile.absolutePath}")
                runLater { tempFile.delete() }
            }

            disableUI(task.runningProperty())
        }
    }

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

            async {
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
