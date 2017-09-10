import java.io.IOException;

import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.internal.commands.ADPCMReadDevice;
import de.treichels.hott.internal.commands.ADPCMReadDevice.Response;
import de.treichels.hott.internal.commands.ADPCMReadInfoSectorCount;
import gde.model.serial.JSSCSerialPort;

public class ADPCMTest {
    private static void getADPCMInfo(final HoTTSerialPort port, final boolean user) throws IOException {
        final ADPCMReadDevice.Response r1 = (Response) port.doCommand(new ADPCMReadDevice(user));
        final ADPCMReadInfoSectorCount.Response r2 = (de.treichels.hott.internal.commands.ADPCMReadInfoSectorCount.Response) port
                .doCommand(new ADPCMReadInfoSectorCount(user));

        System.out.println(r1);
        System.out.println(r2);

        final int flashSize = r1.getFlashSize();
        final int sectorCount = r1.getSectorCount();
        final int sectorSize = r1.getSectorSize();
        final int voiceVersion = r1.getVoiceVersion();
        final int infoSize = voiceVersion < 2500 ? 0x2000 : r2.getInfoSectorCount() * sectorSize;
        final int dataSize = flashSize - infoSize;

        System.out.printf("flashSize=%dk, sectorCount=%d, sectorSize=%dk, voiceVersion=%d, infoSize=%dk, dataSize=%dk", flashSize / 1024, sectorCount,
                sectorSize / 1024, voiceVersion, infoSize / 1024, dataSize / 1024);
    }

    public static void main(final String[] args) throws IOException {
        try (final HoTTSerialPort port = new HoTTSerialPort(new JSSCSerialPort("COM5"))) {
            port.open();

            getADPCMInfo(port, true);
            getADPCMInfo(port, false);
        }
    }
}
