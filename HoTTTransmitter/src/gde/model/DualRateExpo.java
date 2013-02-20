package gde.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;

public class DualRateExpo {
	@XmlList
	private final int[] dualRate = new int[2];
	@XmlList
	private final int[] expo = new int[2];
	@XmlElement(name = "switch")
	private Switch sw;

	public int getDualRate(final int pos) {
		return dualRate[pos];
	}

	public int getExpo(final int pos) {
		return expo[pos];
	}

	public Switch getSwich() {
		return sw;
	}

	public void setDualRate(final int dualRate, final int pos) {
		this.dualRate[pos] = dualRate;
	}

	public void setExpo(final int expo, final int pos) {
		this.expo[pos] = expo;
	}

	public void setSwitch(final Switch sw) {
		this.sw = sw;
	}
}
