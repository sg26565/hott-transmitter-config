package gde.gui;

import gde.model.BaseModel;
import gde.report.Report;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBException;

public class SimpleGUI {
	public static void main(final String[] args) {
		final JFrame frame = new JFrame("Hott Transmitter Config");
		final JMenuBar menubar = new JMenuBar();
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem load = new JMenuItem("Load");
		final JMenuItem save = new JMenuItem("Save");
		final JMenuItem close = new JMenuItem("Close");
		final JTextPane textPane = new JTextPane();
		final JScrollPane scrollPane = new JScrollPane(textPane);

		textPane.setEditable(false);
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
				fc.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "HoTT Transmitter Model Files";
					}

					@Override
					public boolean accept(final File file) {
						return file.isDirectory() || file.getName().endsWith(".mdl");
					}
				});

				final int result = fc.showOpenDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {
					final File file = fc.getSelectedFile();
					try {
						final BaseModel model = Report.getModel(file);
						final ByteArrayOutputStream baos = new ByteArrayOutputStream();
						Report.process(model, baos);
						textPane.setText(baos.toString());
					} catch (IOException | URISyntaxException | JAXBException e) {
						showError(frame, e);
					}
				}
			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setMultiSelectionEnabled(false);
				fc.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "XML Files";
					}

					@Override
					public boolean accept(final File file) {
						return file.isDirectory() || file.getName().endsWith(".xml");
					}
				});
				final int result = fc.showSaveDialog(frame);

				if (result == JFileChooser.APPROVE_OPTION) {
					final File file = fc.getSelectedFile();
					FileWriter fw = null;

					try {
						fw = new FileWriter(file);
						fw.write(textPane.getText());
					} catch (final IOException e) {
						showError(frame, e);
					} finally {
						if (fw != null) {
							try {
								fw.close();
							} catch (final IOException e) {
								// ignore
							}
						}
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
