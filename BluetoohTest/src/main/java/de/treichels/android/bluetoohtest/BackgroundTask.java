package de.treichels.android.bluetoohtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

public class BackgroundTask extends AsyncTask<Void, String, Void> {
  private static final byte STX                      = 0x00;
  private static final UUID UUID_SERIAL_PORT_PROFILE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  private byte              sequence                 = 1;

  private void checkCanceled() throws InterruptedException {
    if (isCancelled()) {
      throw new InterruptedException("canceled.");
    }
  }

  private void connect(final BluetoothSocket socket) throws IOException, InterruptedException {
    checkCanceled();

    print("    connect socket ... ");
    socket.connect();
    println("ok");
  }

  private void disconnect(final BluetoothSocket socket) throws IOException {
    if (socket != null) {
      print("    close socket ... ");
      socket.close();
      println("ok");
    }
  }

  @Override
  protected Void doInBackground(final Void... params) {
    try {
      final BluetoothAdapter adapter = getBluetoothAdapter();
      enableAdapter(adapter);
      final Set<BluetoothDevice> devices = getRemoteDevices(adapter);

      for (final BluetoothDevice device : devices) {
        getDeviceInfo(device);

        for (int retryCount = 0; retryCount < 3; retryCount++) {
          if (retryCount > 0) {
            printf("retry %d\n", retryCount);
          }

          BluetoothSocket socket = null;

          try {
            socket = openSocket(device);
            adapter.cancelDiscovery();
            connect(socket);
            sendCommand(socket);
            wait4Response(socket);
            readResponse(socket);
            break;
          } catch (final IOException e) {
            printf(" %s\n", e.getMessage());
          } finally {
            disconnect(socket);
          }
        }
      }

      println("\nDone.");
    } catch (final Throwable t) {
      printf("\nError: %s\n", t.getMessage());
    }

    return null;
  }

  private void enableAdapter(final BluetoothAdapter adapter) throws IOException, InterruptedException {
    checkCanceled();

    printf("Is Bluetooth enabled: %b\n", adapter.isEnabled());

    if (!adapter.isEnabled()) {
      println("Bluetooth is not enabled! Enabling now. Please re-run test.");
      adapter.enable();

      throw new IOException();
    }
  }

  private BluetoothAdapter getBluetoothAdapter() throws IOException, InterruptedException {
    checkCanceled();

    print("Get local Bluetooth adapter ... ");

    final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    if (adapter == null) {
      throw new IOException("no bluetooth adapter!");
    }

    println("ok.");

    printf("local Adapter Name: %s\n", adapter.getName());
    printf("local Adapter Address: %s\n", adapter.getAddress());

    return adapter;
  }

  private void getDeviceInfo(final BluetoothDevice device) throws InterruptedException {
    checkCanceled();

    printf("\nRemote Device Name: %s\n", device.getName());
    printf("remote Address: %s\n", device.getAddress());
    printf("BoundState: %d\n", device.getBondState());

    final BluetoothClass btclass = device.getBluetoothClass();

    printf("DeviceClass: %s\n", btclass.getDeviceClass());
    printf("MajorDeviceClass: %s\n", btclass.getMajorDeviceClass());
  }

  private Set<BluetoothDevice> getRemoteDevices(final BluetoothAdapter adapter) throws InterruptedException {
    checkCanceled();

    final Set<BluetoothDevice> devices = adapter.getBondedDevices();
    printf("Number of bounded devices: %d\n", devices.size());
    return devices;
  }

  private BluetoothSocket openSocket(final BluetoothDevice device) throws IOException, InterruptedException {
    checkCanceled();

    print("\n  Open socket ... ");
    final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID_SERIAL_PORT_PROFILE);
    println("ok");

    return socket;
  }

  private void print(final String... string) {
    publishProgress(string);
  }

  private void printf(final String format, final Object... args) {
    print(String.format(format, args));
  }

  private void println(final String string) {
    print(string, "\n");
  }

  private void readResponse(final BluetoothSocket socket) throws IOException, InterruptedException {
    checkCanceled();

    println("   reading data ...");
    final InputStream is = socket.getInputStream();
    final HoTTReader reader = new HoTTReader(is);

    if (reader.readUnsignedByte() != STX) {
      throw new IOException("No Stx");
    }

    final byte sqn1 = reader.readByte();
    final byte sqn2 = reader.readByte();
    if (sqn1 != (byte) (sqn2 ^ 0xff)) {
      throw new IOException(String.format("WrongSqn2 %d/%d\n", sqn1, sqn2));
    }

    reader.resetCRC();
    printf("    Result len: %d\n", reader.readShort());
    printf("    TCMD: %d\n", reader.readByte());
    final byte rc = reader.readByte();
    printf("    Result Code: %d\n", rc);

    switch (rc) {
    case 1:
      println("    Found Hott Transmitter!");
      printf("      transmitter type: %d\n", reader.readInt());
      printf("      firmware version: %d\n", reader.readInt());
      reader.skip(4); // product code repeated
      printf("      year: %d\n", reader.readInt());
      printf("      name: %s\n", reader.readString(16).trim());
      printf("      vendor: %s\n", reader.readString(16).trim());
      reader.skip(1);
      printf("      memory version: %d\n", reader.readInt());

      final int expected = reader.getCRC();
      final int crc = reader.readUnsignedShort();
      if (crc != expected) {
        throw new IOException(String.format("      ChecksumError expected: %d, actual: %d\n", expected, crc));
      } else {
        println("      Checksum ok");
      }
      break;

    case 3:
      throw new IOException("error");

    case 4:
      throw new IOException("crc error");

    case 5:
      throw new IOException("busy");
    }
  }

  private void sendCommand(final BluetoothSocket socket) throws IOException, InterruptedException {
    checkCanceled();

    final byte sqn1 = sequence++;
    final byte sqn2 = (byte) (sqn1 ^ 0xff);
    final short len = 0;
    final byte tcmd = 0x00;
    final byte pcmd = 0x11;

    // skip 255 and 0
    if (sequence == (byte) 0xFF) {
      sequence = 1;
    }

    final OutputStream os = socket.getOutputStream();

    print("    sending command ... ");
    final HoTTWriter writer = new HoTTWriter(os);
    writer.writeUnsignedByte(STX);
    writer.writeUnsignedByte(sqn1);
    writer.writeUnsignedByte(sqn2);
    writer.resetCRC();
    writer.writeUnsignedShort(len);
    writer.writeUnsignedByte(tcmd);
    writer.writeUnsignedByte(pcmd);
    writer.writeUnsignedShort(writer.getCRC());

    os.flush();

    println("ok");
  }

  private void wait4Response(final BluetoothSocket socket) throws IOException {
    final InputStream is = socket.getInputStream();

    print("    waiting for reply ");

    for (int retryCount = 0; is.available() == 0 && retryCount < 3; retryCount++) {
      print(".");
      try {
        Thread.sleep(1000);
      } catch (final InterruptedException e) {}
    }

    if (is.available() == 0) {
      throw new IOException("no response!");
    }

    println(" ok");
  }
}
