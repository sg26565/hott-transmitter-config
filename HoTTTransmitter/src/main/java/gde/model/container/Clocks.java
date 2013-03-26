package gde.model.container;

import gde.model.Clock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Clocks implements Iterable<Clock> {
	private List<Clock> clocks = new ArrayList<Clock>();

	@XmlElement(name = "clock")
	public List<Clock> getClocks() {
		return clocks;
	}

	public void setClocks(final List<Clock> clocks) {
		this.clocks = clocks;
	}

	public void addClock(final Clock clock) {
		getClocks().add(clock);
	}

	public Clock getClock(final int number) {
		return getClocks().get(number);
	}

	public int size() {
		return getClocks().size();
	}

	@Override
	public Iterator<Clock> iterator() {
		return getClocks().iterator();
	}
}
