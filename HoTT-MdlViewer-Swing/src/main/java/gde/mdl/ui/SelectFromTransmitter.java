package gde.mdl.ui;

import gde.messages.Messages;
import gde.model.serial.SerialPortDefaultImpl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.treichels.hott.HoTTSerialPort;

public abstract class SelectFromTransmitter extends JPanel implements ModelLoader {
  private final class PortSelectionListener implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent arg0) {
      final String portName = (String) comboBox.getSelectedItem();
      if (portName != null && portName.length() > 0) {
        port = new HoTTSerialPort(new SerialPortDefaultImpl(portName));
        onReload();
      }
    }
  }

  private static final long       serialVersionUID = 1L;

  private final JComboBox<String> comboBox         = new JComboBox<String>();
  protected static HoTTSerialPort port             = null;

  protected void initUI() {
    for (final String s : SerialPortDefaultImpl.getAvailablePorts()) {
      comboBox.addItem(s);
    }
    final String portName = (String) comboBox.getSelectedItem();
    if (portName != null && portName.length() > 0) {
      port = new HoTTSerialPort(new SerialPortDefaultImpl(portName));
    }

    final PortSelectionListener listener = new PortSelectionListener();
    comboBox.addActionListener(listener);

    final JLabel label = new JLabel(Messages.getString("SerialPort")); //$NON-NLS-1$
    label.setLabelFor(comboBox);
    final JPanel portPanel = new JPanel();
    portPanel.add(label);
    portPanel.add(comboBox);

    final JScrollPane scrollPane = new JScrollPane(getSelectionComponent());
    scrollPane.setPreferredSize(new Dimension(100, 300));
    scrollPane.setBorder(BorderFactory.createEtchedBorder());

    setLayout(new BorderLayout());
    add(portPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  protected abstract JComponent getSelectionComponent();
}