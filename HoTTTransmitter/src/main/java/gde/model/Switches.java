package gde.model;

import gde.model.enums.SwitchFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Switches implements Iterable<Switch> {
	private final Map<SwitchFunction, Switch> switches = new HashMap<SwitchFunction, Switch>();

	@XmlElement(name = "switch")
	public Collection<Switch> getSwitches() {
		return switches.values();
	}

	public void setSwitches(final Collection<Switch> switches) {
		for (final Switch sw : switches) {
			addSwitch(sw);
		}
	}

	public void addSwitch(final Switch sw) {
		switches.put(SwitchFunction.valueOf(sw.getFunction()), sw);
	}

	public Switch getSwitch(final SwitchFunction function) {
		return switches.get(function);
	}

	public int size() {
		return getSwitches().size();
	}

	@Override
	public Iterator<Switch> iterator() {
		return getSwitches().iterator();
	}
}
