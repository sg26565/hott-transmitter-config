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
package gde.model.enums;

import java.util.ResourceBundle;

public enum SwitchAssignment {
    DG1(SwitchType.InputControl, false), DG2(SwitchType.InputControl, false), DG3(SwitchType.InputControl, false), DG4(SwitchType.InputControl, false),
    DG5(SwitchType.InputControl, false), FX(SwitchType.Fixed, true), FXi(SwitchType.Fixed, true), G1(SwitchType.ControlSwitch, true),
    G2(SwitchType.ControlSwitch, true), G3(SwitchType.ControlSwitch, true), G4(SwitchType.ControlSwitch, true), G5(SwitchType.ControlSwitch, true),
    G6(SwitchType.ControlSwitch, true), G7(SwitchType.ControlSwitch, true), G8(SwitchType.ControlSwitch, true), GB1(SwitchType.InputControl, false),
    GB10(SwitchType.InputControl, false), GB2(SwitchType.InputControl, false), GB3(SwitchType.InputControl, false), GB4(SwitchType.InputControl, false),
    GB5(SwitchType.InputControl, false), GB6(SwitchType.InputControl, false), GB7(SwitchType.InputControl, false), GB8(SwitchType.InputControl, false),
    GB9(SwitchType.InputControl, false), Gi1(SwitchType.ControlSwitch, true), Gi2(SwitchType.ControlSwitch, true), Gi3(SwitchType.ControlSwitch, true),
    Gi4(SwitchType.ControlSwitch, true), Gi5(SwitchType.ControlSwitch, true), Gi6(SwitchType.ControlSwitch, true), Gi7(SwitchType.ControlSwitch, true),
    Gi8(SwitchType.ControlSwitch, true), L1(SwitchType.LogicalSwitch, true), L2(SwitchType.LogicalSwitch, true), L3(SwitchType.LogicalSwitch, true),
    L4(SwitchType.LogicalSwitch, true), L5(SwitchType.LogicalSwitch, true), L6(SwitchType.LogicalSwitch, true), L7(SwitchType.LogicalSwitch, true),
    L8(SwitchType.LogicalSwitch, true), Li1(SwitchType.LogicalSwitch, true), Li2(SwitchType.LogicalSwitch, true), Li3(SwitchType.LogicalSwitch, true),
    Li4(SwitchType.LogicalSwitch, true), Li5(SwitchType.LogicalSwitch, true), Li6(SwitchType.LogicalSwitch, true), Li7(SwitchType.LogicalSwitch, true),
    Li8(SwitchType.LogicalSwitch, true), SD1(SwitchType.InputControl, false), SD2(SwitchType.InputControl, false), SR1(SwitchType.InputControl, false),
    SR2(SwitchType.InputControl, false), SR3(SwitchType.InputControl, false), SW1(SwitchType.ToggleSwitch, false), SW10(SwitchType.ToggleSwitch, false),
    SW11(SwitchType.ToggleSwitch, false), SW12(SwitchType.ToggleSwitch, false), SW13(SwitchType.ToggleSwitch, false), SW14(SwitchType.ToggleSwitch, false),
    SW15(SwitchType.ToggleSwitch, false), SW16(SwitchType.ToggleSwitch, false), SW17(SwitchType.ToggleSwitch, false), SW18(SwitchType.ToggleSwitch, false),
    SW19(SwitchType.ToggleSwitch, false), SW20(SwitchType.ToggleSwitch, false), SW21(SwitchType.ToggleSwitch, false), SW22(SwitchType.ToggleSwitch, false),
    SW23(SwitchType.ToggleSwitch, false), SW2(SwitchType.ToggleSwitch, false), SW3(SwitchType.ToggleSwitch, false), SW4(SwitchType.ToggleSwitch, false),
    SW5(SwitchType.ToggleSwitch, false), SW6(SwitchType.ToggleSwitch, false), SW7(SwitchType.ToggleSwitch, false), SW8(SwitchType.ToggleSwitch, false),
    SW9(SwitchType.ToggleSwitch, false), TR1(SwitchType.InputControl, false), TR2(SwitchType.InputControl, false), TR3(SwitchType.InputControl, false),
    TR4(SwitchType.InputControl, false), UG1(SwitchType.ToggleSwitch, false), UG2(SwitchType.ToggleSwitch, false), Unassigned(SwitchType.Unknown, true),
    Unknown(SwitchType.Unknown, false);

    private final SwitchType type;
    private final boolean unidirectional;

    private SwitchAssignment(final SwitchType type, final boolean unidirectional) {
        this.type = type;
        this.unidirectional = unidirectional;
    }

    public SwitchType getType() {
        return type;
    }

    public boolean isUnidirectional() {
        return unidirectional;
    }

    /** @return the locale-dependent message */
    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}
