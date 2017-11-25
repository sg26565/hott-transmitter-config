package de.treichels.hott.vdfeditor.ui.transmitter

import gde.model.HoTTException
import gde.model.voice.VoiceFile

internal class LoadVoiceFileTask(title: String, private val user: Boolean) : TransmitterTask(title) {
    override fun call(): VoiceFile? = port?.use { port ->
        port.open()
        port.turnRfOutOff()
        port.delay()

        if (user && port.getVoiceInfo(false).voiceVersion == 2000) throw HoTTException("NoUerVoiceFiles")

        return port.loadVoiceFile(user, this)
    }
}
