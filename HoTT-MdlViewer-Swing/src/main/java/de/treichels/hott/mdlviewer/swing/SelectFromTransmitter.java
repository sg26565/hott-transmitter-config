package de.treichels.hott.mdlviewer.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.treichels.decoder.HoTTSerialPort;
import de.treichels.hott.messages.Messages;
import de.treichels.hott.model.serial.JSSCSerialPort;
import de.treichels.hott.util.ModelLoader;

public abstract class SelectFromTransmitter extends JPanel implements ModelLoader {
    private final class PortSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
            final String portName = (String) comboBox.getSelectedItem();
            if (portName != null && portName.length() > 0 && portNames.contains(portName)) {
                SimpleGUI.PREFS.put("portName", portName);
                try {
                    lock.lock();
                    port = new HoTTSerialPort(new JSSCSerialPort(portName));
                } finally {
                    lock.unlock();
                }
                onReload();
            }
        }
    }

    private static final long serialVersionUID = 1L;
    private static final List<String> portNames = JSSCSerialPort.getAvailablePorts();

    protected static void delay() {
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected HoTTSerialPort port = null;
    private final JComboBox<String> comboBox = new JComboBox<>();
    protected final WaitLayerUI<JScrollPane> layerUI = new WaitLayerUI<>();
    protected Lock lock = new ReentrantLock();

    protected abstract JComponent getSelectionComponent();

    protected void initUI() {
        for (final String s : portNames)
            comboBox.addItem(s);
        final String portName = SimpleGUI.PREFS.get("portName", (String) comboBox.getSelectedItem());
        if (portName != null && portName.length() > 0 && portNames.contains(portName)) try {
            lock.lock();
            port = new HoTTSerialPort(new JSSCSerialPort(portName));
        } finally {
            lock.unlock();
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

        final JLayer<JScrollPane> layer = new JLayer<>(scrollPane, layerUI);

        setLayout(new BorderLayout());
        add(portPanel, BorderLayout.NORTH);
        add(layer, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
