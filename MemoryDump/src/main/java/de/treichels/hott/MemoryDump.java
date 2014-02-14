/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott;

import gde.model.serial.SerialPortDefaultImpl;

import java.io.IOException;
import java.util.List;

import de.treichels.hott.internal.BaseResponse;
import de.treichels.hott.internal.HoTTSerialPort;
import de.treichels.hott.internal.ReadTransmitterMemory;
import de.treichels.hott.internal.Util;

/**
 * @author oli@treichels.de
 */
public class MemoryDump {
  public static void main(final String[] args) throws IOException {
    final List<String> ports = SerialPortDefaultImpl.getAvailablePorts();
    System.out.printf("Bitte seriellen port eingeben  %s: ", ports);

    final String port = System.console().readLine();

    HoTTTransmitter.setSerialPortImpl(new SerialPortDefaultImpl(port));

    for (int i = 0; i < 512; i++) {
      final int address = 0x800 * i;
      final BaseResponse response = HoTTSerialPort.getInstance().doCommand(new ReadTransmitterMemory(address));
      System.out.print(Util.dumpData(response.getData(), address));
    }
  }
}
