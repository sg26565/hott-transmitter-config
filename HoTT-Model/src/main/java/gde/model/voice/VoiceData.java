package gde.model.voice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.IOUtils;

/**
 * Representation of a single announcement in a voice data file (.vdf).
 *
 * @author oliver.treichel@gmx.de
 */
public class VoiceData {
    /** Default audio format 11 kHz 16-bit signed PCM mono */
    public static final AudioFormat AUDIO_FORMAT = new AudioFormat(Encoding.PCM_SIGNED, 11025, 16, 1, 2, 11025, false);

    public static VoiceData readWav(final File wavFile) throws UnsupportedAudioFileException, IOException {
        // read from file
        final AudioInputStream sourceAudioStream = AudioSystem.getAudioInputStream(wavFile);

        // convert audo format
        final AudioInputStream convertedAudioStream = AudioSystem.getAudioInputStream(AUDIO_FORMAT, sourceAudioStream);

        // encode to ADPCM
        final InputStream encodedStream = ADPCMCodec.encode(convertedAudioStream);

        return new VoiceData(wavFile.getName().replaceAll(".wav$", ""), 0, -1, IOUtils.toByteArray(encodedStream));
    }

    private String name;
    private int num1;
    private int num2;

    private final byte[] data;

    public VoiceData(final String name, final int num1, final int num2, final byte[] data) {
        super();
        this.name = name;
        this.num1 = num1;
        this.num2 = num2;
        this.data = data;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final VoiceData other = (VoiceData) obj;
        if (!Arrays.equals(data, other.data)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (num1 != other.num1) return false;
        if (num2 != other.num2) return false;
        return true;
    }

    public AudioInputStream getAudioInputStream() throws IOException {
        return new AudioInputStream(getPCMStream(), AUDIO_FORMAT, data.length * 2);
    }

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public int getNum1() {
        return num1;
    }

    public int getNum2() {
        return num2;
    }

    public byte[] getPCM() throws IOException {
        return ADPCMCodec.decode(data);
    }

    public InputStream getPCMStream() throws IOException {
        return ADPCMCodec.decode(new ByteArrayInputStream(data));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + num1;
        result = prime * result + num2;
        return result;
    }

    public void play() throws LineUnavailableException, InterruptedException, IOException {
        Player.play(AUDIO_FORMAT, getPCMStream());
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setNum1(final int num1) {
        this.num1 = num1;
        num2 = (short) ~num1;
    }

    public void setNum2(final int num2) {
        this.num2 = num2;
        num1 = (short) ~num2;
    }

    public void writeWav(final File wavFile) throws IOException {
        AudioSystem.write(getAudioInputStream(), AudioFileFormat.Type.WAVE, wavFile);
    }
}
