package gde.model.winged;

import gde.model.PhasedMixer;

/**
 * @author oli@treichels.de
 */
public class WingedMixer extends PhasedMixer {
	private int diffAileron;
	private int diffFlap;
	private int diffReduction;
	private int mixAileronFlap;
	private int mixAileronRudder;
	private int mixBrakeAileron;
	private int mixBrakeElevator;
	private int mixBrakeFlap;
	private int mixElevatorAileron;
	private int mixElevatorFlap;
	private int mixFlapAileron;
	private int mixFlapElevator;

	public int getDiffAileron() {
		return diffAileron;
	}

	public int getDiffFlap() {
		return diffFlap;
	}

	public int getDiffReduction() {
		return diffReduction;
	}

	public int getMixAileronFlap() {
		return mixAileronFlap;
	}

	public int getMixAileronRudder() {
		return mixAileronRudder;
	}

	public int getMixBrakeAileron() {
		return mixBrakeAileron;
	}

	public int getMixBrakeElevator() {
		return mixBrakeElevator;
	}

	public int getMixBrakeFlap() {
		return mixBrakeFlap;
	}

	public int getMixElevatorAileron() {
		return mixElevatorAileron;
	}

	public int getMixElevatorFlap() {
		return mixElevatorFlap;
	}

	public int getMixFlapAileron() {
		return mixFlapAileron;
	}

	public int getMixFlapElevator() {
		return mixFlapElevator;
	}

	public void setDiffAileron(final int diffAileron) {
		this.diffAileron = diffAileron;
	}

	public void setDiffFlap(final int diffFlap) {
		this.diffFlap = diffFlap;
	}

	public void setDiffReduction(final int diffReduction) {
		this.diffReduction = diffReduction;
	}

	public void setMixAileronFlap(final int mixAileronFlap) {
		this.mixAileronFlap = mixAileronFlap;
	}

	public void setMixAileronRudder(final int mixAileronRudder) {
		this.mixAileronRudder = mixAileronRudder;
	}

	public void setMixBrakeAileron(final int mixBreakAileron) {
		this.mixBrakeAileron = mixBreakAileron;
	}

	public void setMixBrakeElevator(final int mixBreakEelvator) {
		mixBrakeElevator = mixBreakEelvator;
	}

	public void setMixBrakeFlap(final int mixBreakFlap) {
		this.mixBrakeFlap = mixBreakFlap;
	}

	public void setMixElevatorAileron(final int mixElevatorAileron) {
		this.mixElevatorAileron = mixElevatorAileron;
	}

	public void setMixElevatorFlap(final int mixElevatorFlap) {
		this.mixElevatorFlap = mixElevatorFlap;
	}

	public void setMixFlapAileron(final int mixFlapAileron) {
		this.mixFlapAileron = mixFlapAileron;
	}

	public void setMixFlapElevator(final int mixFlapElevator) {
		this.mixFlapElevator = mixFlapElevator;
	}
}
