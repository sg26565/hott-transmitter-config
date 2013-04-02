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

import gde.model.enums.SwitchFunction;
import gde.model.enums.SwitchName;
import gde.model.enums.SwitchType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class Switch {
	private SwitchName assignment;
	private SwitchFunction function;
	private int position;
	private SwitchType type;

	public SwitchName getAssignment() {
		return assignment;
	}

	public SwitchFunction getFunction() {
		return function;
	}

	@XmlAttribute
	@XmlID
	public String getId() {
		return function.name();
	}

	public int getPosition() {
		return position;
	}

	public SwitchType getType() {
		return type;
	}

	public void setAssignment(final SwitchName assigment) {
		assignment = assigment;
	}

	public void setFunction(final SwitchFunction function) {
		this.function = function;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public void setType(final SwitchType type) {
		this.type = type;
	}
}
