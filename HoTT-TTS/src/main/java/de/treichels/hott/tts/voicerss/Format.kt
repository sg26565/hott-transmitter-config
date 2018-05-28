package de.treichels.hott.tts.voicerss

@Suppress("EnumEntryName")
enum class Format(internal val voiceRSSEncoding: VoiceRSSEncoding, internal val sampleRate: SampleRate, internal val bits: Bits? = null, internal val channels: Channels) {
    pcm_8khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_8khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_8khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_8khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._8KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_11khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_11khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_11khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_11khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._11KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO),
    pcm_12khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_12khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_12khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_12khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._12KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO),
    pcm_16khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_16khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_16khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_16khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._16KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO),
    pcm_22khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_22khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_22khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_22khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._22KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO),
    pcm_24khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_24khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_24khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_24khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._24KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO),
    pcm_32khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_32khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_32khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_32khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._32KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO),
    pcm_44khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_44khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_44khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_44khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._44KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO),
    pcm_48khz_8bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits.EIGHT, channels = Channels.MONO),
    pcm_48khz_8bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits.EIGHT, channels = Channels.STEREO),
    pcm_48khz_16bit_mono(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits.SIXTEEN, channels = Channels.MONO),
    pcm_48khz_16bit_stereo(voiceRSSEncoding = VoiceRSSEncoding.PCM, sampleRate = SampleRate._48KHZ, bits = Bits.SIXTEEN, channels = Channels.STEREO), alaw_8khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.MONO),
    alaw_8khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.STEREO), alaw_11khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.MONO),
    alaw_11khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.STEREO), alaw_22khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.MONO),
    alaw_22khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.STEREO), alaw_44khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.MONO),
    alaw_44khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.STEREO), ulaw_8khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.MONO),
    ulaw_8khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._8KHZ, channels = Channels.STEREO), ulaw_11khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.MONO),
    ulaw_11khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._11KHZ, channels = Channels.STEREO), ulaw_22khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.MONO),
    ulaw_22khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._22KHZ, channels = Channels.STEREO), ulaw_44khz_mono(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.MONO),
    ulaw_44khz_stereo(voiceRSSEncoding = VoiceRSSEncoding.ALAW, sampleRate = SampleRate._44KHZ, channels = Channels.STEREO);

    val key: String
        get() {
            val sb = StringBuilder()

            if (voiceRSSEncoding == VoiceRSSEncoding.PCM)
                sb.append(sampleRate.name.substring(1)).append('_').append(bits!!.bits).append("bit")
            else
                sb.append(voiceRSSEncoding.name).append(sampleRate.name)

            sb.append('_').append(channels.name)

            return sb.toString().toLowerCase()
        }

    override fun toString(): String {
        return "${sampleRate.rate/1000}kHz ${if (channels==Channels.MONO)"Mono" else "Stero"}"
    }
}