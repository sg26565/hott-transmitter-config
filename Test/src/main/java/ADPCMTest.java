import java.io.IOException;
import java.nio.ByteBuffer;

import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.internal.commands.ADPCMPlay;
import de.treichels.hott.internal.commands.ADPCMReadData;
import de.treichels.hott.internal.commands.ADPCMReadDevice;
import de.treichels.hott.internal.commands.ADPCMReadDevice.Response;
import de.treichels.hott.internal.commands.ADPCMReadFileInfo;
import de.treichels.hott.internal.commands.ADPCMReadInfoSectorCount;
import de.treichels.hott.internal.io.BaseResponse;
import gde.model.serial.JSSCSerialPort;
import gde.model.serial.ResponseCode;

public class ADPCMTest {
    private static void getADPCMInfo(final HoTTSerialPort port, final boolean user) throws IOException {
        final ADPCMReadDevice.Response r1 = (Response) port.doCommand(new ADPCMReadDevice(user));
        final ADPCMReadInfoSectorCount.Response r2 = (de.treichels.hott.internal.commands.ADPCMReadInfoSectorCount.Response) port
                .doCommand(new ADPCMReadInfoSectorCount(user));

        final int flashSize = r1.getFlashSize();
        final int sectorCount = r1.getSectorCount();
        final int sectorSize = r1.getSectorSize();
        final int voiceVersion = r1.getVoiceVersion();
        final int infoSize = voiceVersion < 2500 ? 0x2000 : r2.getInfoSectorCount() * sectorSize;
        final int dataSize = flashSize - infoSize;

        System.out.printf("%s: flashSize=%dk, sectorCount=%d, sectorSize=%dk, voiceVersion=%d, infoSize=%dk, dataSize=%dk%n", user ? "User" : "System",
                flashSize / 1024, sectorCount, sectorSize / 1024, voiceVersion, infoSize / 1024, dataSize / 1024);

        for (int i = 0; i < (user ? 40 : 432); i++) {
            final ADPCMReadFileInfo.Response r3 = (de.treichels.hott.internal.commands.ADPCMReadFileInfo.Response) port
                    .doCommand(new ADPCMReadFileInfo(i, user));

            if (r3.getResponseCode() == ResponseCode.NACK) break;

            System.out.printf("%s: %s%n", user ? "User" : "System", r3.dumpData());

            final int address = r3.getBaseAddress();
            final int size = r3.getSize();
            final int packetSize = 0x800;
            final int count = size / packetSize;
            final int rest = size % packetSize;

            final ByteBuffer bb = ByteBuffer.allocate(size);
            for (int j = 0; j < count; j++) {
                final int offset = j * packetSize;
                final BaseResponse r4 = port.doCommand(new ADPCMReadData(address + offset, packetSize, user));
                final byte[] data = r4.getData();
                bb.put(data);
            }

            final int offset = count * packetSize;
            final BaseResponse r4 = port.doCommand(new ADPCMReadData(address + offset, rest, user));
            final byte[] data = r4.getData();
            bb.put(data);

            port.doCommand(new ADPCMPlay(i));
            // final VoiceData vd = new VoiceData(r3.getName(), bb.array());
            // Player.waitDone();
            // vd.play();
        }
    }

    public static void main(final String[] args) throws IOException {
        System.setProperty("debug", "true");

        try (final HoTTSerialPort port = new HoTTSerialPort(new JSSCSerialPort("COM5"))) {
            port.open();

            getADPCMInfo(port, true);
            getADPCMInfo(port, false);
        }
    }
}
