/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.model.enums;

import java.util.ResourceBundle;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public enum Section {
    baseSettings, modelType, servos, stickSettings, controls0, drExpo0, channel1Curve0, controlSwitches, logicalSwitches, phaseSettings, phaseAssignments,
    phaseTrim, nonDelayedChannels, timersGeneral, phaseTimer, wingMix0, helicopterMix0, linearMixer, curveMixer, mixerActive, mixOnlyChannel, dualMixer,
    swashplateMixer, failSafe, trainerPupil, outputChannel, profiTrim, trimMemory, telemetry, channelSequencer, multiChannel, ringLimiter, mp3Player, switches;

    public boolean isValidFor(final ModelType modelType) {
        switch (this) {
        case baseSettings:
        case channel1Curve0:
        case channelSequencer:
        case controlSwitches:
        case controls0:
        case curveMixer:
        case drExpo0:
        case dualMixer:
        case failSafe:
        case linearMixer:
        case logicalSwitches:
        case mixOnlyChannel:
        case mixerActive:
        case modelType:
        case mp3Player:
        case multiChannel:
        case nonDelayedChannels:
        case outputChannel:
        case phaseAssignments:
        case phaseSettings:
        case phaseTimer:
        case profiTrim:
        case ringLimiter:
        case servos:
        case stickSettings:
        case switches:
        case telemetry:
        case timersGeneral:
        case trainerPupil:
        case trimMemory:
            return true;

        case helicopterMix0:
        case swashplateMixer:
            return modelType == ModelType.Helicopter;

        case wingMix0:
        case phaseTrim:
            return modelType == ModelType.Winged;
        }

        return false;
    }

    public boolean isValidFor(final TransmitterType transmitterType) {
        switch (this) {
        // valid for all transmitters
        case baseSettings:
        case controls0:
        case drExpo0:
        case failSafe:
        case helicopterMix0:
        case linearMixer:
        case phaseTrim:
        case servos:
        case swashplateMixer:
        case switches:
        case telemetry:
        case trainerPupil:
        case wingMix0:
            return true;

        // not available on mx-12 and mx-16
        case channel1Curve0:
        case channelSequencer:
        case controlSwitches:
        case curveMixer:
        case dualMixer:
        case mixOnlyChannel:
        case mixerActive:
        case modelType:
        case multiChannel:
        case nonDelayedChannels:
        case outputChannel:
        case phaseAssignments:
        case phaseSettings:
        case phaseTimer:
        case profiTrim:
        case ringLimiter:
        case stickSettings:
        case timersGeneral:
        case trimMemory:
            return transmitterType == TransmitterType.mx20 || transmitterType == TransmitterType.mc16 || transmitterType == TransmitterType.mc20
                    || transmitterType == TransmitterType.mc32;

        // only on mc-20 and mc-32
        case logicalSwitches:
            return transmitterType == TransmitterType.mc20 || transmitterType == TransmitterType.mc32;

        // only on mc-16, mc-20 and mc-32
        case mp3Player:
            return transmitterType == TransmitterType.mc16 || transmitterType == TransmitterType.mc20 || transmitterType == TransmitterType.mc32;
        }

        return false;
    }

    /** @return the locale-dependent message */
    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}
