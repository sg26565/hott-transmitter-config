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

import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.serial.FileInfo
import de.treichels.hott.serial.FileType
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.ImageView
import tornadofx.*
import tornadofx.FX.Companion.messages


class TreeFileInfo(name: String, private val treeView: TreeView<String>, private val transmitter: HoTTTransmitter, internal val fileInfo: FileInfo? = null) : TreeItem<String>(name) {
    companion object {
        private val resources = ResourceLookup(TreeFileInfo)

        private val fileImage = resources.image("/File.gif")
        private val openFolderImage = resources.image("/Folder_open.gif")
        private val closedFolderImage = resources.image("/Folder_closed.gif")
        private val rootFolderImage = resources.image("/Root.gif")
    }

    // helper
    private val isRoot = fileInfo == null
    private val isDir = fileInfo?.type == FileType.Dir
    private val isFile = fileInfo?.type == FileType.File

    internal val isReady
        get() = fileInfo != null && isFile && value.endsWith(".mdl") && fileInfo.size <= 0x3000 && fileInfo.size >= 0x2000

    init {
        isExpanded = false

        // update treeItem children on expand
        expandedProperty().addListener { _ -> update() }

        when {
            isRoot -> {
                graphic = ImageView(rootFolderImage)
            }

            isDir -> {
                graphic = ImageView(closedFolderImage)
            }

            isFile -> {
                graphic = ImageView(fileImage)
            }
        }
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
                    loadFromSdCard("/")
                } else {
                    loading()
                }
            }

            isDir -> {
                if (isExpanded) {
                    graphic = ImageView(openFolderImage)
                    loadFromSdCard(fileInfo!!.path)
                } else {
                    graphic = ImageView(closedFolderImage)
                    loading()
                }
            }
        }
    }

    private fun loadFromSdCard(path: String) {
        treeView.runAsyncWithOverlay {
            transmitter.listDir(path).map { name ->
                transmitter.getFileInfo(name)
            }.map { info ->
                TreeFileInfo(info.name, treeView, transmitter, info).apply {
                    if (isDir) loading()
                }
            }
        }.success { list ->
            children.clear()
            children.addAll(list)
        }
    }
}
