
import de.treichels.hott.model.voice.Player
import de.treichels.hott.model.voice.VolumeControlAudioInputStream
import java.io.File
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

fun main(vararg args: String) {
    play("G:\\Meine Ablage\\Weatronic\\Sounds\\Sprache_BAT60_Anna\\Vollgas.wav")
    play("G:\\Meine Ablage\\Weatronic\\Sounds\\Sprache_BAT60_Anna\\Vollgas.mp3")
    play("G:\\Meine Ablage\\Weatronic\\Sounds\\Sprache_BAT60_Anna\\Vollgas.ogg")
}

private fun play(path: String) {
    var audioInputStream = AudioSystem.getAudioInputStream(File(path))
    var format = audioInputStream.format

    // convert to PCM
    if (format.encoding != AudioFormat.Encoding.PCM_SIGNED) {
        format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.sampleRate, 16, format.channels, 2 * format.channels, format.sampleRate, false)
        audioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream)
    }

    val volumeControlAudioInputStream = VolumeControlAudioInputStream(audioInputStream, 1.0)

    Player.play(volumeControlAudioInputStream)
}

