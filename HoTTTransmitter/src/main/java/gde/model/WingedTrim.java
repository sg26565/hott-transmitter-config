package gde.model;

public class WingedTrim extends PhasedTrim {
	private int aileronPhaseTrim;
	private int aileronStickTrim;
	private int elevatorPhaseTrim;
	private int elevtorStickTrim;
	private int flapPhaseTrim;
	private PhaseType phaseType;
	private int rudderStickTrim;

	public int getAileronPhaseTrim() {
		return aileronPhaseTrim;
	}

	public int getAileronStickTrim() {
		return aileronStickTrim;
	}

	public int getElevatorPhaseTrim() {
		return elevatorPhaseTrim;
	}

	public int getElevtorStickTrim() {
		return elevtorStickTrim;
	}

	public int getFlapPhaseTrim() {
		return flapPhaseTrim;
	}

	public PhaseType getPhaseType() {
		return phaseType;
	}

	public int getRudderStickTrim() {
		return rudderStickTrim;
	}

	public void setAileronPhaseTrim(final int aileronPhaseTrim) {
		this.aileronPhaseTrim = aileronPhaseTrim;
	}

	public void setAileronStickTrim(final int aileronStickTrim) {
		this.aileronStickTrim = aileronStickTrim;
	}

	public void setElevatorPhaseTrim(final int elevatorPhaseTrim) {
		this.elevatorPhaseTrim = elevatorPhaseTrim;
	}

	public void setElevtorStickTrim(final int elevtorStickTrim) {
		this.elevtorStickTrim = elevtorStickTrim;
	}

	public void setFlapPhaseTrim(final int flapPhaseTrim) {
		this.flapPhaseTrim = flapPhaseTrim;
	}

	public void setPhaseType(final PhaseType phaseType) {
		this.phaseType = phaseType;
	}

	public void setRudderStickTrim(final int rudderStickTrim) {
		this.rudderStickTrim = rudderStickTrim;
	}
}
