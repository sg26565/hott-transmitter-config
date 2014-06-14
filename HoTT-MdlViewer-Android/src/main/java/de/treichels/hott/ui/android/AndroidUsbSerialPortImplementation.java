package de.treichels.hott.ui.android;

import gde.model.HoTTException;
import gde.model.serial.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

public class AndroidUsbSerialPortImplementation implements SerialPort {
  static final int      IO_TIMEOUT   = 1000;
  static final int      BUFFER_SIZE  = 2064;

  final UsbSerialDriver driver;
  private boolean       open         = false;
  private InputStream   inputStream  = null;
  private OutputStream  outputStream = null;

  /**
   * @param device
   * @param interface1
   */
  public AndroidUsbSerialPortImplementation(final UsbSerialDriver driver) {
    this.driver = driver;
  }

  @Override
  public void close() throws IOException {
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
        driver.open();
        driver.setParameters(115200, UsbSerialDriver.DATABITS_8, UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
        open = true;

        inputStream = new UsbSerialDriverInputStream(driver);
        outputStream = new UsbSerialDriverOutputStream(driver);
      } catch (final IOException e) {
        throw new HoTTException(e);
      }
    }
  }
}