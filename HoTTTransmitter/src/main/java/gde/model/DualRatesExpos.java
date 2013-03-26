package gde.model;

import gde.model.enums.Function;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class DualRatesExpos implements Iterable<DualRateExpo> {
	private final Map<Function, DualRateExpo> dualRateExpos = new HashMap<Function, DualRateExpo>();

	@XmlElement(name = "dualRateExpo")
	public Collection<DualRateExpo> getDualRateExpoes() {
		return dualRateExpos.values();
	}

	public void setDualRateExpoes(final Collection<DualRateExpo> dualRateExpoes) {
		for (final DualRateExpo sw : dualRateExpoes) {
			addDualRateExpo(sw);
		}
	}

	public void addDualRateExpo(final DualRateExpo sw) {
		dualRateExpos.put(Function.valueOf(sw.getFunction()), sw);
	}

	public DualRateExpo getDualRateExpo(final Function function) {
		return dualRateExpos.get(function);
	}

	public int size() {
		return getDualRateExpoes().size();
	}

	@Override
	public Iterator<DualRateExpo> iterator() {
		return getDualRateExpoes().iterator();
	}
}
