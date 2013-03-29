package gde.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.model.winged.WingedModel;

public class ReportTest {
	@Test
	public void testProcessTemplate() throws TemplateException, IOException {
		final BaseModel model = new WingedModel();
		model.setModelName("testModel1");

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
		assertEquals("Name: testModel1", baos.toString());
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
