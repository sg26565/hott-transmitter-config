package gde.model;

public enum AileronCamberType {
	OneAil(1, 0), OneAilOneFlap(1, 1), TwoAil(2, 0), TwoAilOneFlap(2, 1), TwoAilTwoFlap(
			2, 2), TwoAilFourFlap(2, 4), FourAilTwoFalp(4, 2), FourAilFourFlap(
			4, 4);

	private int ailerons;
	private int flaps;

	private AileronCamberType(int ailerons, int flaps) {
		this.ailerons = ailerons;
		this.flaps = flaps;
	}

	public int getAilerons() {
		return ailerons;
	}

	public int getFlaps() {
		return flaps;
	}
}
