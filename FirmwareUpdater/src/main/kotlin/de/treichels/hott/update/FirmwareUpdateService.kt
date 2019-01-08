package de.treichels.hott.update

import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.ui.CallbackTask
import javafx.concurrent.Service
import java.io.File

class FirmwareUpdateService : Service<Unit>() {
    lateinit var serialPort: HoTTSerialPort
    lateinit var file: File

    override fun createTask() = FirmwareUpdateTask()

    inner class FirmwareUpdateTask : CallbackTask<Unit>() {
        override fun call() {
            serialPort.updateDevice(file, this)
        }
    }
}
