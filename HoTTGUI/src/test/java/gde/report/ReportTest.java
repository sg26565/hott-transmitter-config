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
import gde.model.enums.TransmitterType;
import gde.model.winged.WingedModel;

public class ReportTest {
	@BeforeClass
	public static void init() throws ClassNotFoundException {
		// initialize Report class
		Class.forName("gde.report.Report");
	}

	@Test
	public void testGetModelClassPath() throws IOException, URISyntaxException, TemplateException {
		final BaseModel model = Report.getModel("gde/report/models/mx16/aMERLIN.mdl");

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
		assertEquals("Name: MERLIN", baos.toString());
	}

	@Test
	public void testGetModelFile() throws URISyntaxException, IOException, TemplateException {
		final URL url = ClassLoader.getSystemResource("gde/report/models/mx16/aMERLIN.mdl");
		final File file = new File(url.toURI());
		final BaseModel model = Report.getModel(file);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
		assertEquals("Name: MERLIN", baos.toString());
	}

	@Test
	public void testGetModelFilePath() throws IOException, URISyntaxException, TemplateException {
		final URL url = ClassLoader.getSystemResource("gde/report/models/mx16/aMERLIN.mdl");
		final File file = new File(url.toURI());
		final BaseModel model = Report.getModel(file.getAbsolutePath());

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos, "test.ftl");
		assertEquals("Name: MERLIN", baos.toString());
	}

	@Test
	public void testMx12Models() throws URISyntaxException, IOException, JAXBException {
		final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mx12").toURI());

		for (final File file : dir.listFiles()) {
			final BaseModel model = Report.getModel(file);

			assertEquals(TransmitterType.mx12, model.getTransmitterType());

			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			Report.process(model, out);
			assertTrue(out.size() > 0);
		}
	}

	@Test
	public void testMx16Models() throws URISyntaxException, IOException, JAXBException {
		final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mx16").toURI());

		for (final File file : dir.listFiles()) {
			final BaseModel model = Report.getModel(file);

			assertEquals(TransmitterType.mx16, model.getTransmitterType());

			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			Report.process(model, out);
			assertTrue(out.size() > 0);
		}
	}

	@Test
	public void testMx32Models() throws URISyntaxException, IOException, JAXBException {
		final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mc32").toURI());

		for (final File file : dir.listFiles()) {
			final BaseModel model = Report.getModel(file);

			assertEquals(TransmitterType.mc32, model.getTransmitterType());

			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			Report.process(model, out);
			assertTrue(out.size() > 0);
		}
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
