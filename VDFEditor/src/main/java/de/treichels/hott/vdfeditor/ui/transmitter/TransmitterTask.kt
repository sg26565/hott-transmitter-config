package de.treichels.hott.vdfeditor.ui.transmitter

import de.treichels.hott.HoTTSerialPort
import gde.model.serial.UpdateHandler
import gde.model.voice.VoiceFile
import javafx.concurrent.Task

abstract class TransmitterTask internal constructor(title: String) : Task<VoiceFile?>(), UpdateHandler {
    var port: HoTTSerialPort? = null

    init {
        this.updateTitle(title)
    }

    override fun update(step: Int, count: Int) {
        updateMessage(String.format("%d %%", step * 100 / count))
        updateProgress(step.toLong(), count.toLong())
    }
}
