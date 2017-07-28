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
package de.treichels.hott.android.background.serial.tasks;

import gde.model.serial.FileInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.treichels.hott.android.background.serial.DeviceHandler;
import de.treichels.hott.android.background.serial.TxTask;

/**
 * List a directory on the sd card of the transmitter and return a list of {@link FileInfo} objects for each entry.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class ListDirectoryTask extends TxTask<String, FileInfo, List<FileInfo>> {
  public ListDirectoryTask(final DeviceHandler<?> handler) {
    super(handler);
  }

  /**
   * List the content of the directory specified by the path in the first parameter. Other parameters are ignored.
   *
   * @param params
   *          First param contains the file path of the directory. Other params are ignored.
   * @return A list of {@link FileInfo} objects.
   * @throws IOException
   */
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
