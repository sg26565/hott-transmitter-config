/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.treichels.hott.report.html

import de.treichels.hott.messages.Messages
import freemarker.template.TemplateMethodModelEx
import freemarker.template.TemplateModelException

internal class FreeMarkerHexConverter : TemplateMethodModelEx {
    @Throws(TemplateModelException::class)
    override fun exec(args: List<*>?): Any {
        if (args == null || args.size != 1) throw TemplateModelException(Messages.getString("InvalidNumberOfArguments"))

        val number = java.lang.Long.parseLong(args[0].toString())
        return java.lang.Long.toHexString(number).toUpperCase()
    }
}