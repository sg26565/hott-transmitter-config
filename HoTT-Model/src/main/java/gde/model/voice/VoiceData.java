package gde.model.voice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
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
public class VoiceData implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Default audio format 11 kHz 16-bit signed PCM mono */
    public static final AudioFormat AUDIO_FORMAT = new AudioFormat(Encoding.PCM_SIGNED, 11025, 16, 1, 2, 11025, false);

    public static VoiceData readSoundFile(final File soundFile) throws UnsupportedAudioFileException, IOException {
        // read from file
        final AudioInputStream sourceAudioStream = AudioSystem.getAudioInputStream(soundFile);
        final AudioFormat sourceFormat = sourceAudioStream.getFormat();
        final float rate = sourceFormat.getSampleRate();
        final int channels = sourceFormat.getChannels();

        // convert to from MP3 or OGG to PCM
        final AudioFormat pcmFormat = new AudioFormat(Encoding.PCM_SIGNED, rate, 16, channels, channels * 2, rate, false);
        final AudioInputStream pcmAudioStream = sourceFormat.getEncoding() == Encoding.PCM_SIGNED ? sourceAudioStream
                : AudioSystem.getAudioInputStream(pcmFormat, sourceAudioStream);

        // convert sample rate and channels
        final AudioInputStream targetAudioStream = pcmFormat.matches(AUDIO_FORMAT) ? pcmAudioStream
                : AudioSystem.getAudioInputStream(AUDIO_FORMAT, pcmAudioStream);

        // encode to ADPCM
        final InputStream encodedStream = ADPCMCodec.encode(targetAudioStream);

        final String fileName = soundFile.getName();
        final int dot = fileName.lastIndexOf(".");
        return new VoiceData(fileName.substring(0, dot), IOUtils.toByteArray(encodedStream));
    }

    private String name;
    private final byte[] data;

    public VoiceData(final String name, final byte[] data) {
        this.name = name;
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
        return true;
    }

    public AudioInputStream getAudioInputStream() throws IOException {
        return new AudioInputStream(getPcmInputStream(), AUDIO_FORMAT, data.length * 2);
    }

    public String getName() {
        return name;
    }

    public byte[] getPcmData() throws IOException {
        return ADPCMCodec.decode(data);
    }

    public InputStream getPcmInputStream() throws IOException {
        return ADPCMCodec.decode(getRawInputStream());
    }

    public byte[] getRawData() {
        return data;
    }

    public InputStream getRawInputStream() {
        return new ByteArrayInputStream(getRawData());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public void play() throws LineUnavailableException, InterruptedException, IOException {
        Player.play(AUDIO_FORMAT, getPcmInputStream());
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void writeVox(final File voxFile) throws IOException {
        Files.write(voxFile.toPath(), getRawData());
    }

    public void writeWav(final File wavFile) throws IOException {
        AudioSystem.write(getAudioInputStream(), AudioFileFormat.Type.WAVE, wavFile);
    }
}
