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
import de.treichels.hott.model.serial.FileInfo
import de.treichels.hott.model.serial.ModelInfo
import javafx.concurrent.Task
import tornadofx.*
import java.io.ByteArrayOutputStream
import java.util.concurrent.Callable

/**
 * Run a [Callable] in a background task.
 */
fun <T> Callable<T>.start(): Task<T> {
    return runAsync(daemon = true) {
        call()
    }
}

/**
 * A [Callable] that retrieves the model data for the provided [ModelInfo] from the transmitter memory.
 */
fun HoTTSerialPort.getModel(modelInfo: ModelInfo): Callable<Model> = Callable {
    use {
        open()
        Model(modelInfo, getModelData(modelInfo))
    }
}

/**
 * A [Callable] that retrieves the model data for the provided [FileInfo] from the SD card in the transmitter.
 */
fun HoTTSerialPort.getModel(fileInfo: FileInfo): Callable<Model> = Callable {
    val fileName = fileInfo.name
    val type = ModelType.forChar(fileName[0])
    val name = fileName.substring(1, fileName.length - 4)
    val modelInfo = ModelInfo(0, name, type, null, null)
    val data = use { p ->
        p.open()
        ByteArrayOutputStream().use { stream ->
            p.readFile(fileInfo.path, stream)
            stream.toByteArray()
        }
    }

    Model(modelInfo, data)
}

