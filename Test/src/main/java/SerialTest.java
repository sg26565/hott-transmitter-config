import gde.model.serial.SerialPortDefaultImpl;
import gde.model.serial.TxInfo;
import gde.model.winged.WingedModel;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.List;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.HoTTTransmitter;

public class SerialTest {
  private static void dump(final byte[] data) {
    if (data == null) {
      return;
    }

    final int len = data.length;
    int addr = 0;
    while (addr < len) {
      System.out.printf("0x%04x:", addr);

      for (int i = 0; i < 16 && addr < len; i++) {
        final byte b = data[addr++];
        final char c = (char) (b & 0xff);
        if (c >= 0x20 && c <= 0x7e) {
          System.out.printf(" %c ", c);

        } else {
          System.out.printf(" %02x", b);
        }
      }

      System.out.println();
    }
  }

  public static void main(final String[] args) throws NoSuchPortException, UnsupportedCommOperationException, IOException, PortInUseException,
      InterruptedException {
    final List<String> ports = SerialPortDefaultImpl.getAvailablePorts();

    System.out.println(ports);

    // HoTTSerialPort port = null;
    // try {
    // port = new HoTTSerialPort(ports.get(0));

    // {
    // final ServoPositions.Response response = (ServoPositions.Response)
    // port.doCommand(new ServoPositions());
    // for (int i = 0; i < 4; i++) {
    // for (int j = 0; j < 8; j++) {
    // System.out.printf(" %d", response.getPosition(i * 8 + j));
    // }
    //
    // System.out.println();
    // }
    // }
    //

    try {
      HoTTTransmitter.setSerialPortImpl(new SerialPortDefaultImpl(ports.get(0)));
      final TxInfo info = HoTTTransmitter.getTxInfo();

      System.out.println(info.getTransmitterType());
      System.out.println(info.getAppVersion());
      System.out.println(info.getMemoryVersion());
      System.out.println(info.getYear());
      System.out.println(info.getName());
      System.out.println(info.getVendor());

      final int[] positions = HoTTTransmitter.servoPositions();
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 8; j++) {
          System.out.printf(" %d", positions[i * 8 + j]);
        }
        System.out.println();
      }

      WingedModel model = (WingedModel) HoTTDecoder.decodeMemory(0);
      System.out.printf("ModelName: %s, TransmitterId: 0x%x, MemoryVersion: %d\n", model.getModelName(), model.getTransmitterId(), model.getMemoryVersion());

      model = (WingedModel) HoTTDecoder.decodeMemory(19);
      System.out.printf("ModelName: %s, TransmitterId: 0x%x, MemoryVersion: %d\n", model.getModelName(), model.getTransmitterId(), model.getMemoryVersion());

      HoTTTransmitter.writeScreen(new String[] { "a234567890123456789012345678901234567890", "b234567890123456789012345678901234567890",
          "c234567890123456789012345678901234567890", "d234567890123456789012345678901234567890", "e234567890123456789012345678901234567890",
          "f234567890123456789012345678901234567890" });
      Thread.sleep(5000);
      HoTTTransmitter.closeScreen();
    } finally {
      HoTTTransmitter.closeConnection();
    }
  }
}