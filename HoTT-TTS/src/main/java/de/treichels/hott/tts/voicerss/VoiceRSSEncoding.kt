package de.treichels.hott.tts.voicerss

import javax.sound.sampled.AudioFormat

@Suppress("EnumEntryName")
enum class VoiceRSSEncoding(val encoding: AudioFormat.Encoding) {
    PCM(AudioFormat.Encoding.PCM_SIGNED), ALAW(AudioFormat.Encoding.ALAW), ULAW(AudioFormat.Encoding.ULAW)
}