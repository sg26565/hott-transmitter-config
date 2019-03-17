package de.treichels.hott.update

import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.decoder.updateDevice
import de.treichels.hott.ui.CallbackTask
import javafx.concurrent.Service
import java.io.File

class FirmwareUpdateService : Service<Unit>() {
    lateinit var transmitter: HoTTTransmitter
    lateinit var file: File

    override fun createTask() = FirmwareUpdateTask()

    inner class FirmwareUpdateTask : CallbackTask<Unit>() {
        override fun call() {
            transmitter.updateDevice(file, this)
        }
    }
}
