package de.treichels.hott;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import gde.mdl.messages.Messages;
import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;
import gde.model.serial.SerialPortDefaultImpl;
import gnu.io.RXTXCommDriver;

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

/**
 * @author oli@treichels.de
 */
public class ExportModels {
	private final class ButtonCellRenderer extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
			final JButton button = new JButton(Messages.getString("Save")); //$NON-NLS-1$
			button.addActionListener(evt -> {
				try {
					save(tableModel.getModelInfo(row));
				} catch (final IOException e) {
					e.printStackTrace();
				}
			});
			return button;
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row,
				final int column) {
			return getTableCellEditorComponent(table, value, isSelected, row, column);
		}
	}

	private static final class ModelInfoTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private static final String[] COLUMNS = { Messages.getString("ModelNumber"), Messages.getString("ModelType"), Messages.getString("ModelName"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Messages.getString("ModelInfo"), Messages.getString("ReceiverType"), Messages.getString("Usage"), Messages.getString("Actions") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		private ModelInfo[] modelInfos;

		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public String getColumnName(final int column) {
			return COLUMNS[column];
		}

		/**
		 * @param row
		 * @return
		 */
		public ModelInfo getModelInfo(final int row) {
			return modelInfos[row];
		}

		/**
		 * @return
		 */
		public ModelInfo[] getModelInfos() {
			return modelInfos;
		}

		@Override
		public int getRowCount() {
			return modelInfos == null ? 0 : modelInfos.length;
		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {
			final ModelInfo info = modelInfos[rowIndex];

			switch (columnIndex) {
			case 0:
				return rowIndex + 1;

			case 1:
				return info.getModelType();

			case 2:
				return info.getModelName();

			case 3:
				return info.getModelInfo();

			case 4:
				return info.getReceiverType();

			case 5:
				if (info.getUsedHours() == 0 && info.getUsedMinutes() == 0) {
					return "-:-"; //$NON-NLS-1$
				} else {
					return String.format("%02d:%02d", info.getUsedHours(), info.getUsedMinutes()); //$NON-NLS-1$
				}

			default:
				return null;
			}
		}

		@Override
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			return columnIndex == 6;
		}

		public void setModelInfos(final ModelInfo[] modelInfos) {
			this.modelInfos = modelInfos;
			fireTableDataChanged();
		}
	}

	private final class PortSelectionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent arg0) {
			final String portName = (String) comboBox.getSelectedItem();
			if (portName != null && portName.length() > 0) {
				try {
					updateTableData(portName);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final String LAST_SAVE_DIR = "lastSaveDir"; //$NON-NLS-1$
	private static final Preferences PREFS = Preferences.userNodeForPackage(ExportModels.class);

	@SuppressWarnings("unused")
	private static RXTXCommDriver driver;

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> new ExportModels().showDialog());
	}

	private final JComboBox<String> comboBox = new JComboBox<>();
	private final JButton saveAllButton = new JButton(Messages.getString("SaveAll")); //$NON-NLS-1$
	private final JPanel panel = new JPanel();
	private final JFrame frame = new JFrame(Messages.getString("ExportModels.Title")); //$NON-NLS-1$
	private final JLabel status = new JLabel();
	private final ModelInfoTableModel tableModel = new ModelInfoTableModel();
	private final JTable models = new JTable(tableModel);
	private final JScrollPane scrollPane = new JScrollPane(models);
	private HoTTSerialPort port = null;

	public void save(final ModelInfo info) throws IOException {
		final String extension = "mdl"; //$NON-NLS-1$
		final String description = Messages.getString("MdlFileDescription"); //$NON-NLS-1$
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(false);
		final File dir = new File(PREFS.get(LAST_SAVE_DIR, System.getProperty("user.dir"))); //$NON-NLS-1$
		fc.setCurrentDirectory(dir);
		fc.setFileFilter(new FileNameExtensionFilter(description, extension));
		fc.setSelectedFile(new File(dir, String.format("%c%s.mdl", info.getModelType().getChar(), info.getModelName()))); //$NON-NLS-1$

		final int result = fc.showSaveDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
			final File file = fc.getSelectedFile();
			PREFS.put(LAST_SAVE_DIR, file.getParentFile().getAbsolutePath());

			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file);
				os.write(port.getModelData(info));
			} finally {
				if (os != null) {
					os.close();
				}
			}
		}
	}

	public void saveAll() throws IOException {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		final File dir = new File(PREFS.get(LAST_SAVE_DIR, System.getProperty("user.dir"))); //$NON-NLS-1$
		fc.setCurrentDirectory(dir);

		final int result = fc.showSaveDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
			final File file = fc.getSelectedFile();
			PREFS.put(LAST_SAVE_DIR, file.getAbsolutePath());

			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			frame.setEnabled(false);
			final ProgressMonitor monitor = new ProgressMonitor(frame, Messages.getString("ExportModels.SavingModels"), "", 0, tableModel.getRowCount()); //$NON-NLS-1$ //$NON-NLS-2$
			monitor.setMillisToDecideToPopup(30);
			monitor.setMillisToPopup(0);

			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					int progress = 0;

					for (final ModelInfo info : tableModel.getModelInfos()) {
						if (monitor.isCanceled()) {
							break;
						}

						progress++;
						monitor.setProgress(progress);
						monitor.setNote(info.getModelName());

						if (info.getModelType() == ModelType.Unknown) {
							continue;
						}

						final char type = info.getModelType().getChar();
						String name = info.getModelName();
						if (name == null || name.length() == 0) {
							name = String.format("Model%02d", info.getModelNumber()); //$NON-NLS-1$
						}

						final String fileName = String.format("%c%s.mdl", type, name); //$NON-NLS-1$
						FileOutputStream os = null;
						try {
							os = new FileOutputStream(new File(file, fileName));
							os.write(port.getModelData(info));
						} finally {
							if (os != null) {
								os.close();
							}
						}
					}

					return null;
				}

				@Override
				protected void done() {
					monitor.close();
					frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					frame.setEnabled(true);
				}
			}.execute();
		}
	}

	private void showDialog() {
		for (final String s : SerialPortDefaultImpl.getAvailablePorts()) {
			comboBox.addItem(s);
		}

		final PortSelectionListener listener = new PortSelectionListener();
		comboBox.addActionListener(listener);
		listener.actionPerformed(null);

		panel.add(new JLabel(Messages.getString("SerialPort"))); //$NON-NLS-1$
		panel.add(comboBox);
		panel.add(saveAllButton);

		saveAllButton.addActionListener(e -> {
			try {
				saveAll();
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		});

		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.NORTH);
		frame.add(status, BorderLayout.SOUTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(800, 600);

		final TableColumn actions = models.getColumn(models.getColumnName(6));

		final ButtonCellRenderer renderer = new ButtonCellRenderer();
		actions.setCellRenderer(renderer);
		actions.setCellEditor(renderer);
	}

	private void updateTableData(final String portName) throws IOException {
		port = new HoTTSerialPort(new SerialPortDefaultImpl(portName));
		tableModel.setModelInfos(port.getAllModelInfos());
	}
}
