package gde.model.container;

import gde.model.Phase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Phases implements Iterable<Phase> {
	private List<Phase> phases = new ArrayList<Phase>();

	@XmlElement(name = "phase")
	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(final List<Phase> phases) {
		this.phases = phases;
	}

	public void addPhase(final Phase phase) {
		getPhases().add(phase);
	}

	public Phase getPhase(final int number) {
		return getPhases().get(number);
	}

	public int size() {
		return getPhases().size();
	}

	@Override
	public Iterator<Phase> iterator() {
		return getPhases().iterator();
	}
}
