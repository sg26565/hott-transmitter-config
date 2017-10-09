package gde.mdl.ui;

import java.io.IOException;

import de.treichels.hott.HoTTSerialPort;

public interface PortFunction<T> {
    public T apply(HoTTSerialPort port) throws IOException;
}
