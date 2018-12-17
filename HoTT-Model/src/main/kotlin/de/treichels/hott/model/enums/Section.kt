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

import de.treichels.hott.util.get
import java.util.*

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
enum class Section {
    baseSettings, modelType, servos, stickSettings, controls0, drExpo0, channel1Curve0, controlSwitches, logicalSwitches, phaseSettings, phaseAssignments,
    phaseTrim, nonDelayedChannels, timersGeneral, phaseTimer, wingMix0, helicopterMix0, linearMixer, curveMixer, mixerActive, mixOnlyChannel, dualMixer,
    swashplateMixer, failSafe, trainerPupil, outputChannel, profiTrim, trimMemory, telemetry, channelSequencer, multiChannel, ringLimiter, mp3Player, switches;

    fun isValidFor(type: ModelType): Boolean {
        return when (this) {
            baseSettings, channel1Curve0, channelSequencer, controlSwitches, controls0, curveMixer, drExpo0, dualMixer, failSafe, linearMixer, logicalSwitches, mixOnlyChannel, mixerActive, modelType, mp3Player, multiChannel, nonDelayedChannels, outputChannel, phaseAssignments, phaseSettings, phaseTimer, profiTrim, ringLimiter, servos, stickSettings, switches, telemetry, timersGeneral, trainerPupil, trimMemory -> true

            helicopterMix0, swashplateMixer -> type == ModelType.Helicopter

            wingMix0, phaseTrim -> type == ModelType.Winged
        }
    }

    fun isValidFor(transmitterType: TransmitterType): Boolean {
        return when (this) {
            // valid for all transmitters
            baseSettings, controls0, drExpo0, failSafe, helicopterMix0, linearMixer, phaseTrim, servos, swashplateMixer, switches, telemetry, trainerPupil, wingMix0 -> true

            // not available on mx-12 and mx-16
            channel1Curve0, channelSequencer, controlSwitches, curveMixer, dualMixer, mixOnlyChannel, mixerActive, modelType, multiChannel, nonDelayedChannels, outputChannel, phaseAssignments, phaseSettings, phaseTimer, profiTrim, ringLimiter, stickSettings, timersGeneral, trimMemory -> (transmitterType == TransmitterType.mx20 || transmitterType == TransmitterType.mc16 || transmitterType == TransmitterType.mc20
                    || transmitterType == TransmitterType.mc32)

            // only on mc-20 and mc-32
            logicalSwitches -> transmitterType == TransmitterType.mc20 || transmitterType == TransmitterType.mc32

            // only on mc-16, mc-20 and mc-32
            mp3Player -> transmitterType == TransmitterType.mc16 || transmitterType == TransmitterType.mc20 || transmitterType == TransmitterType.mc32
        }
    }

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
