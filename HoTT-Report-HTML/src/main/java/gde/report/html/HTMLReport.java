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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import gde.model.BaseModel;
import gde.model.helicopter.HelicopterModel;
import gde.model.winged.WingedModel;
import gde.report.CurveImageGenerator;
import gde.report.ReportException;

/**
 * @author oli@treichels.de
 */
public class HTMLReport {
  private static final Configuration            CONFIGURATION;
  private static final TemplateExceptionHandler CUSTOM_HANDLER;
  private static final CurveImageGenerator      CURVE_IMAGE_GENERATOR;

  static {
    // setup freemarker
    CONFIGURATION = new Configuration();
    CONFIGURATION.setEncoding(Locale.getDefault(), "UTF-8");
    CONFIGURATION.setClassForTemplateLoading(HTMLReport.class, "templates");
    CONFIGURATION.setObjectWrapper(new DefaultObjectWrapper());
    CUSTOM_HANDLER = new FreeMarkerExceptionHandler();

    // setup CurveImageGenerator
    final ServiceLoader<CurveImageGenerator> loader = ServiceLoader.load(CurveImageGenerator.class);
    CURVE_IMAGE_GENERATOR = loader.iterator().next();
  }

  public static String generateHTML(final BaseModel model) throws IOException, ReportException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final String templateName;

    switch (model.getTransmitterType()) {
    case mc16:
    case mc20:
    case mc32:
    case mx20:
      templateName = "mc-32.xhtml";
      break;

    case mx12:
    case mx16:
      templateName = "mx-16.xhtml";
      break;

    default:
      throw new IOException("Unsupported transmitter type");
    }

    try {
      final Template template = HTMLReport.CONFIGURATION.getTemplate(templateName);
      final Map<String, Object> rootMap = new HashMap<String, Object>();

      rootMap.put("model", model);
      rootMap.put("hex", new FreeMarkerHexConverter());
      rootMap.put("png", HTMLReport.CURVE_IMAGE_GENERATOR);
      rootMap.put("htmlsafe", new FreeMarkerHtmlSafeDirective());
      rootMap.put("programDir", new File(System.getProperty("program.dir")).toURI().toURL().toString());
      rootMap.put("version", System.getProperty("program.version", "unknown"));
      if (model instanceof WingedModel) {
        rootMap.put("wingedModel", model);
      } else if (model instanceof HelicopterModel) {
        rootMap.put("helicopterModel", model);
      }

      template.process(rootMap, new OutputStreamWriter(baos, "UTF-8"));
    } catch (final TemplateException e) {
      throw new ReportException(e);
    }
    return baos.toString();
  }

  public static boolean isSuppressExceptions() {
    return CONFIGURATION.getTemplateExceptionHandler() instanceof FreeMarkerExceptionHandler;
  }

  public static void save(final File file, final String html) throws IOException {
    final FileWriter fw = new FileWriter(file);
    fw.write(html);
    fw.close();
  }

  public static void setSuppressExceptions(final boolean suppress) {
    if (suppress) {
      CONFIGURATION.setTemplateExceptionHandler(CUSTOM_HANDLER);
    } else {
      CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
    }
  }
}
