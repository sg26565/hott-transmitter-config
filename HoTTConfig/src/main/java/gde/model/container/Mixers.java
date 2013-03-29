package gde.model.container;

import gde.model.Mixer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Mixers implements Iterable<Mixer> {
	private List<Mixer> mixers = new ArrayList<Mixer>();

	@XmlElement(name = "mixer")
	public List<Mixer> getMixers() {
		return mixers;
	}

	public void setMixers(final List<Mixer> mixers) {
		this.mixers = mixers;
	}

	public void addMixer(final Mixer mixer) {
		getMixers().add(mixer);
	}

	public Mixer getMixer(final int number) {
		return getMixers().get(number);
	}

	public int size() {
		return getMixers().size();
	}

	@Override
	public Iterator<Mixer> iterator() {
		return getMixers().iterator();
	}
}
