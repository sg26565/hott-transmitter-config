/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.io.IOUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import gde.model.BaseModel;
import gde.model.HoTTException;
import gde.model.helicopter.HelicopterModel;
import gde.model.winged.WingedModel;
import gde.report.CurveImageGenerator;
import gde.report.ReportException;

/**
 * @author oli@treichels.de
 */
public class HTMLReport {
    private static final Configuration CONFIGURATION;
    private static TemplateExceptionHandler CUSTOM_EXCEPTION_HANDLER;
    private static CurveImageGenerator CURVE_IMAGE_GENERATOR;

    static {
        // setup freemarker
        CONFIGURATION = new Configuration(Configuration.VERSION_2_3_23);
        CONFIGURATION.setEncoding(Locale.getDefault(), "UTF-8"); //$NON-NLS-1$
        CONFIGURATION.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
        CUSTOM_EXCEPTION_HANDLER = new FreeMarkerExceptionHandler();

        // setup CurveImageGenerator
        final ServiceLoader<CurveImageGenerator> loader = ServiceLoader.load(CurveImageGenerator.class);
        final Iterator<CurveImageGenerator> iterator = loader.iterator();

        if (iterator.hasNext())
            CURVE_IMAGE_GENERATOR = loader.iterator().next();
        else
            CURVE_IMAGE_GENERATOR = new DummyCurveImageGenerator();

        // extract font file
        final File fontFile = new File(System.getProperty("java.io.tmpdir"), "Arial.ttf"); //$NON-NLS-1$ //$NON-NLS-2$

        if (!(fontFile.exists() && fontFile.isFile() && fontFile.canRead() && fontFile.length() > 0))
            try (InputStream is = ClassLoader.getSystemResourceAsStream("Arial.ttf"); OutputStream os = new FileOutputStream(fontFile)) {
            IOUtils.copy(is, os);
            } catch (final IOException e) {
            throw new RuntimeException(e);
            }
    }

    public static String generateHTML(final BaseModel model) throws IOException, ReportException {
        CONFIGURATION.setClassForTemplateLoading(HTMLReport.class, "templates/hott"); //$NON-NLS-1$

        final Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("model", model); //$NON-NLS-1$
        rootMap.put("png", HTMLReport.CURVE_IMAGE_GENERATOR); //$NON-NLS-1$
        // TODO: add other model types
        if (model instanceof WingedModel)
            rootMap.put("wingedModel", model); //$NON-NLS-1$
        else if (model instanceof HelicopterModel) rootMap.put("helicopterModel", model); //$NON-NLS-1$

        switch (model.getTransmitterType()) {
        case mc16:
        case mc20:
        case mc26:
        case mc28:
        case mc32:
        case mx20:
            return genetateHTML("mc-32.xhtml", rootMap); //$NON-NLS-1$

        case mx12:
        case mx16:
            return genetateHTML("mx-16.xhtml", rootMap); //$NON-NLS-1$

        default:
            throw new HoTTException("InvalidTransmitterType", model.getTransmitterType()); //$NON-NLS-1$
        }
    }

    private static String genetateHTML(final String templateName, final Map<String, Object> rootMap)
            throws IOException, MalformedURLException, UnsupportedEncodingException {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final Template template = HTMLReport.CONFIGURATION.getTemplate(templateName);

            rootMap.put("hex", new FreeMarkerHexConverter()); //$NON-NLS-1$
            rootMap.put("htmlsafe", new FreeMarkerHtmlSafeDirective()); //$NON-NLS-1$
            rootMap.put("programDir", new File(System.getProperty("program.dir", ".")).toURI().toURL().toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            rootMap.put("fontFile", new File(System.getProperty("java.io.tmpdir"), "Arial.ttf").toURI().toURL().toString()); //$NON-NLS-1$ //$NON-NLS-2$
            rootMap.put("version", System.getProperty("program.version", "unknown")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            template.process(rootMap, new OutputStreamWriter(baos, "UTF-8")); //$NON-NLS-1$
            return baos.toString();
        } catch (final TemplateException e) {
            throw new ReportException(e);
        }
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
        if (suppress)
            CONFIGURATION.setTemplateExceptionHandler(CUSTOM_EXCEPTION_HANDLER);
        else
            CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
    }
}
