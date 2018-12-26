package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

enum class Menus {
    ModelSelect, CopyErase, SuppressModels, BaseSetupModel, ModelType, ServoAdjustment, StickMode,
    ControlAdjustment, DualRateExpo, Channel1Curve, SwitchDisplay, ControlSwitches, LogicalSwitches,
    Announcement, PhaseSettings, PhaseAssignments, PhaseTrim, NonDelayedChannels, TimersGeneral,
    PhaseTimers, WingMixers, FreeMixers, MixerActivePhase, MixOnlyChannel, DualMixer, FailSafeAdjustments,
    TeacherPupil, TransmitterOutputSwap, ProfiTrim, TimMemory, Telemetry, ChannelSequencer, MultiChannel,
    MP3Player, BasicSettings, Vibration, ServoDisplay, ServerTest, CodeLock, InfoDisplay;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
