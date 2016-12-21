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

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

import gde.model.enums.SwitchAssignment;
import gde.model.enums.SwitchFunction;
import gde.model.enums.SwitchType;

/**
 * @author oli@treichels.de
 */
public class Switch extends AbstractBase {
	private static final long serialVersionUID = 1L;

	private SwitchAssignment assignment;
	private int direction;
	private SwitchFunction function;
	private int number;
	private Object[] qualifier;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Switch other = (Switch) obj;
		if (assignment != other.assignment) {
			return false;
		}
		if (direction != other.direction) {
			return false;
		}
		if (function != other.function) {
			return false;
		}
		if (number != other.number) {
			return false;
		}
		if (!Arrays.equals(qualifier, other.qualifier)) {
			return false;
		}
		return true;
	}

	public SwitchAssignment getAssignment() {
		return assignment;
	}

	public int getDirection() {
		return direction;
	}

	public SwitchFunction getFunction() {
		return function;
	}

	@XmlID
	@XmlAttribute
	public String getId() {
		final StringBuilder b = new StringBuilder();

		b.append(function.name());

		if (qualifier != null) {
			for (final Object q : qualifier) {
				b.append("_"); //$NON-NLS-1$

				if (q.getClass().isEnum()) {
					try {
						final Method m = q.getClass().getMethod("name"); //$NON-NLS-1$
						b.append(m.invoke(q));
					} catch (final Exception e) {
						throw new RuntimeException(e);
					}
				} else {
					b.append(q.toString());
				}
			}
		}

		return b.toString();
	}

	public int getNumber() {
		return number;
	}

	public Object[] getQualifier() {
		return qualifier;
	}

	public SwitchType getType() {
		return getAssignment() == null ? SwitchType.Unknown : getAssignment().getType();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (assignment == null ? 0 : assignment.hashCode());
		result = prime * result + direction;
		result = prime * result + (function == null ? 0 : function.hashCode());
		result = prime * result + number;
		result = prime * result + Arrays.hashCode(qualifier);
		return result;
	}

	public void setAssignment(final SwitchAssignment assigment) {
		assignment = assigment;
	}

	public void setDirection(final int position) {
		direction = position;
	}

	public void setFunction(final SwitchFunction function) {
		this.function = function;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public void setQualifier(final Object[] qualifier) {
		this.qualifier = qualifier;
	}
}
