package de.treichels.hott.ui.android;

import gde.model.HoTTException;
import gde.model.serial.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

public class AndroidUsbSerialPortImplementation implements SerialPort {
  private class UsbSerialDriverInputStream extends InputStream {
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private int          index  = 0;
    private int          len    = 0;

    @Override
    public int available() throws IOException {
      int available = len - index;

      if (available == 0) {
        available = fetch();
      }

      return available;
    }

    private int fetch() throws IOException {
      index = 0;
      len = driver.read(buffer, IO_TIMEOUT);

      if (len < 0) {
        Log.i("UsbSerialDriver.read()", "no data");
        len = 0;
      } else {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
          builder.append(String.format("%02x ", buffer[i]));
        }
        Log.i("UsbSerialDriver.read()", builder.toString());
      }

      return len;
    }

    @Override
    public int read() throws IOException {
      if (available() == 0) {
        fetch();
      }

      return index < len ? buffer[index++] & 0xff : -1;
    }
  }

  private class UsbSerialDriverOutputStream extends OutputStream {
    private final byte[] buffer = new byte[BUFFER_SIZE];
    private int          len    = 0;

    @Override
    public void flush() throws IOException {
      if (len > 0) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
          builder.append(String.format("%02x ", buffer[i]));
        }

        byte[] b;
        if (len == BUFFER_SIZE) {
          b = buffer;
        } else {
          b = new byte[len];
          System.arraycopy(buffer, 0, b, 0, len);
        }

        final int rc = driver.write(b, IO_TIMEOUT);
        builder.append(": ").append(rc);

        Log.i("bulkUsbSerialDriver.write()", builder.toString());

        len = 0;
      }
    }

    @Override
    public void write(final int oneByte) throws IOException {
      buffer[len++] = (byte) oneByte;

      if (len == BUFFER_SIZE) {
        flush();
      }
    }
  }

  private static final int      IO_TIMEOUT   = 1000;
  private static final int      BUFFER_SIZE  = 2064;

  private final UsbSerialDriver driver;
  private boolean               open         = false;
  private InputStream           inputStream  = null;
  private OutputStream          outputStream = null;

  /**
   * @param device
   * @param interface1
   */
  public AndroidUsbSerialPortImplementation(final UsbSerialDriver driver) {
    this.driver = driver;
    // try {
    // internal_open();
    // } catch (final HoTTException e) {
    // e.printStackTrace();
    // }
  }

  @Override
  public void close() throws IOException {
    internal_close();
  }

  @Override
  protected void finalize() throws Throwable {
    internal_close();
  }

  /**
   * Will block. Don't call this method from the UI thread!
   * 
   * @return
   * @throws HoTTException
   */
  @Override
  public InputStream getInputStream() throws HoTTException {
    return inputStream;
  }

  /**
   * Will block. Don't call this method from the UI thread!
   * 
   * @return
   * @throws HoTTException
   */
  @Override
  public OutputStream getOutputStream() throws HoTTException {
    return outputStream;
  }

  private void internal_close() throws IOException {
    if (open) {
      inputStream.close();
      inputStream = null;

      outputStream.flush();
      outputStream.close();
      outputStream = null;

      driver.close();
      open = false;
    }
  }

  private void internal_open() throws HoTTException {
    if (!open) {
      try {
        driver.open();
        driver.setParameters(115200, UsbSerialDriver.DATABITS_8, UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
        open = true;

        inputStream = new UsbSerialDriverInputStream();
        outputStream = new UsbSerialDriverOutputStream();
      } catch (final IOException e) {
        throw new HoTTException(e);
      }
    }
  }

  @Override
  public boolean isOpen() {
    return open;
  }

  /**
   * Will block. Don't call this method from the UI thread!
   * 
   * @return
   * @throws HoTTException
   */
  @Override
  public void open() throws HoTTException {
    internal_open();
  }
}