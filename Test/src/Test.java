import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
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
		final JPanel panel = new JPanel();

		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		scrollPane.setMinimumSize(new Dimension(100, 100));
		editorPane.setEditorKit(kit);

		final BaseModel model = Report.getModel("aMERLIN.mdl");

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					Report.process(model, out, "mx-16.xhtml");
				} catch (IOException | TemplateException e) {
					showError(frame, e);
				}
				editorPane.setText(out.toString());
			}
		});

		panel.setLayout(new BorderLayout());
		panel.add(refreshButton, BorderLayout.SOUTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	private static void showError(final JFrame frame, final Throwable t) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		JOptionPane.showMessageDialog(frame, baos.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}
