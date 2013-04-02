/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gde.model.winged;

import gde.model.PhasedMixer;
import gde.model.enums.Function;
import gde.model.enums.SwitchFunction;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class WingedMixers extends PhasedMixer implements Map<String, WingedMixer>, Iterable<WingedMixer> {
	private final Map<String, WingedMixer> mixers = new TreeMap<String, WingedMixer>();

	public void add(final WingedMixer mixer) {
		mixers.put(mixer.getFunction().name(), mixer);
	}

	@Override
	public void clear() {
		mixers.clear();
	}

	@Override
	public boolean containsKey(final Object key) {
		return mixers.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return mixers.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, WingedMixer>> entrySet() {
		return mixers.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return mixers.equals(o);
	}

	public WingedMixer get(final Function function) {
		return mixers.get(function.name());
	}

	@Override
	public WingedMixer get(final Object key) {
		return mixers.get(key);
	}

	@XmlElement(name = "mixer")
	public Collection<WingedMixer> getMixers() {
		return mixers.values();
	}

	@Override
	public int hashCode() {
		return mixers.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return mixers.isEmpty();
	}

	@Override
	public Iterator<WingedMixer> iterator() {
		return mixers.values().iterator();
	}

	@Override
	public Set<String> keySet() {
		return mixers.keySet();
	}

	@Override
	public WingedMixer put(final String key, final WingedMixer value) {
		return mixers.put(key, value);
	}

	public WingedMixer put(final SwitchFunction function, final WingedMixer value) {
		return mixers.put(function.name(), value);
	}

	@Override
	public void putAll(final Map<? extends String, ? extends WingedMixer> m) {
		mixers.putAll(m);
	}

	@Override
	public WingedMixer remove(final Object key) {
		return mixers.remove(key);
	}

	public void setMixers(final Collection<WingedMixer> mixers) {
		this.mixers.clear();
		for (final WingedMixer mixer : mixers) {
			add(mixer);
		}
	}

	@Override
	public int size() {
		return mixers.size();
	}

	@Override
	public Collection<WingedMixer> values() {
		return mixers.values();
	}
}
