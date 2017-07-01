package gde.model.voice;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A utility class that can play audio data.
 *
 * @author oliver.treichel@gmx.de
 */
public class Player {
    public static void play(final AudioFormat format, final byte[] data) throws LineUnavailableException, InterruptedException {
        play(format, data, 0, data.length, true);
    }

    public static void play(final AudioFormat format, final byte[] data, final boolean sync) throws LineUnavailableException, InterruptedException {
        play(format, data, 0, data.length, sync);
    }

    public static void play(final AudioFormat format, final byte[] data, final int offset, final int buffersize)
            throws LineUnavailableException, InterruptedException {
        play(format, data, offset, buffersize, true);
    }

    public static void play(final AudioFormat format, final byte[] data, final int offset, final int buffersize, final boolean sync)
            throws LineUnavailableException, InterruptedException {
        final Clip clip = AudioSystem.getClip();
        clip.open(format, data, offset, buffersize);
        play(clip, sync);
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

    public static void play(final File file, final boolean sync)
            throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException {
        final AudioInputStream stream = AudioSystem.getAudioInputStream(file);
        System.out.println(stream.getFormat());
        final Clip clip = AudioSystem.getClip();
        clip.open(stream);
        play(clip, sync);
    }
}