package gde.model.helicopter;

import gde.model.BaseModel;
import gde.model.Switch;
import gde.model.enums.ModelType;
import gde.model.enums.PitchMin;
import gde.model.enums.RotorDirection;
import gde.model.enums.SwashplateType;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author oli@treichels.de
 */
@XmlRootElement
public class HelicopterModel extends BaseModel {
	private int autorotationPosition;
	private Switch autorotatonSwitch;
	private int expoThrottleLimit;
	private Switch markerSwitch;
	private PitchMin pitchMin;
	private RotorDirection rotorDirection;
	private boolean swashplateLinearization;
	private SwashplateType swashplateType;
	private int throttleLimitWarning;

	public HelicopterModel() {
		super(ModelType.Helicopter);
	}

	public int getAutorotationPosition() {
		return autorotationPosition;
	}

	@XmlIDREF
	public Switch getAutorotatonSwitch() {
		return autorotatonSwitch;
	}

	public int getExpoThrottleLimit() {
		return expoThrottleLimit;
	}

	@XmlIDREF
	public Switch getMarkerSwitch() {
		return markerSwitch;
	}

	public PitchMin getPitchMin() {
		return pitchMin;
	}

	public RotorDirection getRotorDirection() {
		return rotorDirection;
	}

	public SwashplateType getSwashplateType() {
		return swashplateType;
	}

	public int getThrottleLimitWarning() {
		return throttleLimitWarning;
	}

	public boolean isSwashplateLinearization() {
		return swashplateLinearization;
	}

	public void setAutorotationPosition(final int autorotationPosition) {
		this.autorotationPosition = autorotationPosition;
	}

	public void setAutorotatonSwitch(final Switch autorotatonSwitch) {
		this.autorotatonSwitch = autorotatonSwitch;
	}

	public void setExpoThrottleLimit(final int expoThrottleLimit) {
		this.expoThrottleLimit = expoThrottleLimit;
	}

	public void setMarkerSwitch(final Switch markerSwitch) {
		this.markerSwitch = markerSwitch;
	}

	public void setPitchMin(final PitchMin pitchMin) {
		this.pitchMin = pitchMin;
	}

	public void setRotorDirection(final RotorDirection rotorDirection) {
		this.rotorDirection = rotorDirection;
	}

	public void setSwashplateLinearization(final boolean swashplateLinearization) {
		this.swashplateLinearization = swashplateLinearization;
	}

	public void setSwashplateType(final SwashplateType swashplateType) {
		this.swashplateType = swashplateType;
	}

	public void setThrottleLimitWarning(final int throttleLimitWarning) {
		this.throttleLimitWarning = throttleLimitWarning;
	}
}
