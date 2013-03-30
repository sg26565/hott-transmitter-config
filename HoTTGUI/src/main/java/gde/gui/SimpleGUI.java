package gde.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.report.Report;

public class SimpleGUI {
	private final class HtmlActionListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent evt) {
			raw = false;
			editorPane.setContentType("text/html");
			try {
				updateView();
			} catch (IOException | TemplateException | JAXBException e) {
				showError(frame, e);
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
			} catch (IOException | TemplateException | JAXBException e) {
				showError(frame, e);
			}
		}
	}

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
					updateView();
				} catch (IOException | URISyntaxException | TemplateException | JAXBException e) {
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
						data = generateXML(model);
					} else {
						data = generateHTML(model);
					}

					if (file.getName().endsWith(".pdf")) {
						savePDF(file, data);
					} else {
						save(file, data);
					}
				} catch (final IOException | JAXBException | TemplateException | DocumentException e) {
					showError(frame, e);
				}
			}
		}
	}

	private final JFrame frame = new JFrame("Hott Transmitter Config");
	private final JMenuBar menubar = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenuItem loadMenuItem = new JMenuItem("Load");
	private final JMenuItem saveMenuItem = new JMenuItem("Save");
	private final JMenuItem closeMenuItem = new JMenuItem("Close");
	private final JMenu viewMenu = new JMenu("View");
	private final JMenuItem xmlMenuItem = new JMenuItem("XML");
	private final JMenuItem htmlMenuItem = new JMenuItem("HTML");
	private final HTMLEditorKit editorKit = new HTMLEditorKit();
	private final JEditorPane editorPane = new JEditorPane();
	private final JScrollPane scrollPane = new JScrollPane(editorPane);
	private final JPanel buttonPanel = new JPanel();
	private final JButton loadButton = new JButton("Load");
	private final JButton saveButton = new JButton("Save");
	private final JButton xmlButton = new JButton("View XML");
	private final JButton htmlButton = new JButton("View HTML");
	private final JButton closeButton = new JButton("Close");

	private File lastDir = null;
	private BaseModel model = null;
	private boolean raw = false;

	public static void main(final String[] args) {
		new SimpleGUI().show();
	}

	private void show() {
		editorPane.setEditable(false);
		editorPane.setEditorKit(editorKit);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setMinimumSize(new Dimension(100, 100));

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

		frame.setJMenuBar(menubar);
		frame.setLayout(new BorderLayout());
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void updateView() throws IOException, TemplateException, JAXBException {
		if (raw) {
			editorPane.setText(generateXML(model));
		} else {
			editorPane.setText(generateHTML(model));
		}
	}

	private void showError(final JFrame frame, final Throwable t) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		JOptionPane.showMessageDialog(frame, baos.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	private String generateXML(final BaseModel model) throws JAXBException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos);
		return baos.toString();
	}

	private String generateHTML(final BaseModel model) throws IOException, TemplateException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "mx-16.xhtml");
		return baos.toString();
	}

	private void savePDF(final File file, final String data) throws IOException, DocumentException {
		final FileOutputStream fos = new FileOutputStream(file);
		final ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(data);
		renderer.layout();
		renderer.createPDF(fos);
		fos.close();
	}

	private static void save(final File file, final String data) throws IOException {
		final FileWriter fw = new FileWriter(file);
		fw.write(data);
		fw.close();
	}
}
