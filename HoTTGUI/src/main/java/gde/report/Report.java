package gde.report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.treichels.hott.HoTTDecoder;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.model.helicopter.HeliCopterMixer;
import gde.model.helicopter.HelicopterModel;
import gde.model.helicopter.HelicopterTrim;
import gde.model.winged.WingedMixer;
import gde.model.winged.WingedModel;
import gde.model.winged.WingedTrim;

/**
 * @author oli@treichels.de
 */
public class Report {
	private static final Configuration configuration;
	private static final Marshaller marshaller;

	static {
		configuration = new Configuration();
		configuration.setClassForTemplateLoading(Report.class, "templates");
		configuration.setObjectWrapper(new DefaultObjectWrapper());

		try {
			final JAXBContext ctx = JAXBContext.newInstance(WingedModel.class, WingedMixer.class, WingedTrim.class, HelicopterModel.class,
					HeliCopterMixer.class, HelicopterTrim.class);
			marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (final JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public static void process(final BaseModel model, final OutputStream out) throws JAXBException {
		marshaller.marshal(model, out);
	}

	public static void process(final BaseModel model, final OutputStream out, final String templateName) throws IOException, TemplateException {
		final Template template = configuration.getTemplate(templateName);
		template.process(model, new OutputStreamWriter(out));
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
		} else {
			file = new File(fileName);
		}

		return getModel(file);
	}
}
