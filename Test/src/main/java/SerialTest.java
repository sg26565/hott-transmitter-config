import gde.model.serial.SerialPortDefaultImpl;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.List;

import de.treichels.hott.HoTTTransmitter;
import de.treichels.hott.internal.AbstractCommand;
import de.treichels.hott.internal.ClearScreen;
import de.treichels.hott.internal.CloseScreen;
import de.treichels.hott.internal.FastModeStop;
import de.treichels.hott.internal.HoTTSerialPort;
import de.treichels.hott.internal.PrepareListMdl;
import de.treichels.hott.internal.QueryMdlNames;
import de.treichels.hott.internal.QueryTxInfo;
import de.treichels.hott.internal.ReadModelInformation;
import de.treichels.hott.internal.ReadModelMemory;
import de.treichels.hott.internal.ResetScreen;
import de.treichels.hott.internal.ServoPositions;
import de.treichels.hott.internal.WriteScreen;

public class SerialTest {
  public static void main(final String[] args) throws NoSuchPortException, UnsupportedCommOperationException, IOException, PortInUseException,
      InterruptedException {
    final List<String> ports = SerialPortDefaultImpl.getAvailablePorts();
    System.out.println(ports);

    try {
      HoTTTransmitter.setSerialPortImpl(new SerialPortDefaultImpl(ports.get(0)));

      test(new FastModeStop());
      test(new ClearScreen());
      test(new ResetScreen());
      test(new WriteScreen(0, "test"));
      test(new CloseScreen());

      test(new ReadModelInformation(0));
      test(new ReadModelMemory(4096));

      test(new ServoPositions());

      test(new QueryTxInfo());

      test(new PrepareListMdl());
      test(new QueryMdlNames());
    } finally {
      HoTTTransmitter.closeConnection();
    }
  }

  private static void test(final AbstractCommand cmd) throws IOException {
    System.out.println(cmd);
    System.out.println(HoTTSerialPort.getInstance().doCommand(cmd));
  }
}