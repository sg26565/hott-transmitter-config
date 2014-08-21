package de.treichels.hott.ui.android;

import gde.model.HoTTException;
import gde.model.serial.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hoho.android.usbserial.driver.UsbSerialPort;

public class AndroidUsbSerialPortImplementation implements SerialPort {
  static final int     IO_TIMEOUT   = 1000;
  static final int     BUFFER_SIZE  = 2064;

  final UsbSerialPort  port;
  private boolean      open         = false;
  private InputStream  inputStream  = null;
  private OutputStream outputStream = null;

  /**
   * @param device
   * @param interface1
   */
  public AndroidUsbSerialPortImplementation(final UsbSerialPort port) {
    this.port = port;
  }

  @Override
  public void close() throws IOException {
    if (open) {
      inputStream.close();
      inputStream = null;

      outputStream.flush();
      outputStream.close();
      outputStream = null;

      port.close();
      open = false;
    }
  }

  @Override
  public InputStream getInputStream() throws HoTTException {
    return inputStream;
  }

  @Override
  public OutputStream getOutputStream() throws HoTTException {
    return outputStream;
  }

  @Override
  public boolean isOpen() {
    return open;
  }

  @Override
  public void open() throws HoTTException {
    if (!open) {
      try {
        port.setParameters(115200, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        open = true;

        inputStream = new UsbSerialDriverInputStream(port);
        outputStream = new UsbSerialDriverOutputStream(port);
      } catch (final IOException e) {
        throw new HoTTException(e);
      }
    }
  }
}