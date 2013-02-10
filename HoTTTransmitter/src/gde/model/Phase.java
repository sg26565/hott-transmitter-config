package gde.model;

import java.util.Map;

public class Phase {
	private Map<Function, DualRateExpo> dualRate;
	private String name;
	private PhaseType type;

	public Map<Function, DualRateExpo> getDualRate() {
		return dualRate;
	}

	public String getName() {
		return name;
	}

	public PhaseType getType() {
		return type;
	}

	public void setDualRate(Map<Function, DualRateExpo> dualRate) {
		this.dualRate = dualRate;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setType(final PhaseType type) {
		this.type = type;
	}
}
