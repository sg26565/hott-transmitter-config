package gde.model.container;

import gde.model.Mixer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Mixers extends ArrayList<Mixer> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "mixer")
	public List<Mixer> getMixers() {
		return this;
	}

	public void setMixers(final List<Mixer> mixers) {
		clear();
		addAll(mixers);
	}
}
