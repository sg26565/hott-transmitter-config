import de.treichels.hott.model.voice.VoiceRSS
import de.treichels.hott.model.voice.readSoundFile
import java.io.File

fun main(args: Array<String>) {
    val file1 = File("G:\\Meine Ablage\\Graupner\\VDFEditor\\WAV\\23_maximale_Entfernung.wav")
    readSoundFile(file1, 1.0).play()

    val file2 = VoiceRSS(text = "maximale Entfernung").file
    readSoundFile(file2, 3.0).play()
}