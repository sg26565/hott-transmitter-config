package gde.model;

public class DualRateExpo {
	private final int[] dualRate = new int[2];
	private final int[] expo = new int[2];
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
