package gde.model.container;

import gde.model.Phase;
import gde.model.enums.PhaseType;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Phases extends ArrayList<Phase> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "phase")
	public List<Phase> getPhases() {
		return this;
	}

	public void setPhases(final List<Phase> phases) {
		clear();
		addAll(phases);
	}

	public Phase get(final PhaseType type) {
		for (final Phase phase : this) {
			if (phase.getType() == type) {
				return phase;
			}
		}

		return null;
	}
}
