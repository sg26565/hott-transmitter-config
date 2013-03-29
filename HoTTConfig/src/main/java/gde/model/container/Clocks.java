package gde.model.container;

import gde.model.Clock;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Clocks extends ArrayList<Clock> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "clock")
	public List<Clock> getClocks() {
		return this;
	}

	public void setClocks(final List<Clock> clocks) {
		clear();
		addAll(clocks);
	}
}
