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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.bind.JAXBException;

import com.lowagie.text.DocumentException;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.report.Report;

public class SimpleGUI {
	private final class CloseActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			System.exit(0);
		}
	}

	private final class HtmlActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			raw = false;
			editorPane.setContentType("text/html");
			try {
				updateView();
			}
			catch (IOException | TemplateException | JAXBException e) {
				showError(frame, e);
			}
		}
	}

	private final class LoadActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setFileFilter(new FileNameExtensionFilter("HoTT Transmitter Model Files", "mdl"));
			if (lastDir != null) {
				fc.setCurrentDirectory(lastDir);
			}
			final int result = fc.showOpenDialog(frame);

			if (result == JFileChooser.APPROVE_OPTION) {
				final File file = fc.getSelectedFile();
				lastDir = file.getParentFile();
				try {
					model = Report.getModel(file);
					htmlButton.setEnabled(true);
					htmlMenuItem.setEnabled(true);
					xmlButton.setEnabled(true);
					xmlMenuItem.setEnabled(true);
					saveButton.setEnabled(true);
					saveMenuItem.setEnabled(true);
					updateView();
				}
				catch (IOException | URISyntaxException | TemplateException | JAXBException e) {
					showError(frame, e);
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
			fc.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
			fc.addChoosableFileFilter(new FileNameExtensionFilter("HTML Files", "html", "htm", "xhtml"));
			fc.addChoosableFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
			if (lastDir != null) {
				fc.setCurrentDirectory(lastDir);
			}
			final int result = fc.showSaveDialog(frame);

			if (result == JFileChooser.APPROVE_OPTION) {
				final File file = fc.getSelectedFile();
				lastDir = file.getParentFile();

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
				catch (final IOException | JAXBException | TemplateException | DocumentException e) {
					showError(frame, e);
				}
			}
		}
	}

	private final class XmlActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			raw = true;
			editorPane.setContentType("text/xml");
			try {
				updateView();
			}
			catch (IOException | TemplateException | JAXBException e) {
				showError(frame, e);
			}
		}
	}

	public static void main(final String[] args) {
		new SimpleGUI().show();
	}

	private final JPanel				buttonPanel		= new JPanel();
	private final JButton				closeButton		= new JButton("Close");
	private final JMenuItem			closeMenuItem	= new JMenuItem("Close");
	private final HTMLEditorKit	editorKit			= new HTMLEditorKit();
	private final JEditorPane		editorPane		= new JEditorPane();
	private final JMenu					fileMenu			= new JMenu("File");
	private final JFrame				frame					= new JFrame("Hott Transmitter Config");
	private final JButton				htmlButton		= new JButton("View HTML");
	private final JMenuItem			htmlMenuItem	= new JMenuItem("HTML");
	private File								lastDir				= null;
	private final JButton				loadButton		= new JButton("Load");
	private final JMenuItem			loadMenuItem	= new JMenuItem("Load");
	private final JMenuBar			menubar				= new JMenuBar();
	private BaseModel						model					= null;
	private boolean							raw						= false;
	private final JButton				saveButton		= new JButton("Save");
	private final JMenuItem			saveMenuItem	= new JMenuItem("Save");
	private final JScrollPane		scrollPane		= new JScrollPane(editorPane);
	private final JMenu					viewMenu			= new JMenu("View");
	private final JButton				xmlButton			= new JButton("View XML");
	private final JMenuItem			xmlMenuItem		= new JMenuItem("XML");

	private void show() {
		editorPane.setEditable(false);
		editorPane.setEditorKit(editorKit);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setMinimumSize(new Dimension(100, 100));

		lastDir = new File(System.getProperty("user.dir"));

		ActionListener l = new CloseActionListener();
		closeMenuItem.addActionListener(l);
		closeButton.addActionListener(l);

		l = new LoadActionListener();
		loadMenuItem.addActionListener(l);
		loadButton.addActionListener(l);

		l = new SaveActionListener();
		saveMenuItem.addActionListener(l);
		saveButton.addActionListener(l);

		l = new XmlActionListener();
		xmlMenuItem.addActionListener(l);
		xmlButton.addActionListener(l);

		l = new HtmlActionListener();
		htmlMenuItem.addActionListener(l);
		htmlButton.addActionListener(l);

		fileMenu.add(loadMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(closeMenuItem);

		menubar.add(fileMenu);

		viewMenu.add(xmlMenuItem);
		viewMenu.add(htmlMenuItem);

		menubar.add(viewMenu);

		buttonPanel.add(loadButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(htmlButton);
		buttonPanel.add(xmlButton);

		htmlButton.setEnabled(false);
		htmlMenuItem.setEnabled(false);
		xmlButton.setEnabled(false);
		xmlMenuItem.setEnabled(false);
		saveButton.setEnabled(false);
		saveMenuItem.setEnabled(false);

		frame.setJMenuBar(menubar);
		frame.setLayout(new BorderLayout());
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		Report.setSuppressExceptions(true);
	}

	private void showError(final JFrame frame, final Throwable t) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		JOptionPane.showMessageDialog(frame, baos.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void updateView() throws IOException, TemplateException, JAXBException {
		if (raw) {
			editorPane.setText(Report.generateXML(model));
		}
		else {
			editorPane.setText(Report.generateHTML(model));
		}

		editorPane.setCaretPosition(0);
	}
}
