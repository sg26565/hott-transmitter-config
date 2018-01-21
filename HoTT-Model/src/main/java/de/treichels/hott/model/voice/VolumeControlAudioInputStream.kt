package de.treichels.hott.model.voice

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class VolumeControlAudioInputStream(private val source: AudioInputStream, private val volume: Double) : AudioInputStream(source, source.format, AudioSystem.NOT_SPECIFIED.toLong()) {
    override fun read(b: ByteArray?, off: Int, len: Int): Int {
        if (b == null) return 0

        val bytesRead = super.read(b, off, len)

        for (i in 0 until bytesRead step 2) {
            // convert channel to short
            val value = (b[i].toInt() and 0xFF or (b[i + 1].toInt() and 0xFF shl 8)).toShort()

            // apply volume
            var value2 = (value * volume).toInt()

            // clipping
            value2 = Math.max(value2, Short.MIN_VALUE.toInt())
            value2 = Math.min(value2, Short.MAX_VALUE.toInt())

            // back to bytes
            b[i] = (value2 and 0xff).toByte()
            b[i + 1] = (value2 and 0xff00 ushr 8).toByte()
        }

        return bytesRead
    }
}