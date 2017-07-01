import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;

import de.treichels.hott.HoTTDecoder;
import gde.model.voice.VoiceFile;
import gde.model.voice.VoiceData;
import gde.util.Util;

public class VDFDump {
    private static void dump(final File vdf) throws IOException {
        final File out = new File(vdf.getParentFile(), vdf.getName().replaceAll(".vdf", ".txt"));
        final byte[] bytes = Files.readAllBytes(vdf.toPath());
        final String dumpData = Util.dumpData(bytes);
        Files.write(out.toPath(), dumpData.getBytes());
        play(vdf);
    }

    public static void main(final String[] args) throws IOException, LineUnavailableException, InterruptedException {
        final File mc32_dir = new File("C:/Users/olive/Google Drive/Graupner/Official Version/33112_33116_33124_33020_33028_33032/SD card/Firmware/mc-32");
        final File mc32_vdf = new File(mc32_dir, "Voice3_mc32_DE.vdf");

        // dump(mc32_vdf);
        play(mc32_vdf);

        final File mc28_dir = new File("C:/Users/olive/Google Drive/Graupner/Official Version/33112_33116_33124_33020_33028_33032/SD card/Firmware/mc-28");
        final File mc28_vdf = new File(mc28_dir, "mc-28_German.vdf");

        // dump(mc28_vdf);
        // play(mc28_vdf);

        final File desktop = new File("C:/Users/olive/Desktop");
        // dump(new File(desktop, "mc32_user.vdf"));
        // dump(new File(desktop, "mc28_user.vdf"));
        // dump(new File(desktop, "mx16_user.vdf"));
        dump(new File(desktop, "mx12_user.vdf"));
        dump(new File(desktop, "mz24_user.vdf"));
    }

    private static void play(final File vdf) throws IOException {
        System.out.println(vdf);
        final VoiceFile voiceFile = HoTTDecoder.decodeVDF(vdf);
        final List<VoiceData> list = voiceFile.getVoiceData();
        list.stream().forEach(v -> {
            System.out.println(v.getName());
            try {
                v.play();
            } catch (LineUnavailableException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
    }
}
