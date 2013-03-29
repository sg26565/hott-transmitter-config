import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.html.HTMLEditorKit;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.report.Report;

public class Test {
	public static void main(final String[] args) throws IOException, URISyntaxException, TemplateException {
		final JFrame frame = new JFrame("Hott Transmitter Config");
		final JEditorPane editorPane = new JEditorPane();
		final JScrollPane scrollPane = new JScrollPane(editorPane);
		final HTMLEditorKit kit = new HTMLEditorKit();
		final JButton refreshButton = new JButton("Refresh");
		final JButton saveHTMLButton = new JButton("Save HTML");
		final JButton savePDFButton = new JButton("Save PDF");
		final JPanel panel = new JPanel();
		final JPanel boxPanel = new JPanel();

		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setMinimumSize(new Dimension(100, 100));
		editorPane.setEditorKit(kit);

		final BaseModel model = Report.getModel("aMERLIN.mdl");

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				try {
					final ByteArrayOutputStream out = new ByteArrayOutputStream();
					Report.process(model, out, "mx-16.xhtml");
					editorPane.setText(out.toString());
				} catch (final IOException | TemplateException e) {
					showError(frame, e);
				}
			}
		});

		saveHTMLButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				try {
					final FileWriter w = new FileWriter("mx-16.html");
					w.write(editorPane.getText());
					w.close();
				} catch (final IOException e) {
					showError(frame, e);
				}
			}
		});

		savePDFButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				try {
					final FileOutputStream os = new FileOutputStream("mx-16.pdf");
					final ITextRenderer renderer = new ITextRenderer();
					renderer.setDocumentFromString(editorPane.getText());
					renderer.layout();
					renderer.createPDF(os);
					os.close();
				} catch (final DocumentException | IOException e) {
					showError(frame, e);
				}
			}
		});

		boxPanel.setLayout(new FlowLayout());
		boxPanel.add(refreshButton);
		boxPanel.add(saveHTMLButton);
		boxPanel.add(savePDFButton);

		panel.setLayout(new BorderLayout());
		panel.add(boxPanel, BorderLayout.SOUTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);

		refreshButton.getActionListeners()[0].actionPerformed(null);
	}

	private static void showError(final JFrame frame, final Throwable t) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		JOptionPane.showMessageDialog(frame, baos.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}
