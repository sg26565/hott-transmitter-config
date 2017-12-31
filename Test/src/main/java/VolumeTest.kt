import de.treichels.hott.model.voice.Player
import org.apache.commons.io.IOUtils
import java.io.FileOutputStream
import java.net.URL
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

fun main(args: Array<String>) {
//    val file1 = File("G:\\Meine Ablage\\Graupner\\VDFEditor\\WAV\\23_maximale_Entfernung.wav")
//    VoiceData.forFile(file1, 1.0).play()

//    val stream = VoiceRSS(text = "maximale Entfernung").stream
//    VoiceData.forStream(stream, "maximale Entfernung", 3.0).play()

    val src = "http://api.voicerss.org/?key=1def8e9c6ebf4a2eb02fc7b510b04387&hl=de-de&r=0&c=WAV&f=22khz_16bit_mono&ssml=false&b64=false&src=maximale+Entfernung"
    val url = URL(src)
    val conn = url.openConnection()
    val stream = conn.getInputStream()

    if (false)
        FileOutputStream("C:\\Users\\olive\\Desktop\\maximale Entfernung.wav").use {
            IOUtils.copy(stream, it)
        }
    else {
        val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 22050f,16,1,2,22050f,false)
        val audioStream = AudioInputStream(stream,format, AudioSystem.NOT_SPECIFIED.toLong())
        Player.play(audioStream)
    }
}