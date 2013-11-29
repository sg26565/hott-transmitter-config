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
import freemarker.template.TemplateException;
import gde.mdl.ui.swing.Launcher;
import gde.model.BaseModel;
import gde.model.enums.TransmitterType;
import gde.report.HTML.HTMLReport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.treichels.hott.HoTTDecoder;

public class ReportTest {
  @BeforeClass
  public static void init() throws URISyntaxException {
    Launcher.initSystemProperties();
  }

  @Test
  public void testMc20Models() throws URISyntaxException, IOException, JAXBException, TemplateException {
    final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mc20").toURI());

    for (final File file : dir.listFiles()) {
      final BaseModel model = HoTTDecoder.decode(file);

      assertEquals(TransmitterType.mc20, model.getTransmitterType());

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      HTMLReport.process(model, out, "mc-32.xhtml");
      assertTrue(out.size() > 0);
    }
  }

  @Test
  public void testMc32Models() throws URISyntaxException, IOException, JAXBException, TemplateException {
    final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mc32").toURI());

    for (final File file : dir.listFiles()) {
      final BaseModel model = HoTTDecoder.decode(file);

      assertEquals(TransmitterType.mc32, model.getTransmitterType());

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      HTMLReport.process(model, out, "mc-32.xhtml");
      assertTrue(out.size() > 0);
    }
  }

  @Test
  public void testMx12Models() throws URISyntaxException, IOException, JAXBException, TemplateException {
    final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mx12").toURI());

    for (final File file : dir.listFiles()) {
      final BaseModel model = HoTTDecoder.decode(file);

      assertEquals(TransmitterType.mx12, model.getTransmitterType());

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      HTMLReport.process(model, out, "mx-16.xhtml");
      assertTrue(out.size() > 0);
    }
  }

  @Test
  public void testMx16Models() throws URISyntaxException, IOException, JAXBException, TemplateException {
    final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mx16").toURI());

    for (final File file : dir.listFiles()) {
      final BaseModel model = HoTTDecoder.decode(file);

      assertEquals(TransmitterType.mx16, model.getTransmitterType());

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      HTMLReport.process(model, out, "mx-16.xhtml");
      assertTrue(out.size() > 0);
    }
  }

  @Test
  public void testMx20Models() throws URISyntaxException, IOException, JAXBException, TemplateException {
    final File dir = new File(ClassLoader.getSystemResource("gde/report/models/mx20").toURI());

    for (final File file : dir.listFiles()) {
      final BaseModel model = HoTTDecoder.decode(file);

      assertEquals(TransmitterType.mx20, model.getTransmitterType());

      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      HTMLReport.process(model, out, "mc-32.xhtml");
      assertTrue(out.size() > 0);
    }
  }
}