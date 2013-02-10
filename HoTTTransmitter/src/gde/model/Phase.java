package gde.model;

public class Phase {
	private String name;
	private PhaseType type;

	public String getName() {
		return name;
	}

	public PhaseType getType() {
		return type;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setType(final PhaseType type) {
		this.type = type;
	}
}
