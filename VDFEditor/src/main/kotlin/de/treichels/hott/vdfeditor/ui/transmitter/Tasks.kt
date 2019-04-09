package de.treichels.hott.vdfeditor.ui.transmitter

import de.treichels.hott.decoder.HoTTTransmitter
import de.treichels.hott.serial.UpdateHandler
import de.treichels.hott.voice.VoiceFile
import javafx.concurrent.Task

abstract class TransmitterTask<T>(title: String) : Task<T>(), UpdateHandler<T> {
    var transmitter: HoTTTransmitter? = null

    init {
        @Suppress("LeakingThis")
        updateTitle(title)
    }

    override fun update(step: Int, count: Int) {
        updateMessage(String.format("%d %%", step * 100 / count))
        updateProgress(step.toLong(), count.toLong())
    }
}

internal class LoadVoiceFileTask(title: String, private val user: Boolean) : TransmitterTask<VoiceFile?>(title) {
    override fun call(): VoiceFile? {
        transmitter?.turnRfOutOff()
        return transmitter?.loadVoiceFile(user, this)
    }
}

internal class SendVoiceFileTask(title: String, private val voiceFile: VoiceFile) : TransmitterTask<Unit>(title) {
    override fun call() {
        transmitter?.turnRfOutOff()
        transmitter?.sendVoiceFile(voiceFile, this)
    }
}
