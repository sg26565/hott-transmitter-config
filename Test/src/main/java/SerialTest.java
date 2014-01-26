import gde.model.enums.ModelType;
import gde.model.winged.WingedModel;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.internal.BaseResponse;
import de.treichels.hott.internal.HoTTSerialPort;
import de.treichels.hott.internal.ReadModelInformation;
import de.treichels.hott.internal.ReadModelMemory;
import de.treichels.hott.internal.ServoPositions;

public class SerialTest {
  public static void main(final String[] args) throws NoSuchPortException, UnsupportedCommOperationException, IOException, PortInUseException,
      InterruptedException {
    final List<String> ports = HoTTSerialPort.getAvailablePorts();

    System.out.println(ports);

    HoTTSerialPort port = null;
    try {
      port = new HoTTSerialPort(ports.get(0));

      {
        final ServoPositions.Response response = (ServoPositions.Response) port.doCommand(new ServoPositions());
        for (int i = 0; i < 4; i++) {
          for (int j = 0; j < 8; j++) {
            System.out.printf(" %d", response.getPosition(i * 8 + j));
          }

          System.out.println();
        }
      }

      final ReadModelInformation cmd = new ReadModelInformation();
      cmd.setModelNumber(19);
      final ReadModelInformation.Response modelInformation = (ReadModelInformation.Response) port.doCommand(cmd);

      if (modelInformation.getModelType() != ModelType.Unknown) {
        System.out.printf("%d: %s, %d, %s, %s\n", 20, modelInformation.getTransmitterType(), modelInformation.getVersion(), modelInformation.getModelType(),
            modelInformation.getModelName());
      }

      final ReadModelMemory cmd2 = new ReadModelMemory();
      final byte[] modelData = new byte[12288];
      for (int i = 0; i < 6; i++) {
        final int offset = 2048 * i;
        cmd2.setAddress(offset);
        final BaseResponse response = port.doCommand(cmd2);
        System.arraycopy(response.getData(), 0, modelData, offset, response.getLen());
      }

      final ByteArrayInputStream is = new ByteArrayInputStream(modelData);
      final WingedModel model = (WingedModel) HoTTDecoder.decode(modelInformation.getModelType(), modelInformation.getModelName(), is);
      System.out.printf("TransmitterId: 0x%x, MemoryVersion: %d\n", model.getTransmitterId(), model.getMemoryVersion());
    } finally {
      if (port != null) {
        port.close();
      }
    }
  }
}