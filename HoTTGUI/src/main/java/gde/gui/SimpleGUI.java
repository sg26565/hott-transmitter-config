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
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.prefs.Preferences;

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

import com.itextpdf.text.DocumentException;

public class SimpleGUI extends FSScrollPane {
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

	public enum FileType {
		HTML, PDF, XML
	}

	private final class LoadAction extends AbstractAction {
		private static final long	serialVersionUID	= 1L;

		public LoadAction(final String name) {
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent evt) {
			try {
				load();
			}
			catch (final Throwable t) {
				showError(t);
			}
		}
	}

	private final class RefreshAction extends AbstractAction {
		private static final long	serialVersionUID	= 1L;

		public RefreshAction(final String name) {
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent evt) {
			try {
				refresh();
			}
			catch (final Exception e) {
				showError(e);
			}
		}
	}

	private final class SaveAction extends AbstractAction {
		private static final long	serialVersionUID	= 1L;

		private final FileType		fileType;

		public SaveAction(final String name, final FileType fileType) {
			super(name);
			this.fileType = fileType;
		}

		@Override
		public void actionPerformed(final ActionEvent evt) {
			try {
				save(fileType);
			}
			catch (final Throwable t) {
				showError(t);
			}
		}
	}

	private static final String				LAST_LOAD_DIR			= "lastLoadDir";
	private static final String				LAST_SAVE_DIR			= "lastSaveDir";
	private static final Logger				LOG;
	private static final String				LOG_DIR						= "log.dir";
	private static final String				MDL_DIR						= "mdl.dir";
	private static final Preferences	PREFS							= Preferences.userNodeForPackage(SimpleGUI.class);
	private static final String				PROGRAM_DIR				= "program.dir";
	private static final long					serialVersionUID	= 8824399313635999416L;
	private static final String				TEMPLATE_DIR			= "template.dir";

	static {
		File mainJar;
		try {
			mainJar = new File(Report.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		}
		catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}

		final File programDir = mainJar.getParentFile();
		System.setProperty(PROGRAM_DIR, programDir.getAbsolutePath());

		if (!System.getProperties().containsKey(MDL_DIR)) {
			System.setProperty(MDL_DIR, System.getProperty(PROGRAM_DIR));
		}

		if (!System.getProperties().containsKey(LOG_DIR)) {
			System.setProperty(LOG_DIR, System.getProperty(PROGRAM_DIR));
		}

		if (!System.getProperties().containsKey(TEMPLATE_DIR)) {
			System.setProperty(TEMPLATE_DIR, new File(programDir, "templates").getAbsolutePath());
		}

		LOG = Logger.getLogger(SimpleGUI.class);
		LOG.debug("main jar location: " + mainJar.getAbsolutePath());
		LOG.debug("program.dir: " + System.getProperty(PROGRAM_DIR));
		LOG.debug("log.dir: " + System.getProperty(LOG_DIR));
		LOG.debug("mdl.dir: " + System.getProperty(MDL_DIR));
		LOG.debug("templates.dir: " + System.getProperty(TEMPLATE_DIR));
	}

	public static void main(final String[] args) {
		new SimpleGUI().showInFrame();
	}

	private final Action							closeAction			= new CloseAction("Close");
	private final Action							loadAction			= new LoadAction("Load MDL");
	private BaseModel									model						= null;
	private final Action							refreshAction		= new RefreshAction("Refresh");
	private final Action							saveHtmlAction	= new SaveAction("Save HTML", FileType.HTML);
	private final Action							savePdfAction		= new SaveAction("Save PDF", FileType.PDF);
	private final Action							saveXmlAction		= new SaveAction("Save XML", FileType.XML);
	private final ScalableXHTMLPanel	xhtmlPane				= new ScalableXHTMLPanel();

	public SimpleGUI() {
		xhtmlPane.getSharedContext().getTextRenderer().setSmoothingThreshold(10);
		setViewportView(xhtmlPane);
		Report.setSuppressExceptions(true);

		saveHtmlAction.setEnabled(false);
		savePdfAction.setEnabled(false);
		saveXmlAction.setEnabled(false);
	}

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

	public void load() throws IOException, URISyntaxException, TemplateException {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileNameExtensionFilter("HoTT Transmitter Model Files", "mdl"));
		fc.setCurrentDirectory(new File(PREFS.get(LAST_LOAD_DIR, System.getProperty(MDL_DIR))));

		final int result = fc.showOpenDialog(getTopLevelAncestor());

		if (result == JFileChooser.APPROVE_OPTION) {
			final File file = fc.getSelectedFile();
			PREFS.put(LAST_LOAD_DIR, file.getParentFile().getAbsolutePath());

			model = Report.getModel(file);
			saveHtmlAction.setEnabled(true);
			savePdfAction.setEnabled(true);
			saveXmlAction.setEnabled(true);

			refresh();
		}
	}

	public void refresh() throws IOException, TemplateException {
		if (model != null) {
			xhtmlPane.setDocumentFromString(Report.generateHTML(model), "", new XhtmlNamespaceHandler());
		}
	}

	public void save(final FileType fileType) throws IOException, DocumentException, TemplateException, JAXBException {
		if (model == null) {
			return;
		}

		final String extension = fileType.toString().toLowerCase();
		final String description = fileType.toString() + " Files";
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setCurrentDirectory(new File(PREFS.get(LAST_SAVE_DIR, PREFS.get(LAST_LOAD_DIR, System.getProperty(MDL_DIR)))));
		fc.setFileFilter(new FileNameExtensionFilter(description, extension));
		fc.setSelectedFile(new File(getFileName(model)));

		final int result = fc.showSaveDialog(getTopLevelAncestor());

		if (result == JFileChooser.APPROVE_OPTION) {
			final File file = fc.getSelectedFile();
			PREFS.put(LAST_SAVE_DIR, file.getParentFile().getAbsolutePath());

			switch (fileType) {
			case XML:
				Report.save(file, Report.generateXML(model));
				break;

			case HTML:
				Report.save(file, Report.generateHTML(model));
				break;

			case PDF:
				Report.savePDF(file, Report.generateHTML(model));
				break;
			}
		}
	}

	private void showError(final Throwable t) {
		LOG.error("Error", t);
		JOptionPane.showMessageDialog(getTopLevelAncestor(), t.getClass().getName() + ": " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void showInFrame() {
		try {
			final JMenu fileMenu = new JMenu("File");
			fileMenu.add(new JMenuItem(loadAction));
			fileMenu.add(new JMenuItem(saveHtmlAction));
			fileMenu.add(new JMenuItem(savePdfAction));
			fileMenu.add(new JMenuItem(saveXmlAction));
			fileMenu.addSeparator();
			fileMenu.add(new JMenuItem(closeAction));

			final JMenuBar menubar = new JMenuBar();
			menubar.add(fileMenu);

			final JPanel buttonPanel = new JPanel();
			buttonPanel.add(new JButton(closeAction));
			buttonPanel.add(new JButton(refreshAction));
			buttonPanel.add(new JButton(loadAction));
			buttonPanel.add(new JButton(saveHtmlAction));
			buttonPanel.add(new JButton(savePdfAction));
			buttonPanel.add(new JButton(saveXmlAction));

			final JFrame frame = new JFrame("Hott Transmitter Config");
			frame.setJMenuBar(menubar);
			frame.setLayout(new BorderLayout());
			frame.add(buttonPanel, BorderLayout.SOUTH);
			frame.add(this, BorderLayout.CENTER);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setSize(1280, 1024);
		}
		catch (final Throwable t) {
			showError(t);
		}
	}
}
