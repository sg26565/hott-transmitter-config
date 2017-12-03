package de.treichels.hott.vdfeditor.ui.transmitter

import gde.model.voice.VoiceFile

internal class SendVoiceFileTask(title: String, private val voiceFile: VoiceFile) : TransmitterTask(title) {
    override fun call(): VoiceFile? = port?.use { port ->
        port.open()
        port.turnRfOutOff()
        port.delay()
        port.sendVoiceFile(voiceFile, this)

        return voiceFile
    }
}
