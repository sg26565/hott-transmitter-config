/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.report.html

import freemarker.core.Environment
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler

import java.io.IOException
import java.io.Writer

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
internal class FreeMarkerExceptionHandler : TemplateExceptionHandler {
    @Throws(TemplateException::class)
    override fun handleTemplateException(e: TemplateException, env: Environment, out: Writer) {
        try {
            out.write("[ERROR: " + e.message + "]")
        } catch (e1: IOException) {
            throw TemplateException("Failed to print error message. Cause: " + e1, env)
        }
    }
}