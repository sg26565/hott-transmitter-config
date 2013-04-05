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

import gde.model.Phase;

import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author oli
 * 
 */
public class WingedPhase extends Phase {
	private WingedMixer[] wingedMixer;
	private WingedTrim wingedTrim;

	@XmlElementWrapper(name = "wingedMixers")
	public WingedMixer[] getWingedMixer() {
		return wingedMixer;
	}

	public WingedTrim getWingedTrim() {
		return wingedTrim;
	}

	public void setWingedMixer(final WingedMixer[] wingedMixers) {
		wingedMixer = wingedMixers;
	}

	public void setWingedTrim(final WingedTrim wingTrim) {
		wingedTrim = wingTrim;
	}

}
