package gde.report;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class FreeMarkerHexConverter implements TemplateMethodModel {
	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(final List args) throws TemplateModelException {
		if (args == null || args.size() != 1) {
			throw new TemplateModelException("Wrong number of arguments");
		}

		final long number = Long.parseLong((String) args.get(0));
		return Long.toHexString(number).toUpperCase();
	}
}
