package gde.model;

import gde.model.enums.Function;
import gde.model.enums.PhaseType;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class Phase {
	private List<DualRateExpo> dualRatesExpos;
	private PhasedMixer mixer;
	private String name;
	private String number;
	private Switch sw;
	private PhasedTrim trim;
	private PhaseType type;

	public Phase(final int number) {
		this.number = Integer.toString(number);
	}

	public DualRateExpo get(final Function function) {
		for (final DualRateExpo expo : getDualRatesExpos()) {
			if (expo.getFunction() == function) {
				return expo;
			}
		}

		return null;
	}

	@XmlElement(name = "dualRateExpo")
	@XmlElementWrapper(name = "dualRateExpos")
	public List<DualRateExpo> getDualRatesExpos() {
		return dualRatesExpos;
	}

	@XmlElement(name = "phasemixer")
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

	@XmlElement(name = "phasetrim")
	public PhasedTrim getTrim() {
		return trim;
	}

	public PhaseType getType() {
		return type;
	}

	public void setDualRatesExpos(final List<DualRateExpo> dualRatesExpos) {
		this.dualRatesExpos = dualRatesExpos;
	}

	public void setMixer(final PhasedMixer mixer) {
		this.mixer = mixer;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNumber(final String number) {
		this.number = number;
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
