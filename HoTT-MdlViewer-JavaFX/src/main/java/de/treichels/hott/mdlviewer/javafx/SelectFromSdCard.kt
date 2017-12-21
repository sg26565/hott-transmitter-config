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

import de.treichels.hott.model.serial.FileType
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

    override fun isReady(): BooleanBinding = object : BooleanBinding() {
        override fun computeValue(): Boolean {
            val item = treeView.selectionModel.selectedItem

            if (item != null && item is TreeFileInfo) {
                val fileInfo = item.fileInfo
                if (fileInfo != null)
                    return (fileInfo.type == FileType.File && fileInfo.name.endsWith(".mdl") && fileInfo.size <= 0x3000 && fileInfo.size >= 0x2000)
            }

            return false
        }
    }

    override fun getResultCallable(): Callable<Model>? {
        val fileInfo = (treeView.selectionModel.selectedItem as? TreeFileInfo)?.fileInfo

        return if (fileInfo != null) serialPort?.getModel(fileInfo) else null
    }

    override fun refreshUITask() = treeView.runAsyncWithOverlay {
        TreeFileInfo(messages["RootLabel"], serialPort)
    }.ui {
        treeView.root = it
    }

    init {
        title = messages["LoadFromSdCard"]

        root.center = treeView.apply {
            selectionModel.selectionMode = SelectionMode.SINGLE
            setOnMouseClicked { handleDoubleClick(it) }
        }
    }
}