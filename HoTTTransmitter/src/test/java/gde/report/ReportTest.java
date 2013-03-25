package gde.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import freemarker.template.TemplateException;
import gde.model.WingedModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.Before;
import org.junit.Test;

public class ReportTest {
	private Report report;
	private ByteArrayOutputStream baos;
	private WingedModel model;

	@Before
	public void setUp() throws Exception {
		report = new Report("test.ftl");
		baos = new ByteArrayOutputStream();
		model = new WingedModel();
	}

	@Test
	public void testReport() {
		assertNotNull(report);
		assertNotNull(baos);
	}

	@Test
	public void testProcessBaseModelOutputStream() throws TemplateException, IOException {
		model.setModelName("testModel1");
		report.process(model, baos);
		assertEquals("Name: testModel1", baos.toString());
	}

	@Test
	public void testProcessBaseModelWriter() throws TemplateException, IOException {
		model.setModelName("testModel2");
		report.process(model, new OutputStreamWriter(baos));
		assertEquals("Name: testModel2", baos.toString());
	}
}
