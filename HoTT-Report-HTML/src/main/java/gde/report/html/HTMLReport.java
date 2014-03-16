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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import gde.messages.Messages;
import gde.model.BaseModel;
import gde.model.helicopter.HelicopterModel;
import gde.model.winged.WingedModel;
import gde.report.CurveImageGenerator;
import gde.report.ReportException;

/**
 * @author oli@treichels.de
 */
public class HTMLReport {
  private static final Configuration      CONFIGURATION;
  private static TemplateExceptionHandler CUSTOM_EXCEPTION_HANDLER;
  private static CurveImageGenerator      CURVE_IMAGE_GENERATOR;

  static {
    // setup freemarker
    CONFIGURATION = new Configuration();
    CONFIGURATION.setEncoding(Locale.getDefault(), "UTF-8"); //$NON-NLS-1$
    CONFIGURATION.setClassForTemplateLoading(HTMLReport.class, "templates"); //$NON-NLS-1$
    CONFIGURATION.setObjectWrapper(new DefaultObjectWrapper());
    CUSTOM_EXCEPTION_HANDLER = new FreeMarkerExceptionHandler();

    // setup CurveImageGenerator
    final ServiceLoader<CurveImageGenerator> loader = ServiceLoader.load(CurveImageGenerator.class);
    final Iterator<CurveImageGenerator> iterator = loader.iterator();

    if (iterator.hasNext()) {
      CURVE_IMAGE_GENERATOR = loader.iterator().next();
    } else {
      CURVE_IMAGE_GENERATOR = new DummyCurveImageGenerator();
    }

    // extract font file
    final File file = new File(System.getProperty("java.io.tmpdir"), "Arial.ttf"); //$NON-NLS-1$ //$NON-NLS-2$
    if (!(file.exists() && file.isFile() && file.canRead())) {
      InputStream is = null;
      OutputStream os = null;

      try {
        is = ClassLoader.getSystemResourceAsStream("Arial.ttf"); //$NON-NLS-1$
        os = new FileOutputStream(file);

        final byte[] buffer = new byte[1024];
        while (true) {
          final int len = is.read(buffer);
          if (len == -1) {
            break;
          }
          os.write(buffer, 0, len);
        }
      } catch (final IOException e) {
        throw new RuntimeException(e);
      } finally {
        if (is != null) {
          try {
            is.close();
          } catch (final IOException e) {
            throw new RuntimeException(e);
          }
        }

        if (os != null) {
          try {
            os.close();
          } catch (final IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  public static String generateHTML(final BaseModel model) throws IOException, ReportException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final String templateName;

    switch (model.getTransmitterType()) {
    case mc16:
    case mc20:
    case mc32:
    case mx20:
      templateName = "mc-32.xhtml"; //$NON-NLS-1$
      break;

    case mx12:
    case mx16:
      templateName = "mx-16.xhtml"; //$NON-NLS-1$
      break;

    default:
      throw new IOException(Messages.getString("InvalidTransmitterType", model.getTransmitterType())); //$NON-NLS-1$
    }

    try {
      final Template template = HTMLReport.CONFIGURATION.getTemplate(templateName);
      final Map<String, Object> rootMap = new HashMap<String, Object>();

      rootMap.put("model", model); //$NON-NLS-1$
      rootMap.put("hex", new FreeMarkerHexConverter()); //$NON-NLS-1$
      rootMap.put("png", HTMLReport.CURVE_IMAGE_GENERATOR); //$NON-NLS-1$
      rootMap.put("htmlsafe", new FreeMarkerHtmlSafeDirective()); //$NON-NLS-1$
      rootMap.put("programDir", new File(System.getProperty("program.dir", ".")).toURI().toURL().toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      rootMap.put("tmpdir", System.getProperty("java.io.tmpdir")); //$NON-NLS-1$ //$NON-NLS-2$
      rootMap.put("version", System.getProperty("program.version", "unknown")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      if (model instanceof WingedModel) {
        rootMap.put("wingedModel", model); //$NON-NLS-1$
      } else if (model instanceof HelicopterModel) {
        rootMap.put("helicopterModel", model); //$NON-NLS-1$
      }

      template.process(rootMap, new OutputStreamWriter(baos, "UTF-8")); //$NON-NLS-1$
    } catch (final TemplateException e) {
      throw new ReportException(e);
    }
    return baos.toString();
  }

  public static Configuration getConfiguration() {
    return CONFIGURATION;
  }

  public static CurveImageGenerator getCurveImageGenerator() {
    return CURVE_IMAGE_GENERATOR;
  }

  public static TemplateExceptionHandler getCustomHandler() {
    return CUSTOM_EXCEPTION_HANDLER;
  }

  public static boolean isSuppressExceptions() {
    return CONFIGURATION.getTemplateExceptionHandler() instanceof FreeMarkerExceptionHandler;
  }

  public static void save(final File file, final String html) throws IOException {
    final FileWriter fw = new FileWriter(file);
    fw.write(html);
    fw.close();
  }

  public static void setCurveImageGenerator(final CurveImageGenerator generator) {
    CURVE_IMAGE_GENERATOR = generator;
  }

  public static void setCustomHandler(final TemplateExceptionHandler handler) {
    CUSTOM_EXCEPTION_HANDLER = handler;
  }

  public static void setSuppressExceptions(final boolean suppress) {
    if (suppress) {
      CONFIGURATION.setTemplateExceptionHandler(CUSTOM_EXCEPTION_HANDLER);
    } else {
      CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
    }
  }
}
