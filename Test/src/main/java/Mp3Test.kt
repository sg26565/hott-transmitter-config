import com.sun.media.sound.WaveFileReader
import de.treichels.hott.model.voice.Player
import de.treichels.hott.model.voice.VolumeControlAudioInputStream
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader
import org.apache.commons.io.FilenameUtils
import java.io.File
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

fun main(vararg args: String) {
    play(File("G:\\Meine Ablage\\Weatronic\\Sounds\\Sprache_BAT60_Anna\\Vollgas.wav"))
    play(File("G:\\Meine Ablage\\Weatronic\\Sounds\\Sprache_BAT60_Anna\\Vollgas.mp3"))
    play(File("G:\\Meine Ablage\\Weatronic\\Sounds\\Sprache_BAT60_Anna\\Vollgas.ogg"))
}

private fun play(file: File) {
    val inputStream = file.inputStream()
    val extension = FilenameUtils.getExtension(file.name)

    val fileReader = when (extension) {
        "wav" -> WaveFileReader()
        "mp3" -> MpegAudioFileReader()
        "ogg" -> VorbisAudioFileReader()
        else -> TODO()
    }

    var audioInputStream = fileReader.getAudioInputStream(inputStream)
    var format = audioInputStream.format

    // convert to PCM
    if (format.encoding != AudioFormat.Encoding.PCM_SIGNED) {
        format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.sampleRate, 16, format.channels, 2 * format.channels, format.sampleRate, false)
        audioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream)
    }

    val volumeControlAudioInputStream = VolumeControlAudioInputStream(audioInputStream, 1.0)

    Player.play(volumeControlAudioInputStream)
}

