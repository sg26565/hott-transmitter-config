import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.internal.decoder.VDFDecoder;
import gde.model.serial.JSSCSerialPort;
import gde.model.voice.Player;
import gde.model.voice.VoiceData;
import gde.model.voice.VoiceFile;
import gde.model.voice.VoiceFileInfo;
import gde.model.voice.VoiceInfo;

public class ADPCMTest {
    private static void getADPCMInfo(final HoTTSerialPort port, final boolean user) throws IOException {
        final String type = user ? "User" : "System";

        final VoiceInfo voiceInfo = port.getVoiceInfo(user);
        System.out.printf("%s: flashSize=%dk, sectorCount=%d, sectorSize=%dk, voiceVersion=%d, infoSize=%dk, maxDataSize=%dk%n", type,
                voiceInfo.getFlashSize() / 1024, voiceInfo.getSectorCount(), voiceInfo.getSectorSize() / 1024, voiceInfo.getVoiceVersion(),
                voiceInfo.getInfoSize() / 1024, voiceInfo.getMaxDataSize() / 1024);

        for (int i = 0; i < (user ? 40 : 432); i++) {
            final VoiceFileInfo info = port.getVoiceFileInfo(i, user);
            if (info == null) break;

            System.out.printf("%s: size=0x%04x, baseAddress=0x%06x, index=%d/%d, sampleRate=%d, name=%s%n", type, info.getSize(), info.getBaseAddress(),
                    info.getIndex(), info.getReverseIndex(), info.getSampleRate(), info.getName());
            final VoiceData vd = port.getVoiceData(info);

            Player.waitDone();
            vd.play();
            Player.waitDone();
        }
    }

    public static void main(final String[] args) throws IOException {
        System.setProperty("debug", "true");

        try (final HoTTSerialPort port = new HoTTSerialPort(new JSSCSerialPort("COM5"))) {
            port.open();

            // getADPCMInfo(port, true);
            getADPCMInfo(port, false);

            // sendVoiceFile(port, new File("C:/Users/olive/Desktop/user.vdf"));
            sendVoiceFile(port, new File("C:/Users/olive/Desktop/system.vdf"));
        }
    }

    private static void sendVoiceFile(final HoTTSerialPort port, final File file) throws IOException {
        final byte[] data = Files.readAllBytes(file.toPath());
        final VoiceFile vdf = VDFDecoder.decodeVDF(data);
        port.sendVoiceFile(vdf, (i, c) -> System.out.printf("%d/%d%n", i, c));
    }
}
