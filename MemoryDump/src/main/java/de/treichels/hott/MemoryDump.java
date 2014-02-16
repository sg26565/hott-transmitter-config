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
import gde.util.Util;
import gnu.io.RXTXCommDriver;

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
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * @author oli@treichels.de
 */
public class MemoryDump {
  @SuppressWarnings("unused")
  private static RXTXCommDriver driver;

  public static void main(final String[] args) throws IOException {
    final JComboBox<String> comboBox = new JComboBox<String>();
    final JButton dumpButton = new JButton("dump");
    final JButton saveButton = new JButton("save");
    final JPanel panel = new JPanel();
    final JTextArea textArea = new JTextArea();
    final JScrollPane scrollPane = new JScrollPane(textArea);
    final JFrame frame = new JFrame("HoTT Transmitter Memory Dump");

    for (final String s : SerialPortDefaultImpl.getAvailablePorts()) {
      comboBox.addItem(s);
    }

    dumpButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {
        final String port = (String) comboBox.getSelectedItem();

        if (port != null && port.length() > 0) {
          dumpButton.setEnabled(false);
          saveButton.setEnabled(false);
          HoTTTransmitter.setSerialPortImpl(new SerialPortDefaultImpl(port));

          new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                try {
                  textArea.setText("");

                  for (int i = 0; i < 512; i++) {
                    final int address = 0x800 * i;
                    final byte[] data = HoTTTransmitter.memoryDump(address);
                    final String text = Util.dumpData(data, address);
                    textArea.append(text);
                    textArea.setCaretPosition(textArea.getText().length());
                  }
                } finally {
                  HoTTTransmitter.closeConnection();
                  dumpButton.setEnabled(true);
                  saveButton.setEnabled(true);
                }
              } catch (final IOException e) {
                throw new RuntimeException(e);
              }
            }
          }).start();
        }
      }
    });

    saveButton.setEnabled(false);
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent evt) {
        final String extension = "txt";
        final String description = "Text Files";
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter(description, extension));

        final int result = fc.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsoluteFile() + ".txt");
          }

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
    });

    panel.add(new JLabel("Serial Port:"));
    panel.add(comboBox);
    panel.add(dumpButton);
    panel.add(saveButton);

    textArea.setEditable(false);
    textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

    frame.setLayout(new BorderLayout());
    frame.add(panel, BorderLayout.NORTH);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.setSize(800, 600);
  }
}