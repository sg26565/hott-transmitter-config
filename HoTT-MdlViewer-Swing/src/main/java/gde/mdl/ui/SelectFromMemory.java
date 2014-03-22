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
package gde.mdl.ui;

import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

import de.treichels.hott.HoTTDecoder;

/**
 * @author oli@treichels.de
 */
public class SelectFromMemory extends SelectFromTransmitter {
  private class ArrayListModel extends AbstractListModel<String> {
    private static final long     serialVersionUID = 1L;
    private final List<ModelInfo> modelInfos       = new ArrayList<ModelInfo>();

    public void addModelInfo(final ModelInfo info) {
      if (info.getModelType() != ModelType.Unknown) {
        final int index = getSize();
        modelInfos.add(info);
        fireIntervalAdded(this, index, index);
      }
    }

    public void clear() {
      modelInfos.clear();
    }

    @Override
    public String getElementAt(final int index) {
      final ModelInfo info = modelInfos.get(index);

      return String.format("%02d: %c%s.mdl", info.getModelNumber(), info.getModelType() == ModelType.Helicopter ? 'h' : 'a', info.getModelName()); //$NON-NLS-1$
    }

    public int getModelNumerAt(final int index) {
      return modelInfos.get(index).getModelNumber();
    }

    @Override
    public int getSize() {
      return modelInfos.size();
    }
  }

  private final class ReloadWorker extends SwingWorker<Void, ModelInfo> {
    @Override
    protected Void doInBackground() throws Exception {
      layerUI.start();
      list.setEnabled(false);
      final ModelInfo infos[] = port.getAllModelInfos();

      for (final ModelInfo info : infos) {
        publish(info);
      }

      return null;
    }

    @Override
    protected void done() {
      layerUI.stop();
      list.setEnabled(true);
    }

    @Override
    protected void process(final List<ModelInfo> chunks) {
      for (final ModelInfo info : chunks) {
        model.addModelInfo(info);
      }
    }
  }

  private static final long    serialVersionUID = 1L;

  private final ArrayListModel model            = new ArrayListModel();
  private final JList<String>  list             = new JList<String>(model);
  private int                  selectedIndex    = -1;

  public SelectFromMemory() {
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setVisibleRowCount(-1);

    initUI();

    onReload();
  }

  @Override
  public BaseModel getModel() throws IOException {
    if (selectedIndex == -1) {
      return null;
    }

    return HoTTDecoder.decodeMemory(port, model.getModelNumerAt(selectedIndex));
  }

  @Override
  protected JComponent getSelectionComponent() {
    return list;
  }

  @Override
  public void onCancel() {
    selectedIndex = -1;
  }

  @Override
  public void onOpen() {
    selectedIndex = list.getSelectedIndex();
  }

  @Override
  public void onReload() {
    selectedIndex = -1;
    model.clear();

    if (port != null) {
      new ReloadWorker().execute();
    }
  }
}
