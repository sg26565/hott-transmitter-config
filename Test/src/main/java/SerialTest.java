import gde.model.serial.SerialPortDefaultImpl;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.List;

import de.treichels.hott.HoTTTransmitter;
import de.treichels.hott.internal.BaseCommand;
import de.treichels.hott.internal.HoTTSerialPort;
import de.treichels.hott.internal.ReadTransmitterMemory;

public class SerialTest {
  public static void main(final String[] args) throws NoSuchPortException, UnsupportedCommOperationException, IOException, PortInUseException,
      InterruptedException {
    final List<String> ports = SerialPortDefaultImpl.getAvailablePorts();
    System.out.println(ports);

    try {
      HoTTTransmitter.setSerialPortImpl(new SerialPortDefaultImpl(ports.get(0)));

      // test(new FastModeStop());
      // test(new ClearScreen());
      // test(new ResetScreen());
      // test(new WriteScreen(0, "test"));
      // test(new CloseScreen());

      // test(new ReadModelInformation(0));
      // test(new ReadModelMemory(4096));

      // test(new ServoPositions());

      // test(new QueryTxInfo());
      // test(new BaseCommand(0x00, 0x11, 0));

      // test(new PrepareListMdl());
      for (int i = 0; i < 512; i++) {
        test(new ReadTransmitterMemory(0x800 * i));
      }
    } finally {
      HoTTTransmitter.closeConnection();
    }
  }

  private static void test(final BaseCommand cmd) throws IOException {
    System.out.println(cmd);
    System.out.println(HoTTSerialPort.getInstance().doCommand(cmd));
  }
}