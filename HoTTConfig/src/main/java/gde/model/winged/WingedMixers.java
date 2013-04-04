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

/**
 * @author oli@treichels.de
 */
public class WingedMixers extends PhasedMixer implements Map<String, WingedMixer>, Iterable<WingedMixer> {
	private final Map<String, WingedMixer> mixer = new TreeMap<String, WingedMixer>();

	public void add(final WingedMixer mixer) {
		this.mixer.put(mixer.getId(), mixer);
	}

	@Override
	public void clear() {
		mixer.clear();
	}

	@Override
	public boolean containsKey(final Object key) {
		return mixer.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return mixer.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, WingedMixer>> entrySet() {
		return mixer.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return mixer.equals(o);
	}

	public WingedMixer get(final Function function) {
		return mixer.get(function.name());
	}

	@Override
	public WingedMixer get(final Object key) {
		return mixer.get(key);
	}

	public Collection<WingedMixer> getMixer() {
		return mixer.values();
	}

	@Override
	public int hashCode() {
		return mixer.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return mixer.isEmpty();
	}

	@Override
	public Iterator<WingedMixer> iterator() {
		return mixer.values().iterator();
	}

	@Override
	public Set<String> keySet() {
		return mixer.keySet();
	}

	@Override
	public WingedMixer put(final String key, final WingedMixer value) {
		return mixer.put(key, value);
	}

	public WingedMixer put(final SwitchFunction function, final WingedMixer value) {
		return mixer.put(function.name(), value);
	}

	@Override
	public void putAll(final Map<? extends String, ? extends WingedMixer> m) {
		mixer.putAll(m);
	}

	@Override
	public WingedMixer remove(final Object key) {
		return mixer.remove(key);
	}

	public void setMixers(final Collection<WingedMixer> mixers) {
		mixer.clear();
		for (final WingedMixer mixer : mixers) {
			add(mixer);
		}
	}

	@Override
	public int size() {
		return mixer.size();
	}

	@Override
	public Collection<WingedMixer> values() {
		return mixer.values();
	}
}
