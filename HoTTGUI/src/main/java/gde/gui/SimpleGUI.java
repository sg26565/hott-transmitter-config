package gde.gui;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.report.Report;

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

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.bind.JAXBException;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class SimpleGUI {
	private static BaseModel model = null;

	public static void main(final String[] args) {
		final JFrame frame = new JFrame("Hott Transmitter Config");
		final JMenuBar menubar = new JMenuBar();
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem load = new JMenuItem("Load");
		final JMenuItem save = new JMenuItem("Save");
		final JMenuItem close = new JMenuItem("Close");
		final HTMLEditorKit kit = new HTMLEditorKit();
		final JEditorPane editorPane = new JEditorPane();
		final JScrollPane scrollPane = new JScrollPane(editorPane);

		editorPane.setEditable(false);
		editorPane.setEditorKit(kit);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setMinimumSize(new Dimension(100, 100));

		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				System.exit(0);
			}
		});

		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setMultiSelectionEnabled(false);
				fc.setFileFilter(new FileNameExtensionFilter("HoTT Transmitter Model Files", "mdl"));
				final int result = fc.showOpenDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {
					final File file = fc.getSelectedFile();
					try {
						model = Report.getModel(file);
						final ByteArrayOutputStream baos = new ByteArrayOutputStream();
						Report.process(model, baos, "mx-16.xhtml");
						editorPane.setText(baos.toString());
					} catch (IOException | URISyntaxException | TemplateException e) {
						showError(frame, e);
					}
				}
			}
		});

		save.addActionListener(new ActionListener() {
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
				final int result = fc.showSaveDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {
					final File file = fc.getSelectedFile();

					try {
						final ByteArrayOutputStream baos = new ByteArrayOutputStream();
						if (file.getName().endsWith(".xml")) {
							Report.process(model, baos);
						} else {
							Report.process(model, baos, "mx-16.xhtml");
						}

						if (file.getName().endsWith(".pdf")) {
							final FileOutputStream fos = new FileOutputStream(file);
							final ITextRenderer renderer = new ITextRenderer();
							renderer.setDocumentFromString(baos.toString());
							renderer.layout();
							renderer.createPDF(fos);
							fos.close();
						} else {
							final FileWriter fw = new FileWriter(file);
							fw.write(baos.toString());
							fw.close();
						}
					} catch (final IOException | JAXBException | TemplateException | DocumentException e) {
						showError(frame, e);
					}
				}
			}
		});

		fileMenu.add(load);
		fileMenu.add(save);
		fileMenu.addSeparator();
		fileMenu.add(close);

		menubar.add(fileMenu);

		frame.setJMenuBar(menubar);
		frame.add(scrollPane);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static void showError(final JFrame frame, final Throwable t) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		JOptionPane.showMessageDialog(frame, baos.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}
