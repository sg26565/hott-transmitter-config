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

import gde.messages.Messages;
import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;
import gde.model.serial.SerialPortDefaultImpl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.HoTTSerialPort;

/**
 * @author oli@treichels.de
 */
public class SelectFromMemoryDialog extends JDialog {
  private class ArrayListModel extends AbstractListModel<String> {
    private static final long serialVersionUID = 1L;
    private List<ModelInfo>   data;

    @Override
    public String getElementAt(final int index) {
      final ModelInfo info = data.get(index);

      return String.format("%02d: %c%s.mdl", info.getModelNumber(), info.getModelType() == ModelType.Helicopter ? 'h' : 'a', info.getModelName()); //$NON-NLS-1$
    }

    public int getModelNumerAt(final int index) {
      final ModelInfo info = data.get(index);

      return info.getModelNumber();
    }

    @Override
    public int getSize() {
      return data.size();
    }

    public void setData(final ModelInfo[] infos) {
      data = new ArrayList<ModelInfo>();
      for (final ModelInfo info : infos) {
        if (info.getModelType() != ModelType.Unknown) {
          data.add(info);
        }
      }
    }
  }

  private final class PortSelectionListener implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent arg0) {
      final String portName = (String) comboBox.getSelectedItem();
      if (portName != null && portName.length() > 0) {
        port = new HoTTSerialPort(new SerialPortDefaultImpl(portName));
        try {
          model.setData(port.getAllModelInfos());
        } catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private static final long serialVersionUID = 1L;

  public static SelectFromMemoryDialog showDialog(final Frame frame) {
    final SelectFromMemoryDialog dialog = new SelectFromMemoryDialog(frame);
    dialog.setVisible(true);
    return dialog;
  }

  private final JComboBox<String> comboBox      = new JComboBox<String>();
  private final ArrayListModel    model         = new ArrayListModel();
  private final JList<String>     list          = new JList<String>(model);
  private final JButton           okButton      = new JButton(Messages.getString("Ok")); //$NON-NLS-1$
  private HoTTSerialPort          port;

  private int                     selectedIndex = -1;

  public SelectFromMemoryDialog(final Frame owner) {
    super(owner, Messages.getString("SelectFromMemoryDialog.Title"), true); //$NON-NLS-1$

    for (final String s : SerialPortDefaultImpl.getAvailablePorts()) {
      comboBox.addItem(s);
    }

    final PortSelectionListener listener = new PortSelectionListener();
    comboBox.addActionListener(listener);
    listener.actionPerformed(null);

    final JLabel label = new JLabel(Messages.getString("SerialPort")); //$NON-NLS-1$
    label.setLabelFor(comboBox);
    final JPanel portPanel = new JPanel();
    portPanel.add(label);
    portPanel.add(comboBox);

    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setVisibleRowCount(-1);
    list.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        if (e.getClickCount() == 2) {
          okButton.doClick(); // emulate button click
        }
      }
    });
    final JScrollPane scrollPane = new JScrollPane(list);
    scrollPane.setPreferredSize(new Dimension(100, 300));

    final JButton cancelButton = new JButton(Messages.getString("Cancel")); //$NON-NLS-1$
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {
        selectedIndex = -1;
        setVisible(false);
      }
    });

    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        selectedIndex = list.getSelectedIndex();
        setVisible(false);
      }
    });

    final JPanel buttonPanel = new JPanel();
    buttonPanel.add(cancelButton);
    buttonPanel.add(okButton);

    getRootPane().setDefaultButton(okButton);

    final Container container = getContentPane();
    container.add(portPanel, BorderLayout.NORTH);
    container.add(buttonPanel, BorderLayout.SOUTH);
    container.add(scrollPane, BorderLayout.CENTER);

    pack();

    setLocationRelativeTo(owner);
  }

  public void closeDialog() {
    setVisible(false);
  }

  public BaseModel getModel() throws IOException {
    if (selectedIndex == -1) {
      return null;
    }

    return HoTTDecoder.decodeMemory(port, model.getModelNumerAt(selectedIndex));
  }
}
