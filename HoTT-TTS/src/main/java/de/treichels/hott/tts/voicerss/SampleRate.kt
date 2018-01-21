package de.treichels.hott.tts.voicerss

@Suppress("EnumEntryName")
enum class SampleRate(val rate: Float) {
    _8KHZ(8000f), _11KHZ(11025f), _12KHZ(12000f), _16KHZ(16000f), _22KHZ(22050f), _24KHZ(24000f), _32KHZ(32000f), _44KHZ(44100f), _48KHZ(48000f)
}