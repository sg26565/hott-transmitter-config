package gde.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.model.winged.WingedModel;

public class ReportTest {
	@BeforeClass
	public static void init() throws ClassNotFoundException {
		// initialize Report class
		Class.forName("gde.report.Report");
	}

	@Test
	public void testGetModelClassPath() throws IOException, URISyntaxException, TemplateException {
		final BaseModel model = Report.getModel("gde/report/models/aMERLIN.mdl");

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
		assertEquals("Name: MERLIN", baos.toString());
	}

	@Test
	public void testGetModelFile() throws URISyntaxException, IOException, TemplateException {
		final URL url = ClassLoader.getSystemResource("gde/report/models/aMERLIN.mdl");
		final File file = new File(url.toURI());
		final BaseModel model = Report.getModel(file);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
		assertEquals("Name: MERLIN", baos.toString());
	}

	@Test
	public void testGetModelFilePath() throws IOException, URISyntaxException, TemplateException {
		final URL url = ClassLoader.getSystemResource("gde/report/models/aMERLIN.mdl");
		final File file = new File(url.toURI());
		final BaseModel model = Report.getModel(file.getAbsolutePath());

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
		assertEquals("Name: MERLIN", baos.toString());
	}

	@Test
	public void testProcessTemplate() throws TemplateException, IOException {
		final BaseModel model = new WingedModel();
		model.setModelName("testModel1");

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
	}

	@Test
	public void testProcessXML() throws IOException, JAXBException {
		final BaseModel model = new WingedModel();
		model.setModelName("testModel1");

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos);
		assertTrue(baos.toString().contains("<modelName>testModel1</modelName>"));
	}
}
