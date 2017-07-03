package gde.model.voice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A utility class that can play audio data.
 *
 * @author oliver.treichel@gmx.de
 */
public class Player {
    public static void play(final AudioFormat format, final byte[] data) throws LineUnavailableException, InterruptedException, IOException {
        play(format, data, true);
    }

    public static void play(final AudioFormat format, final byte[] data, final boolean sync)
            throws LineUnavailableException, InterruptedException, IOException {
        play(format, data, 0, data.length, sync);
    }

    public static void play(final AudioFormat format, final byte[] data, final int offset, final int buffersize)
            throws LineUnavailableException, InterruptedException, IOException {
        play(format, data, offset, buffersize, true);
    }

    public static void play(final AudioFormat format, final byte[] data, final int offset, final int buffersize, final boolean sync)
            throws LineUnavailableException, InterruptedException, IOException {
        play(format, new ByteArrayInputStream(data, offset, buffersize), sync);
    }

    public static void play(final AudioFormat format, final InputStream stream) throws LineUnavailableException, IOException {
        play(format, stream, true);
    }

    public static void play(final AudioFormat format, final InputStream stream, final boolean sync) throws LineUnavailableException, IOException {
        final SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(format);

        try {
            sourceDataLine.open();
            sourceDataLine.start();

            final byte[] buffer = new byte[sourceDataLine.getBufferSize()];

            while (stream.available() > 0) {
                final int len = stream.read(buffer);
                sourceDataLine.write(buffer, 0, len);
            }

            if (sync) sourceDataLine.drain();
        } finally {
            if (sourceDataLine.isRunning()) sourceDataLine.stop();
            if (sourceDataLine.isOpen()) sourceDataLine.close();
        }
    }

    public static void play(final AudioInputStream stream) throws LineUnavailableException, IOException {
        play(stream, true);
    }

    public static void play(final AudioInputStream stream, final boolean sync) throws LineUnavailableException, IOException {
        play(stream.getFormat(), stream, sync);
    }

    public static void play(final Clip clip) throws InterruptedException {
        play(clip, true);
    }

    public static void play(final Clip clip, final boolean sync) throws InterruptedException {
        clip.addLineListener(e -> {
            if (e.getType() == Type.STOP) {
                if (sync) synchronized (clip) {
                    clip.notify();
                }
                clip.close();
            }
        });

        clip.start();

        if (sync) synchronized (clip) {
            clip.wait();
        }
    }

    public static void play(final File file) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        play(file, true);
    }

    public static void play(final File file, final boolean sync) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        play(AudioSystem.getAudioInputStream(file), sync);
    }
}