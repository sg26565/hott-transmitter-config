import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import javax.sound.sampled.LineUnavailableException;

import de.treichels.hott.HoTTDecoder;
import gde.model.voice.VoiceData;
import gde.model.voice.VoiceFile;
import gde.util.Util;

public class VDFDump {
    public static void dump(final File vdf) {
        System.out.println(vdf);
        final File out = new File(vdf.getParentFile(), vdf.getName().replaceAll(".vdf", ".txt"));
        try {
            final byte[] bytes = Files.readAllBytes(vdf.toPath());
            Files.write(out.toPath(), Util.dumpData(bytes).getBytes());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void exportAllVox(final File vdf) throws IOException {
        System.out.println(vdf);
        HoTTDecoder.decodeVDF(vdf).getVoiceData().forEach(v -> {
            System.out.println(v.getName());
            final File vox = new File(vdf.getParentFile(), v.getName() + ".vox");
            try {
                Files.write(vox.toPath(), v.getRawData());
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public static void exportAllWav(final File vdf) throws IOException {
        System.out.println(vdf);
        final VoiceFile voiceFile = HoTTDecoder.decodeVDF(vdf);
        final List<VoiceData> list = voiceFile.getVoiceData();
        list.stream().forEach(v -> {
            System.out.println(v.getName());
            final File wav = new File(vdf.getParentFile(), v.getName() + ".wav");
            try {
                v.writeWav(wav);
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public static void main(final String[] args) throws IOException, LineUnavailableException, InterruptedException {
        // final File mc32_dir = new File("C:/Users/olive/Google Drive/Graupner/Official Version/33112_33116_33124_33020_33028_33032/SD card/Firmware/mc-32");
        // final File mc32_vdf = new File(mc32_dir, "Voice3_mc32_DE.vdf");

        // dump(mc32_vdf);
        // play(mc32_vdf);
        // exportWAV(mc32_vdf);

        // final File mc28_dir = new File("C:/Users/olive/Google Drive/Graupner/Official Version/33112_33116_33124_33020_33028_33032/SD card/Firmware/mc-28");
        // final File mc28_vdf = new File(mc28_dir, "mc-28_German.vdf");

        // dump(mc28_vdf);
        // play(mc28_vdf);
        // exportWAV(mc28_vdf);

        // final File desktop = new File("C:/Users/olive/Desktop");
        // for (final File file : desktop.listFiles((dir, name) -> name.endsWith(".vdf")))
        // dump(file);

        // final File sounds = new File("C:/Users/olive/Google Drive/Weatronic/Sounds/Sprache_BAT60_Anna");
        // Stream.of(sounds.listFiles()).filter(f -> f.getName().endsWith(".mp3")).forEach(Player::play);
        // Stream.of(sounds.listFiles()).filter(f -> f.getName().endsWith(".ogg")).forEach(Player::play);
        // Stream.of(sounds.listFiles()).filter(f -> f.getName().endsWith(".wav")).forEach(Player::play);

        final File vdfDir = new File("C:/Users/olive/Google Drive/Graupner/VDFEditor/Voice Files/VoiceFile");
        Stream.of(vdfDir.listFiles()).filter(s -> s.getName().endsWith(".vdf")).forEach(VDFDump::dump);

        // File vdfDir = new File("C:/Users/olive/Google Drive/Graupner/Official Version/33112_33116_33124_33020_33028_33032/SD card/HoTT_Voice_Files");
        // Stream.of(vdfDir.listFiles()).filter(s -> s.getName().endsWith(".vdf")).forEach(VDFDump::dump);

        // final File vdfDir = new File("C:/Users/olive/Desktop");
        // Stream.of(vdfDir.listFiles()).filter(s -> s.getName().endsWith(".vdf")).forEach(VDFDump::dump);
    }

    public static void playAll(final File vdf) {
        System.out.println(vdf);
        try {
            HoTTDecoder.decodeVDF(vdf).getVoiceData().forEach(VoiceData::play);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
