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

import de.treichels.decoder.HoTTSerialPort
import de.treichels.hott.model.serial.FileInfo
import de.treichels.hott.model.serial.FileType
import javafx.scene.control.TreeItem
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import tornadofx.*
import tornadofx.FX.Companion.log
import tornadofx.FX.Companion.messages

private fun image(fileName: String): Image = Image(TreeFileInfo::class.java.getResource(fileName).toString())

private val fileImage = image("/File.gif")
private val openFolderImage = image("/Folder_open.gif")
private val closedFolderImage = image("/Folder_closed.gif")
private val rootFolderImage = image("/Root.gif")

class TreeFileInfo(value: String, port: HoTTSerialPort? = null, internal val fileInfo: FileInfo? = null) : TreeItem<String>(value) {
    // secondary constructor - retrieve value form fileInfo
    internal constructor(info: FileInfo) : this(info.name, fileInfo = info)

    // helper
    private val isRoot = fileInfo == null
    private val isDir = fileInfo?.type == FileType.Dir
    private val isFile = fileInfo?.type == FileType.File

    private val serialPort: HoTTSerialPort = port ?: getRoot().serialPort

    init {
        isExpanded = false

        // update treeitem children on expand
        expandedProperty().addListener { _ -> update() }

        when {
            isRoot -> {
                graphic = ImageView(rootFolderImage)
                isExpanded = true
            }

            isDir -> {
                graphic = ImageView(closedFolderImage)
            }

            isFile -> {
                graphic = ImageView(fileImage)
            }
        }
    }

    private fun getRoot(): TreeFileInfo {
        val p: TreeFileInfo? = parent as? TreeFileInfo
        return p?.getRoot() ?: this
    }

    private fun loading() {
        // add loading pseudo child
        children.clear()
        children.add(TreeItem(messages["loading"]))
    }

    /**
     * Update children of this tree item in a background task.
     */
    private fun update() {
        when {
            isRoot -> {
                if (isExpanded) {
                    loading()
                    loadFromSdCard("/")
                }
            }

            isDir -> {
                if (isExpanded) {
                    graphic = ImageView(openFolderImage)
                    loading()
                    loadFromSdCard(fileInfo!!.path)
                } else {
                    graphic = ImageView(closedFolderImage)
                }
            }
        }
    }

    private fun loadFromSdCard(path: String) {
        log.info("loading entries for path $path from sd-card")

        runAsync {
            serialPort.use { p ->
                log.info("start of background task")
                p.open()
                p.listDir(path).map { name ->
                    log.info(name)
                    p.getFileInfo(name)
                }.map { info -> TreeFileInfo(info) }
            }
        }.success { list ->
            log.info("end of background task")
            children.clear()
            children.addAll(list)
        }
    }
}
