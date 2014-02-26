import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.model.serial.FileInfo;
import gde.model.serial.ModelInfo;
import gde.model.serial.SerialPort;
import gde.model.serial.SerialPortDefaultImpl;
import gde.model.serial.TxInfo;
import gde.util.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.internal.BaseCommand;
import de.treichels.hott.internal.ChangeDir;

public class SerialTest {
  private static SerialPort     portImpl;
  private static HoTTSerialPort port;

  private static void list(final String path) throws IOException {
    port.doCommand(new ChangeDir(path));

    final List<String> subdirs = new ArrayList<String>();
    System.out.printf("\nContents of %s:\n", path);
    for (final FileInfo info : port.listDir(path)) {
      final String name = path + (path.endsWith("/") ? "" : "/") + info.getName();

      System.out.printf("%1$s %2$8d %3$tF %3$tT %4$s\n", info.getType().toString().substring(0, 1), info.getSize(), info.getModifyDate(), info.getName());

      switch (info.getType()) {
      case Dir:
        subdirs.add(name);
        break;

      case File:
        final String fileName = info.getName();
        if (fileName.endsWith(".mdl")) {
          ModelType type;
          switch (fileName.charAt(0)) {
          case 'a':
            type = ModelType.Winged;
            break;

          case 'h':
            type = ModelType.Helicopter;
            break;

          default:
            throw new IOException("invalid model type");
          }

          final String modelName = fileName.substring(1, fileName.length() - 4);

          final byte[] data = port.readFile(name);
          final ByteArrayInputStream is = new ByteArrayInputStream(data);
          final BaseModel model = HoTTDecoder.decodeStream(type, modelName, is);
          if (model.isBound()) {
            System.out.printf("TransmitterID: %#x, ModelName: %s, ReceiverID: %#x\n", model.getTransmitterId(), model.getModelName(),
                model.getReceiver()[0].getRfid());
          } else {
            System.out.printf("TransmitterID: %#x, ModelName: %s, unbound\n", model.getTransmitterId(), model.getModelName());
          }
        }
      }
    }

    for (final String subdir : subdirs) {
      list(subdir);
    }
  }

  public static void main(final String[] args) throws Exception {
    final List<String> ports = SerialPortDefaultImpl.getAvailablePorts();
    System.out.println(ports);

    portImpl = new SerialPortDefaultImpl(ports.get(0));
    port = new HoTTSerialPort(portImpl);

    port.writeScreen("Test", "1234567890123456789012345678901234567890abcde");
    Thread.sleep(3000);
    port.closeScreen();

    final ModelInfo[] modelInfos = port.getAllModelInfos();
    for (final ModelInfo info : modelInfos) {
      if (info.getModelType() == ModelType.Unknown) {
        continue;
      }

      System.out.println(info);

      final byte[] data = port.getModelData(info);
      final ByteArrayInputStream is = new ByteArrayInputStream(data);
      final BaseModel model = HoTTDecoder.decodeStream(info.getModelType(), info.getModelName(), is);
      if (model.isBound()) {
        System.out.printf("TransmitterID: %#x, ModelName: %s, ReceiverID: %#x\n", model.getTransmitterId(), model.getModelName(),
            model.getReceiver()[0].getRfid());
      } else {
        System.out.printf("TransmitterID: %#x, ModelName: %s, unbound\n", model.getTransmitterId(), model.getModelName());
      }
    }

    final TxInfo info = port.getTxInfo();
    System.out.println(info);

    final byte[] data1 = port.readMemory(0, 0x48);
    System.out.println(Util.dumpData(data1));

    try {
      port.createDir("/test");
    } catch (final IOException e) {
      // ignore
    }
    port.writeFile("/test/foo", "Hello, World!".getBytes());

    list("/");

    final String hello = new String(port.readFile("/test/foo"));
    System.out.println(hello);

    port.deleteFile("/test/foo");
    port.deleteFile("/test");

    port.close();
  }

  private static void test(final BaseCommand cmd) throws IOException {
    System.out.println(cmd);
    System.out.println(port.doCommand(cmd));
  }
}