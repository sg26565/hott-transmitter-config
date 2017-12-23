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
import de.treichels.hott.model.enums.ModelType
import de.treichels.hott.model.serial.ModelInfo
import javafx.beans.binding.BooleanBinding
import javafx.concurrent.Task
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import tornadofx.*
import java.util.concurrent.Callable

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
class SelectFromMemory : SelectFromTransmitter() {
    private var listView by singleAssign<ListView<String>>()
    private var modelInfoList: List<ModelInfo>? = null

    // dialog is ready when the user selects an item from the list
    override fun isReady(): BooleanBinding = listView.selectionModel.selectedItemProperty().isNotNull

    // refresh listView from Transmitter memory
    override fun refreshUITask() = listView.loadFromMemory()

    // load the selected item as Model
    override fun getResultCallable(): Callable<Model>? {
        val index = listView.selectionModel.selectedIndex
        val modelInfo = modelInfoList?.get(index)

        return if (modelInfo != null) {
            Callable {
                val modelData = serialPort?.use { p ->
                    p.open()
                    p.getModelData(modelInfo)
                }

                Model(modelInfo, modelData)
            }
        } else null
    }

    init {
        title = messages["select_from_memory_title"]

        // add to UI
        root.center {
            listView = listview {
                selectionModel.selectionMode = SelectionMode.SINGLE
                setOnMouseClicked { handleDoubleClick(it) }
                items.add(messages["loading"])
            }
        }
    }

    /**
     * Refresh a [ListView] with a list of all models from the transmitter memory using the [HoTTSerialPort] from parent.
     */
    @Synchronized
    private fun ListView<String>.loadFromMemory(): Task<*> {
        items.clear()

        // add temporary placeholder
        items.add(messages["loading"])

        // fill list in background
        return listView.runAsyncWithOverlay {
            serialPort?.use { p ->
                p.open()
                p.allModelInfos
            }?.filter { it?.modelType != ModelType.Unknown } ?: listOf()
        }.success { list ->
            // save result
            modelInfoList = list

            // remove temporary placeholder
            items.clear()

            // translate list of ModelInfo to list of String
            items.addAll(list.map { info -> String.format("%02d: %c%s.mdl", info.modelNumber + 1, info.modelType?.char, info.modelName) })
        }
    }
}

