package gde.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import gde.model.BaseModel;

/**
 * @author oli@treichels.de
 */
public class Report {
	private final Configuration configuration;
	private final Template template;

	public Report(final String templateName) throws IOException {
		configuration = new Configuration();
		configuration.setClassForTemplateLoading(Report.class, "templates");
		configuration.setObjectWrapper(new DefaultObjectWrapper());

		template = configuration.getTemplate(templateName);
	}

	public void process(final BaseModel model, final OutputStream out) throws TemplateException, IOException {
		process(model, new OutputStreamWriter(out));
	}

	public void process(final BaseModel model, final Writer writer) throws TemplateException, IOException {
		template.process(model, writer);
	}
}
