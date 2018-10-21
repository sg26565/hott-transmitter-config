/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package de.treichels.hott.model.enums

import tornadofx.*
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class SwitchFunction {
    Autorotation, AutorotationC1, AutoTrim, ChannelSequencer, Clock, Combine, Control, CutOff, Diff, Dr_Expo, DualRate, Expo, InputControl, Logical, MarkerKey,
    Mixer, Phase, PlayPause, PowerWarning, ProfiTrim, ThrottleLimit, ToggleHigh, ToggleLow, Trainer, VarioTone, VoiceRepeat, VoiceTrigger, Volume, VolumeLeft,
    VolumeRight, Unassigned, Announcement;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
