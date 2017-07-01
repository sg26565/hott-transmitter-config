package gde.model.voice;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.LineUnavailableException;

public class VoiceData {
    /** Default audio format 11 kHz 16-bit signed PCM mono */
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(Encoding.PCM_SIGNED, 11025, 16, 1, 2, 11025, false);

    private final String name;
    private final int num1;
    private final int num2;
    private final byte[] data;

    public VoiceData(final String name, final int num1, final int num2, final byte[] data) {
        super();
        this.name = name;
        this.num1 = num1;
        this.num2 = num2;
        this.data = data;
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

    public void play() throws LineUnavailableException, InterruptedException, IOException {
        final ADPCMCodec codec = new ADPCMCodec();
        Player.play(AUDIO_FORMAT, codec.decodeADPCM(data));
    }
}
