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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;

import de.treichels.hott.HoTTDecoder;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import gde.mdl.ui.Launcher;
import gde.model.BaseModel;
import gde.model.CurveMixer;
import gde.model.LinearMixer;
import gde.model.helicopter.HelicopterModel;
import gde.model.helicopter.HelicopterPhase;
import gde.model.winged.WingedModel;
import gde.model.winged.WingedPhase;

/**
 * @author oli@treichels.de
 */
public class Report {
	private static final Configuration						CONFIGURATION;
	private static final JAXBContext							CTX;
	private static final TemplateExceptionHandler	CUSTOM_HANDLER;
	private static final ITextRenderer						ITEXT_RENDERER;
	private static final Marshaller								MARSHALLER;

	static {
		// setup freemarker
		CONFIGURATION = new Configuration();
		CONFIGURATION.setEncoding(Locale.getDefault(), "UTF-8");
		CONFIGURATION.setClassForTemplateLoading(Report.class, "templates");
		CONFIGURATION.setObjectWrapper(new DefaultObjectWrapper());
		CUSTOM_HANDLER = new FreeMarkerExceptionHandler();

		// setup JAXB
		try {
			CTX = JAXBContext.newInstance(WingedModel.class, WingedPhase.class, HelicopterModel.class, HelicopterPhase.class, LinearMixer.class, CurveMixer.class);
			MARSHALLER = CTX.createMarshaller();
			MARSHALLER.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
		catch (final JAXBException e) {
			throw new RuntimeException(e);
		}

		// setup flyingsaucer
		ITEXT_RENDERER = new ITextRenderer();
		final SharedContext ctx = ITEXT_RENDERER.getSharedContext();
		ctx.setReplacedElementFactory(new ITextSVGReplacedElementFactory(ctx.getReplacedElementFactory()));
	}

	public static String generateHTML(final BaseModel model) throws IOException, TemplateException {
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

		Report.process(model, baos, templateName);
		return baos.toString();
	}

	public static String generateXML(final BaseModel model) throws JAXBException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Report.process(model, baos);
		return baos.toString();
	}

	public static void generateXsd(final File file) throws IOException {
		CTX.generateSchema(new SchemaOutputResolver() {
			@Override
			public Result createOutput(final String namespaceUri, final String suggestedFileName) throws IOException {
				final StreamResult result = new StreamResult(file);
				return result;
			}
		});
	}

	public static BaseModel getModel(final File file) throws IOException, URISyntaxException {
		// decode the model file into the data model
		return HoTTDecoder.decode(file);
	}

	public static BaseModel getModel(final String fileName) throws IOException, URISyntaxException {
		// lookup the binary model file from the class path
		final URL url = ClassLoader.getSystemResource(fileName);
		final File file;

		if (url != null) {
			file = new File(url.toURI());
		}
		else {
			file = new File(fileName);
		}

		return getModel(file);
	}

	public static boolean isSuppressExceptions() {
		return CONFIGURATION.getTemplateExceptionHandler() instanceof FreeMarkerExceptionHandler;
	}

	public static void process(final BaseModel model, final OutputStream out) throws JAXBException {
		MARSHALLER.marshal(model, out);
	}

	public static void process(final BaseModel model, final OutputStream out, final String templateName) throws IOException, TemplateException {
		final Template template = CONFIGURATION.getTemplate(templateName);
		final Map<String, Object> rootMap = new HashMap<String, Object>();

		rootMap.put("model", model);
		rootMap.put("hex", new FreeMarkerHexConverter());
		rootMap.put("htmlsafe", new FreeMarkerHtmlSafeDirective());
		rootMap.put("programDir", new File(System.getProperty(Launcher.PROGRAM_DIR)).toURI().toURL().toString());
		rootMap.put("version", System.getProperty(Launcher.PROGRAM_VERSION,"unknown"));
		if (model instanceof WingedModel) {
			rootMap.put("wingedModel", model);
		}
		else if (model instanceof HelicopterModel) {
			rootMap.put("helicopterModel", model);
		}

		template.process(rootMap, new OutputStreamWriter(out, "UTF-8"));
	}

	public static void save(final File file, final String data) throws IOException {
		final FileWriter fw = new FileWriter(file);
		fw.write(data);
		fw.close();
	}

	public static void savePDF(final File file, final String data) throws IOException, DocumentException {
		final FileOutputStream fos = new FileOutputStream(file);
		ITEXT_RENDERER.setDocumentFromString(data);
		ITEXT_RENDERER.layout();
		ITEXT_RENDERER.createPDF(fos);
		fos.close();
	}

	public static void setSuppressExceptions(final boolean suppress) {
		if (suppress) {
			CONFIGURATION.setTemplateExceptionHandler(CUSTOM_HANDLER);
		}
		else {
			CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		}
	}
}
