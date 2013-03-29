package gde.model;

import gde.model.enums.Function;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author oli@treichels.de
 */
public class DualRateExpo {
	private int dualRate0;
	private int dualRate1;
	private int expo0;
	private int expo1;
	private final String function;
	private Switch sw;

	public DualRateExpo(final Function function) {
		this.function = function.toString();
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

	@XmlAttribute
	@XmlID
	public String getFunction() {
		return function;
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

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}
}
