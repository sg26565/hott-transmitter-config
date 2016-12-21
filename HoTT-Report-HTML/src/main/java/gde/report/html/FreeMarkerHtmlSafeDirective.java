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
	private static class HtmlSafeWriter extends Writer {
		private final Writer out;

		public HtmlSafeWriter(final Writer out) {
			this.out = out;
		}

		@Override
		public void close() throws IOException {
			out.close();
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void write(final char[] cbuf, final int off, final int len) throws IOException {
			final CharArrayWriter w = new CharArrayWriter();

			for (int i = 0; i < len; i++) {
				final char c = cbuf[i + off];

				switch (c) {
				case 'ä':
					w.write("&auml;"); //$NON-NLS-1$
					break;

				case 'ö':
					w.write("&ouml;"); //$NON-NLS-1$
					break;

				case 'ü':
					w.write("&uuml;"); //$NON-NLS-1$
					break;

				case 'Ä':
					w.write("&Auml;"); //$NON-NLS-1$
					break;

				case 'Ö':
					w.write("&Ouml;"); //$NON-NLS-1$
					break;

				case 'Ü':
					w.write("&Uuml;"); //$NON-NLS-1$
					break;

				case 'ß':
					w.write("&szlig;"); //$NON-NLS-1$
					break;

				case '€':
					w.write("&euro;"); //$NON-NLS-1$
					break;

				default:
					w.write(c);
				}
			}

			out.write(w.toCharArray());
		}
	}

	@Override
	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params, final TemplateModel[] loopVars, final TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if (body != null) {
			body.render(new HtmlSafeWriter(env.getOut()));
		}
	}
}