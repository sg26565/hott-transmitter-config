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

package gde.gui;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.report.Report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.ScalableXHTMLPanel;

public class SimpleGUI {
	private final class CloseAction extends AbstractAction {
		private static final long	serialVersionUID	= 1L;

		public CloseAction(final String name) {
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent evt) {
			System.exit(0);
		}
	}

	private final class LoadAction extends AbstractAction {
		private static final long	serialVersionUID	= 1L;

		public LoadAction(final String name) {
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent evt) {
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileFilter(new FileNameExtensionFilter("HoTT Transmitter Model Files", "mdl"));
			if (lastLoadDir != null) {
				fc.setCurrentDirectory(lastLoadDir);
			}
			else {
				fc.setCurrentDirectory(new File(System.getProperty("program.dir")));
			}

			final int result = fc.showOpenDialog(frame);

			if (result == JFileChooser.APPROVE_OPTION) {
				final File file = fc.getSelectedFile();
				lastLoadDir = file.getParentFile();
				try {
					model = Report.getModel(file);
					saveHtmlAction.setEnabled(true);
					savePdfAction.setEnabled(true);
					saveXmlAction.setEnabled(true);
					updateView();
				}
				catch (final Throwable t) {
					showError(t);
				}
			}
		}
	}

	private final class SaveAction extends AbstractAction {
		private static final long	serialVersionUID	= 1L;

		private final String			fileType;

		public SaveAction(final String name, final String fileType) {
			super(name);
			this.fileType = fileType;
		}

		@Override
		public void actionPerformed(final ActionEvent evt) {
			if (model == null) {
				return;
			}

			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setAcceptAllFileFilterUsed(false);

			if (lastSaveDir != null) {
				fc.setCurrentDirectory(lastSaveDir);
			}
			else if (lastLoadDir != null) {
				fc.setCurrentDirectory(lastLoadDir);
			}
			else {
				fc.setCurrentDirectory(new File(System.getProperty("program.dir")));
			}

			fc.setFileFilter(new FileNameExtensionFilter(fileType.toUpperCase() + " Files", fileType));
			fc.setSelectedFile(new File(getFileName(model)));

			final int result = fc.showSaveDialog(frame);

			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				lastSaveDir = file.getParentFile();

				if (!file.getName().endsWith("." + fileType)) {
					file = new File(lastSaveDir, file.getName() + "." + fileType);
				}

				try {
					String data;
					if (file.getName().endsWith(".xml")) {
						data = Report.generateXML(model);
					}
					else {
						data = Report.generateHTML(model);
					}

					if (file.getName().endsWith(".pdf")) {
						Report.savePDF(file, data);
					}
					else {
						Report.save(file, data);
					}
				}
				catch (final Throwable t) {
					showError(t);
				}
			}
		}
	}

	private static final Logger	LOG;

	static {
		File mainJar;
		try {
			mainJar = new File(Report.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		}
		catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}

		final File programDir = mainJar.getParentFile();
		System.setProperty("program.dir", programDir.getAbsolutePath());

		LOG = Logger.getLogger(SimpleGUI.class);
		LOG.debug("main jar location: " + mainJar.getAbsolutePath());
		LOG.debug("program dir: " + programDir.getAbsolutePath());
	}

	public static void main(final String[] args) {
		new SimpleGUI().show();
	}

	private final JPanel							buttonPanel				= new JPanel();
	private final Action							closeAction				= new CloseAction("Close");
	private final JButton							closeButton				= new JButton(closeAction);
	private final JMenuItem						closeMenuItem			= new JMenuItem(closeAction);
	private final JMenu								fileMenu					= new JMenu("File");
	private final JFrame							frame							= new JFrame("Hott Transmitter Config");
	private File											lastLoadDir				= null;
	private File											lastSaveDir				= null;
	private final Action							loadAction				= new LoadAction("Load MDL");
	private final JButton							loadMdlButton			= new JButton(loadAction);
	private final JMenuItem						loadMdlMenuItem		= new JMenuItem(loadAction);
	private final JMenuBar						menubar						= new JMenuBar();
	private BaseModel									model							= null;
	private final JButton							refreshButton			= new JButton("Refresh");
	private final Action							saveHtmlAction		= new SaveAction("Save HTML", "html");
	private final JButton							saveHtmlButton		= new JButton(saveHtmlAction);
	private final JMenuItem						saveHtmlMenuItem	= new JMenuItem(saveHtmlAction);
	private final Action							savePdfAction			= new SaveAction("Save PDF", "pdf");
	private final JButton							savePdfButton			= new JButton(savePdfAction);
	private final JMenuItem						savePdfMenuItem		= new JMenuItem(savePdfAction);
	private final Action							saveXmlAction			= new SaveAction("Save XML", "xml");
	private final JButton							saveXmlButton			= new JButton(saveXmlAction);
	private final JMenuItem						saveXmlMenuItem		= new JMenuItem(saveXmlAction);
	private final ScalableXHTMLPanel	xhtmlPane					= new ScalableXHTMLPanel();
	private final FSScrollPane				xhtmlScrollPane		= new FSScrollPane(xhtmlPane);

	private String getFileName(final BaseModel model) {
		// setup filename
		final StringBuilder fileName = new StringBuilder();
		switch (model.getModelType()) {
		case Helicopter:
			fileName.append('h');
			break;

		case Winged:
			fileName.append('a');
			break;

		default:
			break;
		}

		fileName.append(model.getModelName());
		if (model.getModelNumber() > 0) {
			if (model.getModelNumber() < 10) {
				fileName.append('0');
			}
			fileName.append(model.getModelNumber());
		}

		return fileName.toString();
	}

	private void show() {
		try {
			fileMenu.add(loadMdlMenuItem);
			fileMenu.add(saveHtmlMenuItem);
			fileMenu.add(savePdfMenuItem);
			fileMenu.add(saveXmlMenuItem);
			fileMenu.addSeparator();
			fileMenu.add(closeMenuItem);

			menubar.add(fileMenu);

			buttonPanel.add(closeButton);
			buttonPanel.add(refreshButton);
			buttonPanel.add(loadMdlButton);
			buttonPanel.add(saveHtmlButton);
			buttonPanel.add(savePdfButton);
			buttonPanel.add(saveXmlButton);

			refreshButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt) {
					try {
						updateView();
					}
					catch (final Exception e) {
						showError(e);
					}
				}
			});

			saveHtmlAction.setEnabled(false);
			savePdfAction.setEnabled(false);
			saveXmlAction.setEnabled(false);

			xhtmlPane.getSharedContext().getTextRenderer().setSmoothingThreshold(10);

			frame.setJMenuBar(menubar);
			frame.setLayout(new BorderLayout());
			frame.add(buttonPanel, BorderLayout.SOUTH);
			frame.add(xhtmlScrollPane, BorderLayout.CENTER);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setSize(1280, 1024);

			Report.setSuppressExceptions(true);
		}
		catch (final Throwable t) {
			showError(t);
		}
	}

	private void showError(final Throwable t) {
		LOG.error("Error", t);
		JOptionPane.showMessageDialog(frame, t.getClass().getName() + ": " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void updateView() throws IOException, TemplateException, JAXBException {
		if (model != null) {
			xhtmlPane.setDocumentFromString(Report.generateHTML(model), "", new XhtmlNamespaceHandler());
		}
	}
}
