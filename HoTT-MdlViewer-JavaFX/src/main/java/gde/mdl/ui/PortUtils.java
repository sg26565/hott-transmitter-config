package gde.mdl.ui;

import de.treichels.hott.HoTTSerialPort;
import gde.model.serial.SerialPortDefaultImpl;

public class PortUtils {
    public static synchronized <T> T withPort(final String portName, final PortFunction<T> function) {
        T result = null;

        if (portName != null && portName.length() > 0) try (HoTTSerialPort port = new HoTTSerialPort(new SerialPortDefaultImpl(portName))) {
            port.open();
            result = function.apply(port);
        } catch (final Exception e) {
            ExceptionDialog.show(e);
        }

        return result;
    }
}
