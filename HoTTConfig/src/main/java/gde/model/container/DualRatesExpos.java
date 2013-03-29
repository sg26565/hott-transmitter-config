package gde.model.container;

import gde.model.DualRateExpo;
import gde.model.enums.Function;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class DualRatesExpos extends HashMap<Function, DualRateExpo> implements Iterable<DualRateExpo> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "dualRateExpo")
	public Collection<DualRateExpo> getDualRateExpoes() {
		return values();
	}

	public void setDualRateExpoes(final Collection<DualRateExpo> dualRateExpoes) {
		for (final DualRateExpo sw : dualRateExpoes) {
			add(sw);
		}
	}

	public void add(final DualRateExpo sw) {
		put(Function.valueOf(sw.getFunction()), sw);
	}

	@Override
	public Iterator<DualRateExpo> iterator() {
		return values().iterator();
	}

}
