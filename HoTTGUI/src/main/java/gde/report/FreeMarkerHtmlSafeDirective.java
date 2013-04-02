package gde.report;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class FreeMarkerHtmlSafeDirective implements TemplateDirectiveModel {
	@Override
	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params, final TemplateModel[] loopVars, final TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if (body != null) {
			body.render(new HtmlSafeWriter(env.getOut()));
		}
	}

	private static class HtmlSafeWriter extends Writer {
		private final Writer out;

		public HtmlSafeWriter(final Writer out) {
			this.out = out;
		}

		@Override
		public void write(final char[] cbuf, final int off, final int len) throws IOException {
			final CharArrayWriter w = new CharArrayWriter();

			for (int i = 0; i < len; i++) {
				final char c = cbuf[i + off];

				switch (c) {
				case 'ä':
					w.write("&auml;");
					break;

				case 'ö':
					w.write("&ouml;");
					break;

				case 'ü':
					w.write("&uuml;");
					break;

				case 'Ä':
					w.write("&Auml;");
					break;

				case 'Ö':
					w.write("&Ouml;");
					break;

				case 'Ü':
					w.write("&Uuml;");
					break;

				case 'ß':
					w.write("&szlig;");
					break;

				case '€':
					w.write("&euro;");
					break;

				default:
					w.write(c);
				}
			}

			out.write(w.toCharArray());
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void close() throws IOException {
			out.close();
		}
	}
}
