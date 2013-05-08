import gde.model.BaseModel;
import gde.report.Report;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFrame;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

import com.itextpdf.text.pdf.BaseFont;

public class BrowserTest {
	private static final File	HTML	= new File("aP-51D Mustang.html");
	private static final File	MDL		= new File("aP-51D Mustang.mdl");
	private static final File	PDF		= new File("aP-51D Mustang.pdf");

	public static void main(final String[] args) throws Exception {
		final XHTMLPanel panel = new XHTMLPanel();
		final FSScrollPane scrollPane = new FSScrollPane(panel);
		final JFrame frame = new JFrame("Browser Test");
		frame.getContentPane().add(scrollPane);

		final BaseModel model = Report.getModel(MDL);
		final String data = Report.generateHTML(model);
		Report.save(HTML, data);

		panel.setDocument(HTML);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(1024, 768);
		frame.setVisible(true);

		final FileOutputStream fos = new FileOutputStream(PDF);
		final ITextRenderer renderer = new ITextRenderer();
		renderer.getFontResolver().addFont("/usr/share/fonts/truetype/msttcorefonts/Arial.ttf", BaseFont.IDENTITY_H, true);
		renderer.setDocument(HTML);
		renderer.layout();
		renderer.createPDF(fos);
		fos.close();
	}
}
