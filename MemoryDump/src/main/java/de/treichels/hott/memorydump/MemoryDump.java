/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.memorydump;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.treichels.decoder.HoTTSerialPort;
import de.treichels.decoder.HoTTSerialPortException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import de.treichels.hott.messages.Messages;
import de.treichels.hott.model.serial.JSSCSerialPort;
import de.treichels.hott.model.serial.ResponseCode;
import de.treichels.hott.model.serial.SerialPort;
import de.treichels.hott.util.Util;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class MemoryDump {
    private final class DumpThread extends Thread {
        @Override
        public void run() {
            final String portName = (String) comboBox.getSelectedItem();
            if (portName == null || portName.length() == 0) return;

            final SerialPort portImpl = new JSSCSerialPort(portName);

            dumpButton.setText(Messages.getString("Abort")); //$NON-NLS-1$
            saveButton.setEnabled(false);

            textArea.setText(""); //$NON-NLS-1$
            messageArea.setText(Messages.getString("MemoryDump.StartMessage")); //$NON-NLS-1$

            try (HoTTSerialPort port = new HoTTSerialPort(portImpl)) {
                byte[] data = null;
                port.open();

                for (int i = 0; dumpThread != null && i < 4096; i++) {
                    ResponseCode rc = ResponseCode.NACK;

                    final int address = 0x800 * i;
                    if (i % 128 == 0) messageArea.append("\n"); //$NON-NLS-1$

                    while (rc != ResponseCode.ACK && dumpThread != null) {
                        try {
                            data = port.readMemoryBlock(address, 0x800);
                            rc = ResponseCode.ACK;
                        } catch (final Exception e) {
                            if (e instanceof HoTTSerialPortException)
                                rc = ((HoTTSerialPortException) e).getResponseCode();
                            else
                                rc = ResponseCode.ERROR;
                        }

                        switch (rc) {
                        case ACK:
                            final String text = Util.dumpData(data, address);
                            textArea.append(text);
                            textArea.setCaretPosition(textArea.getText().length());
                            messageArea.append("."); //$NON-NLS-1$
                            continue;

                        case BUSY:
                            messageArea.append("B"); //$NON-NLS-1$
                            break;

                        case CRC_ERROR:
                            messageArea.append("C"); //$NON-NLS-1$
                            break;

                        case ERROR:
                            messageArea.append("E"); //$NON-NLS-1$
                            break;

                        case NACK:
                            messageArea.append("N"); //$NON-NLS-1$
                            break;
                        }
                    }
                }
            } catch (final IOException e) {
                messageArea.append(ExceptionUtils.getStackTrace(e));
            } finally {
                dumpButton.setText(Messages.getString("Start")); //$NON-NLS-1$
                saveButton.setEnabled(true);

                messageArea.append(Messages.getString("MemoryDump.done")); //$NON-NLS-1$
            }
        }
    }

    private final class SaveButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent evt) {
            final String extension = "txt"; //$NON-NLS-1$
            final String description = Messages.getString("MemoryDump.FileDescription"); //$NON-NLS-1$
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setMultiSelectionEnabled(false);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(new FileNameExtensionFilter(description, extension));

            final int result = fc.showSaveDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (!file.getName().endsWith(".txt")) file = new File(file.getAbsoluteFile() + ".txt"); //$NON-NLS-1$

                FileWriter writer;
                try {
                    writer = new FileWriter(file);
                    writer.write(textArea.getText());
                    writer.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private final class StartButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
            if (dumpThread == null) {
                dumpThread = new DumpThread();
                dumpThread.start();
            } else
                dumpThread = null;
        }
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryDump().showDialog());
    }

    private final JComboBox<String> comboBox = new JComboBox<>();
    private final JButton dumpButton = new JButton(Messages.getString("Start")); //$NON-NLS-1$
    private final JButton saveButton = new JButton(Messages.getString("Save")); //$NON-NLS-1$
    private final JPanel panel = new JPanel();
    private final JTextArea textArea = new JTextArea();
    private final JScrollPane textScrollPane = new JScrollPane(textArea);
    private final JTextArea messageArea = new JTextArea();
    private final JScrollPane messageScrollPane = new JScrollPane(messageArea);
    private final JFrame frame = new JFrame(Messages.getString("MemoryDump.Title")); //$NON-NLS-1$
    private DumpThread dumpThread = null;

    private void showDialog() {
        for (final String s : JSSCSerialPort.getAvailablePorts())
            comboBox.addItem(s);

        dumpButton.addActionListener(new StartButtonActionListener());
        saveButton.addActionListener(new SaveButtonActionListener());
        saveButton.setEnabled(false);

        panel.add(new JLabel(Messages.getString("SerialPort"))); //$NON-NLS-1$
        panel.add(comboBox);
        panel.add(dumpButton);
        panel.add(saveButton);

        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        messageArea.setEditable(false);
        messageArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        messageArea.setRows(6);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(messageScrollPane, BorderLayout.SOUTH);
        frame.add(textScrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(800, 600);
    }
}
