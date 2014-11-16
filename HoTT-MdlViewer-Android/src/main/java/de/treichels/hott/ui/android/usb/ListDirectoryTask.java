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
package de.treichels.hott.ui.android.usb;

import gde.model.serial.FileInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;

public class ListDirectoryTask extends UsbTask<String, FileInfo, List<FileInfo>> {
  public ListDirectoryTask(final Context context, final UsbDevice device) {
    super(context, device);
  }

  @Override
  protected List<FileInfo> doInternal(final String... params) throws IOException {
    final String path = params[0];
    final List<FileInfo> infos = new ArrayList<FileInfo>();

    final String[] content = port.listDir(path);

    for (final String name : content) {
      if (isCancelled()) {
        break;
      }

      final FileInfo info = port.getFileInfo(name);
      if (!isCancelled()) {
        infos.add(info);
        publishProgress(info);
      }
    }

    return isCancelled() ? null : infos;
  }
}
