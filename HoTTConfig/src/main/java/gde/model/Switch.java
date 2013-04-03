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
package gde.model;

import gde.model.enums.SwitchAssignment;
import gde.model.enums.SwitchFunction;
import gde.model.enums.SwitchType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class Switch {
	private SwitchAssignment assignment;
	private SwitchFunction function;
	private int number;
	private int position;
	private SwitchType type;
	private Object[] qualifier;

	public SwitchAssignment getAssignment() {
		return assignment;
	}

	public SwitchFunction getFunction() {
		return function;
	}

	public int getNumber() {
		return number;
	}

	@XmlID
	@XmlAttribute
	public String getId() {
		final StringBuilder b = new StringBuilder();

		b.append(function.name());

		if (qualifier != null) {
			for (final Object q : qualifier) {
				b.append("_");

				if (q.getClass().isEnum()) {
					try {
						final Method m = q.getClass().getMethod("name");
						b.append(m.invoke(q));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						throw new RuntimeException(e);
					}
				} else {
					b.append(q.toString());
				}
			}
		}

		return b.toString();
	}

	public int getPosition() {
		return position;
	}

	public SwitchType getType() {
		return type;
	}

	public void setAssignment(final SwitchAssignment assigment) {
		assignment = assigment;
	}

	public void setFunction(final SwitchFunction function) {
		this.function = function;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setType(final SwitchType type) {
		this.type = type;
	}

	public Object[] getQualifier() {
		return qualifier;
	}

	public void setQualifier(final Object[] qualifier) {
		this.qualifier = qualifier;
	}
}
