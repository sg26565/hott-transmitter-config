import java.io.IOException;

import de.treichels.hott.HoTTSerialPort;
import gde.model.serial.JSSCSerialPort;
import gde.model.serial.SerialPort;

public class RfOutOn {
    public static void main(final String[] args) throws IOException {
        final SerialPort impl = new JSSCSerialPort("COM5");

        try (HoTTSerialPort port = new HoTTSerialPort(impl)) {
            port.open();
            port.turnRfOutOn();
        }
    }
}
