package gde.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author oli@treichels.de
 */
@XmlRootElement
public class WingedModel extends BaseModel {
	private AileronFlapType aileronFlapType;
	private int brakeInput;
	private int brakeOffset;
	private boolean channel8Delay;
	private MoterOnC1Type motorOnC1Type;
	private TailType tailType;

	public WingedModel() {
		super(ModelType.Winged);
	}

	public AileronFlapType getAileronFlapType() {
		return aileronFlapType;
	}

	public int getBrakeInput() {
		return brakeInput;
	}

	public int getBrakeOffset() {
		return brakeOffset;
	}

	public MoterOnC1Type getMotorOnC1Type() {
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

	public void setBrakeInput(final int brakeInput) {
		this.brakeInput = brakeInput;
	}

	public void setBrakeOffset(final int brakeOffset) {
		this.brakeOffset = brakeOffset;
	}

	public void setChannel8Delay(final boolean channel8Delay) {
		this.channel8Delay = channel8Delay;
	}

	public void setMotorOnC1Type(final MoterOnC1Type motorOnC1Type) {
		this.motorOnC1Type = motorOnC1Type;
	}

	public void setTailType(final TailType tailType) {
		this.tailType = tailType;
	}
}
