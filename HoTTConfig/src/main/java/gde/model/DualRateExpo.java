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

import gde.model.enums.Function;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class DualRateExpo {
	private static int NEXT_NUMBER = 0;

	private int dualRate0;
	private int dualRate1;
	private int expo0;
	private int expo1;
	private Function function;
	private String number;
	private Switch sw;

	public DualRateExpo() {
		this(Function.Unknown);
	}

	public DualRateExpo(final Function function) {
		this.function = function;
		number = Integer.toString(NEXT_NUMBER++);
	}

	public int getDualRate0() {
		return dualRate0;
	}

	public int getDualRate1() {
		return dualRate1;
	}

	public int getExpo0() {
		return expo0;
	}

	public int getExpo1() {
		return expo1;
	}

	public Function getFunction() {
		return function;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	@XmlIDREF
	public Switch getSwitch() {
		return sw;
	}

	public void setDualRate0(final int dualRate0) {
		this.dualRate0 = dualRate0;
	}

	public void setDualRate1(final int dualRate1) {
		this.dualRate1 = dualRate1;
	}

	public void setExpo0(final int expo0) {
		this.expo0 = expo0;
	}

	public void setExpo1(final int expo1) {
		this.expo1 = expo1;
	}

	public void setFunction(final Function function) {
		this.function = function;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}
}
