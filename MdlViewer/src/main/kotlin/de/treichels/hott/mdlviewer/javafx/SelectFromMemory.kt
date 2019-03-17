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
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.serial.ModelInfo
import javafx.beans.binding.BooleanBinding
import javafx.concurrent.Task
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.util.Callback
import tornadofx.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class SelectFromMemory : SelectFromTransmitter() {
    private var listView by singleAssign<ListView<ModelInfo>>()

    // dialog is ready when the user selects an item from the list
    override fun isReady(): BooleanBinding = listView.selectionModel.selectedItemProperty().isNotNull

    // load the selected item as Model
    override fun getResult(): Task<Model>? {
        val modelInfo = listView.selectionModel.selectedItem
        val serialPort = this.transmitter

        return if (modelInfo != null && serialPort != null) {
            runAsync { Model.loadModel(modelInfo, serialPort) }
        } else null
    }

    init {
        title = messages["select_from_memory_title"]

        // add to UI
        root.center {
            listView = listview {
                selectionModel.selectionMode = SelectionMode.SINGLE
                setOnMouseClicked { handleDoubleClick(it) }
                cellFactory = Callback { ModelInfoListCell() }
            }
        }
    }

    /**
     * Refresh a [ListView] with a list of all models from the transmitter memory using the [HoTTTransmitter] from parent.
     */
    override fun refreshUITask(): Task<*> {
        // add temporary placeholder
        listView.items = observableList(null)

        // fill list in background
        return listView.runAsyncWithOverlay {
            transmitter?.allModelInfos?.filter { it.modelType != ModelType.Unknown } ?: listOf()
        }.success { list ->
            listView.items = list.observable()
        }
    }

    private inner class ModelInfoListCell : ListCell<ModelInfo>() {
        override fun updateItem(item: ModelInfo?, empty: Boolean) {
            super.updateItem(item, empty)

            if (!empty) {
                text = if (item == null) {
                    messages["loading"]
                } else {
                    String.format("%02d: %c%s.mdl", item.modelNumber + 1, item.modelType.char, item.modelName)
                }
            }
        }
    }
}


