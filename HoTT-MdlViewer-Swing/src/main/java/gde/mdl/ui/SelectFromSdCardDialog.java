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
import gde.model.HoTTException;
import gde.model.enums.ModelType;
import gde.model.serial.FileInfo;
import gde.model.serial.FileType;
import gde.model.serial.SerialPortDefaultImpl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeSelectionModel;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.HoTTSerialPort;

/**
 * @author oli@treichels.de
 */
public class SelectFromSdCardDialog extends JDialog {
  private class FileInfoTreeNode extends DefaultMutableTreeNode {
    private static final long serialVersionUID = 1L;

    public FileInfoTreeNode(final FileInfo info) {
      super(info);
    }

    private FileInfo getFileInfo() {
      return (FileInfo) getUserObject();
    }

    @Override
    public boolean isLeaf() {
      return getFileInfo().getType() == FileType.File;
    }

    @Override
    public String toString() {
      return getFileInfo().getName();
    }
  }

  private final class PortSelectionListener implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent arg0) {
      final String portName = (String) comboBox.getSelectedItem();
      if (portName != null && portName.length() > 0) {
        rootNode.removeAllChildren();
        port = new HoTTSerialPort(new SerialPortDefaultImpl(portName));
        try {
          for (final String name : port.listDir("/")) { //$NON-NLS-1$
            final FileInfo info = port.getFileInfo(name);
            rootNode.add(new FileInfoTreeNode(info));
          }
        } catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private static final long serialVersionUID = 1L;

  public static SelectFromSdCardDialog showDialog(final Frame frame) {
    final SelectFromSdCardDialog dialog = new SelectFromSdCardDialog(frame);
    dialog.setVisible(true);
    return dialog;
  }

  private final JComboBox<String>      comboBox = new JComboBox<String>();
  private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(Messages.getString("SelectFromSdCardDialog.RootNodeLabel")); //$NON-NLS-1$
  private final DefaultTreeModel       model    = new DefaultTreeModel(rootNode);
  private final JTree                  tree     = new JTree(model);
  private final JButton                okButton = new JButton(Messages.getString("Ok"));                                                 //$NON-NLS-1$
  private FileInfo                     fileInfo = null;
  private HoTTSerialPort               port;

  public SelectFromSdCardDialog(final Frame owner) {
    super(owner, Messages.getString("SelectFromSdCardDialog.Title"), true); //$NON-NLS-1$

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

    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setRootVisible(true);
    tree.expandRow(0);
    tree.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        if (e.getClickCount() == 2) {
          okButton.doClick(); // emulate button click
        }
      }
    });
    tree.addTreeWillExpandListener(new TreeWillExpandListener() {
      @Override
      public void treeWillCollapse(final TreeExpansionEvent ev) throws ExpandVetoException {
        // ignored
      }

      @Override
      public void treeWillExpand(final TreeExpansionEvent ev) throws ExpandVetoException {
        final FileInfoTreeNode node = (FileInfoTreeNode) ev.getPath().getLastPathComponent();

        if (node.getChildCount() == 0) {
          try {
            for (final String name : port.listDir(node.getFileInfo().getPath())) {
              final FileInfo info = port.getFileInfo(name);
              node.add(new FileInfoTreeNode(info));
            }
          } catch (final Exception e) {
            throw new RuntimeException(e);
          }
        }
      }
    });

    final JScrollPane scrollPane = new JScrollPane(tree);
    scrollPane.setPreferredSize(new Dimension(100, 300));

    final JButton cancelButton = new JButton(Messages.getString("Cancel")); //$NON-NLS-1$
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {
        fileInfo = null;
        setVisible(false);
      }
    });

    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        final Object node = tree.getSelectionPath().getLastPathComponent();

        if (node != null && node instanceof FileInfoTreeNode) {
          fileInfo = ((FileInfoTreeNode) node).getFileInfo();
        } else {
          fileInfo = null;
        }

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
    BaseModel result = null;

    if (fileInfo != null && fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000 //$NON-NLS-1$
        && fileInfo.getSize() >= 0x2000) {
      final String fileName = fileInfo.getName();

      // check model type
      ModelType type;
      final char typeChar = fileName.charAt(0);
      switch (typeChar) {
      case 'a':
        type = ModelType.Winged;
        break;

      case 'h':
        type = ModelType.Helicopter;
        break;

      default:
        throw new HoTTException("InvalidModelType", typeChar); //$NON-NLS-1$
      }

      final String name = fileName.substring(1, fileName.length() - 4);

      final ByteArrayOutputStream os = new ByteArrayOutputStream();
      port.readFile(fileInfo.getPath(), os);
      final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
      result = HoTTDecoder.decodeStream(type, name, is);
    }

    return result;
  }
}
