package gde.model.container;

import gde.model.Switch;
import gde.model.enums.SwitchFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Switches extends HashMap<String, Switch> implements Iterable<Switch> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "switch")
	public Collection<Switch> getSwitches() {
		return values();
	}

	public void setSwitches(final Collection<Switch> switches) {
		for (final Switch sw : switches) {
			add(sw);
		}
	}

	public Switch get(final SwitchFunction func) {
		return get(func.name());
	}

	public void add(final Switch sw) {
		put(sw.getFunction().name(), sw);
	}

	@Override
	public Iterator<Switch> iterator() {
		return values().iterator();
	}
}
