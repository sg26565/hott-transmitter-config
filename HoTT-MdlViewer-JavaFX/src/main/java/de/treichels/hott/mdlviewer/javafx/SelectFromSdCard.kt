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
import de.treichels.hott.model.serial.FileInfo
import javafx.beans.binding.BooleanBinding
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeView
import tornadofx.*
import java.util.concurrent.Callable

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class SelectFromSdCard : SelectFromTransmitter() {
    private val treeView = TreeView<String>()

    override fun isReady() = object : BooleanBinding() {
        override fun computeValue(): Boolean {
            val item = treeView.selectionModel.selectedItem as? TreeFileInfo

            return item != null && item.isReady
        }

        init {
            // invalidate this BooleanBinding when treeView selection changes
            treeView.selectionModel.selectedItemProperty().addListener { _ -> invalidate() }
        }
    }

    /**
     * A [Callable] that retrieves the model data for the provided [FileInfo] from the SD card in the transmitter.
     */
    override fun getResultCallable(): Callable<Model>? {
        val item = treeView.selectionModel.selectedItem as? TreeFileInfo
        val fileInfo = item?.fileInfo

        return if (fileInfo != null) Callable { loadModel(fileInfo, serialPort) } else null
    }

    override fun refreshUITask() = treeView.runAsyncWithOverlay {
        TreeFileInfo(messages["root"], treeView, serialPort!!)
    }.ui {
        treeView.root = it
        it.isExpanded = true
    }

    init {
        title = messages["select_from_sdcard_title"]

        root.center = treeView.apply {
            selectionModel.selectionMode = SelectionMode.SINGLE
            setOnMouseClicked { handleDoubleClick(it) }
        }
    }
}