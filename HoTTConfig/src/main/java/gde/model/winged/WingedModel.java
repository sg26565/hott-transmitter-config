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
package gde.model.winged;

import gde.model.BaseModel;
import gde.model.Channel;
import gde.model.enums.AileronFlapType;
import gde.model.enums.ModelType;
import gde.model.enums.MotorOnC1Type;
import gde.model.enums.TailType;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author oli@treichels.de
 */
@XmlRootElement
public class WingedModel extends BaseModel {
	private AileronFlapType aileronFlapType;
	private Channel brakeInputChannel;
	private int brakeOffset;
	private boolean channel8Delay;
	private MotorOnC1Type motorOnC1Type;
	private TailType tailType;

	public WingedModel() {
		super(ModelType.Winged);
	}

	public AileronFlapType getAileronFlapType() {
		return aileronFlapType;
	}

	@XmlIDREF
	public Channel getBrakeInputChannel() {
		return brakeInputChannel;
	}

	public int getBrakeOffset() {
		return brakeOffset;
	}

	public MotorOnC1Type getMotorOnC1Type() {
		return motorOnC1Type;
	}

	public TailType getTailType() {
		return tailType;
	}

	public boolean isChannel8Delay() {
		return channel8Delay;
	}

	public void setAileronFlapType(final AileronFlapType aileronFlapType) {
		this.aileronFlapType = aileronFlapType;
	}

	public void setBrakeInputChannel(final Channel brakeInputChannel) {
		this.brakeInputChannel = brakeInputChannel;
	}

	public void setBrakeOffset(final int brakeOffset) {
		this.brakeOffset = brakeOffset;
	}

	public void setChannel8Delay(final boolean channel8Delay) {
		this.channel8Delay = channel8Delay;
	}

	public void setMotorOnC1Type(final MotorOnC1Type motorOnC1Type) {
		this.motorOnC1Type = motorOnC1Type;
	}

	public void setTailType(final TailType tailType) {
		this.tailType = tailType;
	}
}
