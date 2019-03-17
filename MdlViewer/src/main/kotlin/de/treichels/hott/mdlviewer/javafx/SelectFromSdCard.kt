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
import de.treichels.hott.serial.FileInfo
import de.treichels.hott.serial.FileType
import javafx.beans.binding.BooleanBinding
import javafx.concurrent.Task
import javafx.event.EventType
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.ImageView
import javafx.util.Callback
import tornadofx.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class SelectFromSdCard : SelectFromTransmitter() {
    /**
     * Root [TreeItem] that loads it's children dynamically.
     */
    private val rootItem: TreeItem<FileInfo> = TreeItem(FileInfo.root).apply {
        isExpanded = false
        loading()

        // Update children of this tree item when the node is expanded
        addEventHandler(branchExpandedEvent) { event ->
            runLater {
                treeView.runAsyncWithOverlay {
                    transmitter!!.listDir(event.treeItem.value.path).map {
                        TreeItem(transmitter!!.getFileInfo(it))
                    }
                }.success { list ->
                    event.treeItem.children.clear()
                    event.treeItem.children.addAll(list)
                }
            }
        }

        // clear all children - reload on expand
        addEventHandler(branchCollapsedEvent) { event ->
            event.treeItem.loading()
        }
    }

    /**
     * [TreeView] with a single root node.
     */
    private val treeView = treeview(rootItem) {
        cellFactory = Callback { FileInfoTreeCell() }
        selectionModel.selectionMode = SelectionMode.SINGLE
        setOnMouseClicked { handleDoubleClick(it) }
    }

    /**
     * This dialog is ready when the user selects [FileInfo] node representing a .mdl file with a valid file size.
     */
    override fun isReady() = object : BooleanBinding() {
        override fun computeValue(): Boolean {
            val item = treeView.selectionModel.selectedItem?.value
            return item != null && item.type == FileType.File && item.name.endsWith(".mdl") && item.size <= 0x3000 && item.size >= 0x2000
        }

        init {
            // invalidate this BooleanBinding when treeView selection changes
            treeView.selectionModel.selectedItemProperty().addListener { _ -> invalidate() }
        }
    }

    /**
     * A [Task] that retrieves the model data for the provided [FileInfo] from the SD card in the transmitter.
     */
    override fun getResult(): Task<Model>? {
        val fileInfo = treeView.selectionModel.selectedItem?.value
        val serialPort = this.transmitter

        return if (isReady().value && fileInfo != null && serialPort != null) runAsync { loadModel(fileInfo, serialPort) } else null
    }

    /**
     * Reload the [TreeView]
     */
    override fun refreshUITask() = treeView.runAsyncWithOverlay {
        rootItem.isExpanded = false
        rootItem.loading()

        runLater {
            rootItem.isExpanded = true
        }
    }

    init {
        title = messages["select_from_sdcard_title"]
        root.center = treeView
    }

    companion object {
        private val resources = ResourceLookup(TreeFileInfo)

        private val fileImage = resources.image("/File.gif")
        private val openFolderImage = resources.image("/Folder_open.gif")
        private val closedFolderImage = resources.image("/Folder_closed.gif")
        private val rootFolderImage = resources.image("/Root.gif")

        private val branchExpandedEvent: EventType<TreeItem.TreeModificationEvent<FileInfo>> = TreeItem.branchExpandedEvent()
        private val branchCollapsedEvent: EventType<TreeItem.TreeModificationEvent<FileInfo>> = TreeItem.branchCollapsedEvent()
    }

    /**
     * Add a dummy "loading ..." node
     */
    private fun TreeItem<FileInfo>.loading() {
        // add loading pseudo child
        children.clear()
        children.add(null)
    }

    /**
     * Format [FileInfo] items in the [TreeView]
     */
    private inner class FileInfoTreeCell : TreeCell<FileInfo>() {
        override fun updateItem(item: FileInfo?, empty: Boolean) {
            super.updateItem(item, empty)

            when {
                empty -> {
                    graphic = null
                    text = null
                }

                item == null -> {
                    graphic = null
                    text = messages["loading"]
                }

                item.isRoot -> {
                    graphic = ImageView(rootFolderImage)
                    text = messages["root"]
                }

                item.isDir -> {
                    if (treeItem.isExpanded) {
                        graphic = ImageView(openFolderImage)
                    } else {
                        graphic = ImageView(closedFolderImage)
                        treeItem.loading()
                    }
                    text = item.name
                }

                item.isFile -> {
                    graphic = ImageView(fileImage)
                    text = item.name
                }
            }
        }
    }
}

