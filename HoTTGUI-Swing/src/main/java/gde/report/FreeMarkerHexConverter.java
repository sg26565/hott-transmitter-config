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