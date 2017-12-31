package de.treichels.hott.vdfeditor.ui.transmitter

import de.treichels.hott.decoder.HoTTSerialPort
import de.treichels.hott.model.HoTTException
import de.treichels.hott.model.serial.UpdateHandler
import de.treichels.hott.model.voice.VoiceFile
import javafx.concurrent.Task

abstract class TransmitterTask<T>(title: String) : Task<T>(), UpdateHandler {
    override val cancelled = isCancelled
    var serialPort: HoTTSerialPort? = null

    init {
        @Suppress("LeakingThis")
        updateTitle(title)
    }

    override fun update(step: Int, count: Int) {
        updateMessage(String.format("%d %%", step * 100 / count))
        updateProgress(step.toLong(), count.toLong())
    }
}

class LoadVoiceFileTask(title: String, private val user: Boolean) : TransmitterTask<VoiceFile?>(title) {
    override fun call(): VoiceFile? = serialPort?.use { p ->
        p.open()
        p.turnRfOutOff()
        p.delay()

        if (user && p.getVoiceInfo(false).voiceVersion == 2000) throw HoTTException("NoUerVoiceFiles")

        return p.loadVoiceFile(user, this)
    }
}

internal class SendVoiceFileTask(title: String, private val voiceFile: VoiceFile) : TransmitterTask<Unit>(title) {
    override fun call() = serialPort?.use { p ->
        p.open()
        p.turnRfOutOff()
        p.delay()

        p.sendVoiceFile(voiceFile, this)
    }
}