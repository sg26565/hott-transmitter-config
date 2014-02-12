import gde.model.serial.SerialPortDefaultImpl;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.List;

import de.treichels.hott.HoTTTransmitter;
import de.treichels.hott.internal.HoTTSerialPort;
import de.treichels.hott.internal.PrepareListMdl;
import de.treichels.hott.internal.QueryTxInfo;
import de.treichels.hott.internal.ReadModelInformation;
import de.treichels.hott.internal.ServoPositions;

public class SerialTest {
  public static void main(final String[] args) throws NoSuchPortException, UnsupportedCommOperationException, IOException, PortInUseException,
      InterruptedException {
    final List<String> ports = SerialPortDefaultImpl.getAvailablePorts();

    System.out.println(ports);

    try {
      HoTTTransmitter.setSerialPortImpl(new SerialPortDefaultImpl(ports.get(0)));

      // System.out.println(HoTTSerialPort.getInstance().doCommand(new
      // FastModeStop()));
      // System.out.println(HoTTSerialPort.getInstance().doCommand(new
      // ClearScreen()));
      // System.out.println(HoTTSerialPort.getInstance().doCommand(new
      // ResetScreen()));
      // System.out.println(HoTTSerialPort.getInstance().doCommand(new
      // WriteScreen(0, "test")));
      // System.out.println(HoTTSerialPort.getInstance().doCommand(new
      // CloseScreen()));

      System.out.println(HoTTSerialPort.getInstance().doCommand(new ReadModelInformation(0)));
      System.out.println(HoTTSerialPort.getInstance().doCommand(new ServoPositions()));
      System.out.println(HoTTSerialPort.getInstance().doCommand(new QueryTxInfo()));
      System.out.println(HoTTSerialPort.getInstance().doCommand(new PrepareListMdl()));
    } finally {
      HoTTTransmitter.closeConnection();
    }
  }
}