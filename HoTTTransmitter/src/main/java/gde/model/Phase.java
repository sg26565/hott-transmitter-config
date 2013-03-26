package gde.model;

import gde.model.enums.Function;
import gde.model.enums.PhaseType;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Phase {
	private Map<Function, DualRateExpo> dualRate;
	private PhasedMixer mixer;
	private String name;
	private final String number;
	private Switch sw;
	private PhasedTrim trim;
	private PhaseType type;

	public Phase(final int number) {
		this.number = Integer.toString(number);
	}

	public Map<Function, DualRateExpo> getDualRate() {
		return dualRate;
	}

	public PhasedMixer getMixer() {
		return mixer;
	}

	public String getName() {
		return name;
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

	public PhasedTrim getTrim() {
		return trim;
	}

	public PhaseType getType() {
		return type;
	}

	public void setDualRate(final Map<Function, DualRateExpo> dualRate) {
		this.dualRate = dualRate;
	}

	public void setMixer(final PhasedMixer mixer) {
		this.mixer = mixer;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}

	public void setTrim(final PhasedTrim trim) {
		this.trim = trim;
	}

	public void setType(final PhaseType type) {
		this.type = type;
	}
}
