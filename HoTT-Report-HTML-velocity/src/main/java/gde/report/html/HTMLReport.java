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

package gde.report.html;

import gde.model.BaseModel;
import gde.report.CurveImageGenerator;
import gde.report.ReportException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ServiceLoader;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;

/**
 * @author oli@treichels.de
 */
public class HTMLReport {
  private static final String  MX_16_TEMPLATE_NAME = "mx-16.xhtml";
  private static final String  MC_32_TEMPLATE_NAME = "mc-32.xhtml";
  private static final Context CTX;

  static {
    Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
    Velocity.setProperty("class.resource.loader.class", TemplateLoader.class.getName());
    try {
      Velocity.init();
    } catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    final ToolManager manager = new ToolManager();
    manager.autoConfigure(true);
    CTX = manager.createContext();
    CTX.put("hex", new HexConverter());
    CTX.put("programDir", System.getProperty("program.dir"));
    CTX.put("version", System.getProperty("program.version", "unknown"));

    // setup CurveImageGenerator
    final ServiceLoader<CurveImageGenerator> loader = ServiceLoader.load(CurveImageGenerator.class);
    final CurveImageGenerator generator = loader.iterator().next();
    CTX.put("png", generator);
  }

  public static String generateHTML(final BaseModel model) throws IOException, ReportException {
    final String templateName;

    switch (model.getTransmitterType()) {
    case mc16:
    case mc20:
    case mc32:
    case mx20:
      templateName = MC_32_TEMPLATE_NAME;
      break;

    case mx12:
    case mx16:
      templateName = MX_16_TEMPLATE_NAME;
      break;

    default:
      throw new IOException("Unsupported transmitter type");
    }

    CTX.put("model", model);

    final StringWriter sw = new StringWriter();
    final Writer w = new HtmlSafeWriter(sw);

    Template template;
    try {
      template = Velocity.getTemplate(templateName, "UTF-8");
      template.merge(CTX, w);
    } catch (final ResourceNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final ParseErrorException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      w.close();
      sw.close();
    }

    return sw.toString();
  }

  public static void save(final File file, final String html) throws IOException {
    final FileWriter fw = new FileWriter(file);
    fw.write(html);
    fw.close();
  }
}
