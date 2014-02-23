import gde.model.serial.FileMode;
import gde.model.serial.FileType;
import gde.model.serial.SerialPort;
import gde.model.serial.SerialPortDefaultImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.treichels.hott.HoTTSerialPort;
import de.treichels.hott.internal.BaseCommand;
import de.treichels.hott.internal.ChangeDir;
import de.treichels.hott.internal.GetFileInfo;
import de.treichels.hott.internal.ListDir;
import de.treichels.hott.internal.ListDir.Response;
import de.treichels.hott.internal.ReadFile;
import de.treichels.hott.internal.StartFileXfer;
import de.treichels.hott.internal.StopFileXfer;

public class SerialTest {
  private static SerialPort     portImpl;
  private static HoTTSerialPort port;

  private static void list(final String path) throws IOException {
    port.doCommand(new ChangeDir(path));

    final List<String> subdirs = new ArrayList<String>();
    System.out.printf("\nContents of %s:\n", path);
    while (true) {
      final ListDir.Response response = (Response) port.doCommand(new ListDir());
      if (response.getLen() == 0) {
        break;
      }

      System.out.printf("%1$-4s %2$c %3$tF %3$tT %4$s\n", response.getFileType(), response.getData()[1], response.getModifyDate(), response.getName());

      final String name = path + (path.endsWith("/") ? "" : "/") + response.getName();

      if (response.getFileType() == FileType.Dir) {
        subdirs.add(name);
      }

      if (response.getFileType() == FileType.Dir || response.getData()[1] == 'A') {
        final GetFileInfo.Response response2 = (GetFileInfo.Response) port.doCommand(new GetFileInfo(name));
        System.out.printf("%2$6d %1$tF %1$tT %3$s\n", response2.getModifyDate(), response2.getSize(), response2.getName());
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
    // for (int i = 0; i < 512; i++) {
    // test(new ReadTransmitterMemory(0x800 * i));
    // }

    // mx-16
    // test(new ReadTransmitterMemory(0x202c, 20 + 20 * 9));

    // mc-32
    // test(new ReadTransmitterMemory(0x200d, 80 + 80 * 13));

    // final ModelInfo[] modelInfos = port.getAllModelInfos();
    // for (final ModelInfo info : modelInfos) {
    // if (info.getModelType() == ModelType.Unknown) {
    // continue;
    // }
    //
    // System.out.println(info);
    //
    // final byte[] data1 = port.getModelData(info);
    // final byte[] data2 = port.getModelData(info.getModelNumber());
    //
    // for (int i = 0; i < data1.length; i++) {
    // if (data1[i] != data2[i]) {
    // System.out.printf("mismatch at 0x%04x: 0x%02x <> 0x%02x\n", i,
    // data1[i], data2[i]);
    // }
    // }
    // }

    // final TxInfo info = port.getTxInfo();
    // System.out.println(info);
    //
    // final byte[] data1 = port.readMemory(0, 0x3000);
    //
    // final BufferedReader reader = new BufferedReader(new
    // InputStreamReader(System.in));
    // while (true) {
    // final byte[] data2 = port.readMemory(0, 0x3000);
    //
    // for (int i = 0; i < data1.length; i++) {
    // if (data1[i] != data2[i]) {
    // System.out.printf("mismatch at 0x%04x: 0x%02x <> 0x%02x\n", i, data1[i],
    // data2[i]);
    // }
    // }
    //
    // System.out.println("ok");
    // reader.readLine();
    // }

    // test(new PrepareFileTransfer());
    // test(new SelectSDCard());

    // test(new ChangeDir("/"));
    // while (true) {
    // final ListDir.Response response = (Response) port.doCommand(new
    // ListDir());
    // if (response.getLen() == 0) {
    // break;
    // }
    //
    // System.out.printf("%1$-4s %2$c %3$tF %3$tT %4$s\n",
    // response.getFileType(), response.getData()[1], response.getModifyDate(),
    // response.getName());
    // }
    //
    // test(new ChangeDir("/Models"));
    // while (true) {
    // final ListDir.Response response = (Response) port.doCommand(new
    // ListDir());
    // if (response.getLen() == 0) {
    // break;
    // }
    //
    // System.out.printf("%1$-4s %2$c %3$tF %3$tT %4$s\n",
    // response.getFileType(), response.getData()[1], response.getModifyDate(),
    // response.getName());
    // }
    //
    // test(new ChangeDir("/Models/mc-32"));
    // while (true) {
    // final ListDir.Response response = (Response) port.doCommand(new
    // ListDir());
    // if (response.getLen() == 0) {
    // break;
    // }
    //
    // System.out.printf("%1$-4s %2$c %3$tF %3$tT %4$s\n",
    // response.getFileType(), response.getData()[1], response.getModifyDate(),
    // response.getName());
    // }

    // list("/");
    // test(new ChangeDir("/Models/mc-32"));
    // test(new ListDir());
    // test(new GetFileInfo("/Models/mc-32"));
    // System.out.println(port.getFileInfo("/Models/mc-32/aMerlin.mdl"));
    // for (final FileInfo fileInfo : port.listDir("/Models/mx-16")) {
    // System.out.println(fileInfo);
    // }

    // test(new DeleteFile("/Models/mx-16/ht3.mdl"));

    // test(new MakeDir("/test"));
    // test(new MakeDir("/test/a"));
    // test(new MakeDir("/test/a/b"));
    //
    // list("/");

    // test(new DeleteFile("/test/a/b"));
    // test(new DeleteFile("/test/a"));
    // test(new DeleteFile("/test"));
    //
    // list("/Models");

    System.out.println(port.getFileInfo("/Models/mx-16/aMERLIN.mdl"));
    // test(new SelectSDCard());
    // test(new PrepareFileTransfer());
    test(new StartFileXfer("/Models/mx-16/aMERLIN.mdl", FileMode.Read));
    test(new ReadFile(0x800));
    test(new ReadFile(0x800));
    test(new ReadFile(0x800));
    test(new ReadFile(0x800));
    test(new StopFileXfer());
  }

  private static void test(final BaseCommand cmd) throws IOException {
    System.out.println(cmd);
    System.out.println(port.doCommand(cmd));
  }
}