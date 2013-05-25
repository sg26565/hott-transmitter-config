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

import java.io.IOException;
import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author oli
 * 
 */
public class FreeMarkerExceptionHandler implements TemplateExceptionHandler {
	@Override
	public void handleTemplateException(final TemplateException e, final Environment env, final Writer out) throws TemplateException {
		try {
			out.write("[ERROR: " + e.getMessage() + "]");
		}
		catch (final IOException e1) {
			throw new TemplateException("Failed to print error message. Cause: " + e1, env);
		}
	}
}
