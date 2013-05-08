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
	private final class CloseActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			System.exit(0);
		}
	}

	private final class LoadActionListener implements ActionListener {
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
					saveHtmlButton.setEnabled(true);
					saveHtmlMenuItem.setEnabled(true);
					savePdfButton.setEnabled(true);
					savePdfMenuItem.setEnabled(true);
					saveXmlButton.setEnabled(true);
					saveXmlMenuItem.setEnabled(true);
					updateView();
				}
				catch (final Throwable t) {
					showError(t);
				}
			}
		}
	}

	private final class SaveActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			if (model == null) {
				return;
			}

			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setAcceptAllFileFilterUsed(false);
			if (evt.getSource() == saveXmlButton || evt.getSource() == saveXmlMenuItem) {
				fc.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
			}
			else if (evt.getSource() == saveHtmlButton || evt.getSource() == saveHtmlMenuItem) {
				fc.setFileFilter(new FileNameExtensionFilter("HTML Files", "html", "htm", "xhtml"));
			}
			else {
				fc.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
			}
			if (lastSaveDir != null) {
				fc.setCurrentDirectory(lastSaveDir);
			}
			else if (lastLoadDir != null) {
				fc.setCurrentDirectory(lastLoadDir);
			}
			else {
				fc.setCurrentDirectory(new File(System.getProperty("program.dir")));
			}

			final int result = fc.showSaveDialog(frame);

			if (result == JFileChooser.APPROVE_OPTION) {
				final File file = fc.getSelectedFile();
				lastSaveDir = file.getParentFile();

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
	private final JButton							closeButton				= new JButton("Close");
	private final JMenuItem						closeMenuItem			= new JMenuItem("Close");
	private final JMenu								fileMenu					= new JMenu("File");
	private final JFrame							frame							= new JFrame("Hott Transmitter Config");
	private File											lastLoadDir				= null;
	private File											lastSaveDir				= null;
	private final JButton							loadMdlButton			= new JButton("Load MDL");
	private final JMenuItem						loadMdlMenuItem		= new JMenuItem("Load MDL");
	private final JMenuBar						menubar						= new JMenuBar();
	private BaseModel									model							= null;
	private final JButton							saveHtmlButton		= new JButton("Save HTML");
	private final JMenuItem						saveHtmlMenuItem	= new JMenuItem("Save HTML");
	private final JButton							savePdfButton			= new JButton("Save PDF");
	private final JMenuItem						savePdfMenuItem		= new JMenuItem("Save PDF");
	private final JButton							saveXmlButton			= new JButton("Save XML");
	private final JMenuItem						saveXmlMenuItem		= new JMenuItem("Save XML");
	private final ScalableXHTMLPanel	xhtmlPane					= new ScalableXHTMLPanel();
	private final FSScrollPane				xhtmlScrollPane		= new FSScrollPane(xhtmlPane);

	private void show() {
		try {
			ActionListener l = new CloseActionListener();
			closeMenuItem.addActionListener(l);
			closeButton.addActionListener(l);

			l = new LoadActionListener();
			loadMdlMenuItem.addActionListener(l);
			loadMdlButton.addActionListener(l);

			l = new SaveActionListener();
			saveHtmlMenuItem.addActionListener(l);
			saveHtmlButton.addActionListener(l);
			savePdfMenuItem.addActionListener(l);
			savePdfButton.addActionListener(l);
			saveXmlMenuItem.addActionListener(l);
			saveXmlButton.addActionListener(l);

			fileMenu.add(loadMdlMenuItem);
			fileMenu.add(saveHtmlMenuItem);
			fileMenu.add(savePdfMenuItem);
			fileMenu.add(saveXmlMenuItem);
			fileMenu.addSeparator();
			fileMenu.add(closeMenuItem);

			menubar.add(fileMenu);

			buttonPanel.add(loadMdlButton);
			buttonPanel.add(saveHtmlButton);
			buttonPanel.add(savePdfButton);
			buttonPanel.add(saveXmlButton);

			saveHtmlButton.setEnabled(false);
			saveHtmlMenuItem.setEnabled(false);
			savePdfButton.setEnabled(false);
			savePdfMenuItem.setEnabled(false);
			saveXmlButton.setEnabled(false);
			saveXmlMenuItem.setEnabled(false);

			frame.setJMenuBar(menubar);
			frame.setLayout(new BorderLayout());
			frame.add(buttonPanel, BorderLayout.SOUTH);
			frame.add(xhtmlScrollPane, BorderLayout.CENTER);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setSize(1280, 1024);
			xhtmlPane.getSharedContext().getTextRenderer().setSmoothingThreshold(10);
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
		xhtmlPane.setDocumentFromString(Report.generateHTML(model), "", new XhtmlNamespaceHandler());
	}
}
