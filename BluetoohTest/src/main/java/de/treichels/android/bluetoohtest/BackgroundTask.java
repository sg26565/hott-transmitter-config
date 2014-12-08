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
import android.os.ParcelUuid;

public class BackgroundTask extends AsyncTask<Void, String, Void> {
  private enum RC {
    ACK, NACK, ERROR, CRC_ERROR, BUSY, CANCELLED, NO_RESPONSE;
  };

  private static final byte STX      = 0x00;
  private byte              sequence = 1;

  @Override
  protected Void doInBackground(final Void... params) {
    try {
      print("Get local Bluetooth adapter ... ");
      final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
      if (adapter == null) {
        println("no adapter!");
        return null;
      } else {
        println("ok.");
      }

      if (isCancelled()) {
        return null;
      }

      printf("Is Bluetooth enabled: %b\n", adapter.isEnabled());
      if (!adapter.isEnabled()) {
        println("Bluetooth is not enabled! Enabling now. Please re-run test.");
        adapter.enable();
        return null;
      }

      printf("local Adapter Name: %s\n", adapter.getName());
      printf("local Adapter Address: %s\n", adapter.getAddress());

      final Set<BluetoothDevice> devices = adapter.getBondedDevices();
      printf("Number of bounded devices: %d\n", devices.size());

      for (final BluetoothDevice device : devices) {
        if (isCancelled()) {
          return null;
        }

        printf("\nRemote Device Name: %s\n", device.getName());
        printf("remote Address: %s\n", device.getAddress());
        printf("BoundState: %d\n", device.getBondState());

        final BluetoothClass btclass = device.getBluetoothClass();

        printf("DeviceClass: %s\n", btclass.getDeviceClass());
        printf("MajorDeviceClass: %s\n", btclass.getMajorDeviceClass());

        final ParcelUuid[] uuids = device.getUuids();
        printf("Number of Uuids: %d\n", uuids.length);

        for (final ParcelUuid parcelUuid : uuids) {
          final UUID uuid = parcelUuid.getUuid();
          printf("\n%s/%s\n", device.getName(), uuid);

          try {
            if (isCancelled()) {
              return null;
            }

            int retryCount = 3;
            while (retryCount > 0) {
              printf("  Open insecure socket (retry %d) ... ", 3 - retryCount);
              final BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
              println("ok");

              if (isCancelled()) {
                if (socket != null) {
                  socket.close();
                }
                return null;
              }

              final RC rc = sendCommand(socket);
              if (socket != null) {
                socket.close();
              }
              if (rc == RC.ACK) {
                break;
              } else {
                retryCount--;
                printf(" %s\n", rc.toString());
              }
            }
          } catch (final Throwable t) {
            printf("failed: %s\n", t.getMessage());
          }

          try {
            Thread.sleep(5000);
          } catch (final InterruptedException e1) {
            // ignore
          }

          try {
            if (isCancelled()) {
              return null;
            }

            int retryCount = 3;
            while (retryCount > 0) {
              printf("\n  Open secure socket (retry %d) ... ", 3 - retryCount);
              final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
              println("ok");

              if (isCancelled()) {
                if (socket != null) {
                  socket.close();
                }
                return null;
              }

              final RC rc = sendCommand(socket);
              socket.close();
              if (rc == RC.ACK) {
                break;
              } else {
                retryCount--;
                printf(" %s\n", rc.toString());
              }
            }
          } catch (final IOException e) {
            printf("failed: %s\n", e.getMessage());
          }
        }
      }

      println("\nDone.");
    } catch (final Throwable t) {

    }

    return null;
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

  private RC sendCommand(final BluetoothSocket socket) throws IOException {
    byte sqn1 = sequence++;
    byte sqn2 = (byte) (sqn1 ^ 0xff);
    short len = 0;
    byte tcmd = 0x00;
    final byte pcmd = 0x11;

    // skip 255 and 0
    if (sequence == (byte) 0xFF) {
      sequence = 1;
    }

    print("    connect socket ... ");
    socket.connect();
    println("ok");

    if (isCancelled()) {
      return RC.CANCELLED;
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

    int retryCount = 3;

    if (isCancelled()) {
      return RC.CANCELLED;
    }

    printf("    waiting for reply (retry %d) ", 3 - retryCount);
    final InputStream is = socket.getInputStream();
    while (is.available() == 0) {
      print(".");

      if (retryCount == 0) {
        return RC.NO_RESPONSE;
      }

      try {
        Thread.sleep(1000);
      } catch (final InterruptedException e) {}

      retryCount--;
    }
    println(" ok");

    if (isCancelled()) {
      return RC.CANCELLED;
    }

    print("   reading data ... ");
    final HoTTReader reader = new HoTTReader(is);

    if (reader.readUnsignedByte() != STX) {
      println("No Stx");
      return RC.ERROR;
    }

    sqn1 = reader.readByte();
    sqn2 = reader.readByte();

    if (sqn1 != (byte) (sqn2 ^ 0xff)) {
      printf("WrongSqn2 %d/%d\n", sqn1, sqn2);
      return RC.ERROR;
    }

    reader.resetCRC();
    len = reader.readShort();
    tcmd = reader.readByte();
    final byte rc = reader.readByte();
    printf("Result Code: %d\n", rc);

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
        printf("      ChecksumError expected: %d, actual: %d\n", expected, crc);
        return RC.CRC_ERROR;
      } else {
        println("      Checksum ok");
      }

      return RC.ACK;

    case 3:
      return RC.ERROR;

    case 4:
      return RC.CRC_ERROR;

    case 5:
      return RC.BUSY;
    }

    return null;
  }
}
