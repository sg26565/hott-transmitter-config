<#--noinspection HtmlUnknownAnchorTarget-->
<h1>Inhaltsverzeichnis</h1>
<#if model.isMenuEnabled("BaseSetupModel")>
	<a href="#baseSettings">Grundeinstellungen Modell</a>
</#if>
<#if model.isMenuEnabled("ModelType")>
	<a href="#modelType"><#if model.modelType.name() == "Helicopter">Helicoptertyp<#else>Modelltyp</#if></a>
</#if>
<#if model.isMenuEnabled("ServoAdjustment")>
	<a href="#servos">Servoeinstellungen</a>
</#if>
<#if model.isMenuEnabled("StickMode")>
	<a href="#stickSettings">Knüppeleinstellungen</a>
</#if>
<#if model.isMenuEnabled("ControlAdjustment")>
	<a href="#controls0">Gebereinstellungen</a>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<a class="i1" href="#controls${phase.number}">${phase.toString()}</a>
		</#if>
	</#list>
</#if>
<#if model.isMenuEnabled("DualRateExpo")>
	<a href="#drExpo0">DualRate Expo</a>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<a class="i1" href="#drExpo${phase.number}">${phase.toString()}</a>
		</#if>
	</#list>
</#if>
<#if model.isMenuEnabled("Channel1Curve")>
	<a href="#channel1Curve0">Kanal 1 Kurve</a>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<a class="i1" href="#channel1Curve${phase.number}">${phase.toString()}</a>
		</#if>
	</#list>
</#if>
<#if model.isMenuEnabled("ControlSwitches")>
	<a href="#controlSwitches">Geberschalter</a>
</#if>
<#if model.logicalSwitch?size &gt; 0 && model.isMenuEnabled("LogicalSwitches")>
	<a href="#logicalSwitches">logische Schalter</a>
</#if>
<#if model.isMenuEnabled("Announcement")>
	<a href="#announcements0">Ankünden</a>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<a class="i1" href="#announcements${phase.number}">${phase.toString()}</a>
		</#if>
	</#list>
</#if>
<#if model.isMenuEnabled("PhaseSettings")>
	<a href="#phaseSettings">Phaseneinstellungen</a>
</#if>
<#if model.phaseAssignment?? && model.isMenuEnabled("PhaseAssignments")>
	<a href="#phaseAssignments">Phasenzuweisung</a>
</#if>
<#if model.modelType.name() == "Winged" && model.isMenuEnabled("PhaseTrim")>
	<a href="#phaseTrim">Phasentrimm</a>
</#if>
<#if model.isMenuEnabled("NonDelayedChannels")>
	<a href="#nonDelayedChannels">unverzögerte Kanäle</a>
</#if>
<#if model.isMenuEnabled("TimersGeneral")>
	<a href="#timersGeneral">Uhren (allgemein)</a>
</#if>
<#if model.isMenuEnabled("PhaseTimers")>
	<a href="#phaseTimer">Flugphasenuhren</a>
	<#if model.lapStore.laps?size &gt; 0>
		<a href="#lapTimer">Rundenzähler</a>
	</#if>
</#if>
<#if model.isMenuEnabled("WingMixers")>
	<#if model.modelType.name() == "Winged">
		<a href="#wingMix0">Flächenmischer</a>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<a class="i1" href="#wingMix${phase.number}">${phase.toString()}</a>
			</#if>
		</#list>
	</#if>
	<#if model.modelType.name() == "Helicopter">
		<a href="#helicopterMix0">Helikoptermix</a>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<a class="i1" href="#helicopterMix${phase.number}">${phase.toString()}</a>
			</#if>
		</#list>
	</#if>
</#if>
<#if model.isMenuEnabled("FreeMixers")>
	<a href="#linearMixer">Linearmischer</a>
	<a href="#curveMixer">Kurvenmischer</a>
</#if>
<#if model.isMenuEnabled("MixerActivePhase")>
	<a href="#mixerActive">MIX aktiv / Phase</a>
</#if>
<#if model.isMenuEnabled("MixOnlyChannel")>
	<a href="#mixOnlyChannel">Nur MIX Kanal</a>
</#if>
<#if model.isMenuEnabled("DualMixer")>
	<a href="#dualMixer">Kreuzmischer</a>
</#if>
<#if model.modelType.name() == "Helicopter" && model.isMenuEnabled("WingMixers") && helicopterModel.swashplateType.name() != "OneServo">
	<a href="#swashplateMixer">Taumelscheibenmischer</a>
</#if>
<#if model.isMenuEnabled("FailSafeAdjustments")>
	<a href="#failSafe">Fail Safe</a>
</#if>
<#if model.isMenuEnabled("TeacherPupil")>
	<a href="#trainerPupil">Lehrer/Schüler</a>
</#if>
<#if model.isMenuEnabled("TransmitterOutputSwap")>
	<a href="#outputChannel">Senderausgang</a>
</#if>
<#if model.transmitterType.name() != "mx20" && model.isMenuEnabled("ProfiTrim")>
	<a href="#profiTrim">Profitrimm</a>
</#if>
<#if model.isMenuEnabled("TimMemory")>
	<a href="#trimMemory">Trimmspeicher</a>
</#if>
<#if model.isMenuEnabled("Telemetry")>
	<a href="#telemetry">Telemetrie</a>
</#if>
<#if model.channelSequencer?? && model.isMenuEnabled("ChannelSequencer")>
	<a href="#channelSequencer">Kanal Sequenzer</a>
</#if>
<#if model.transmitterType.name() != "mx20" && model.isMenuEnabled("MultiChannel")>
	<a href="#multiChannel">Multikanal</a>
</#if>
<#assign show=false/>
<#list model.ringLimiter as limit>
	<#if limit.enabled>
		<#assign show=true/>
		<#break>
	</#if>
</#list>
<#if show>
	<a href="#ringLimiter">Ringbegrenzer</a>
</#if>
<#if model.transmitterType.name() != "mx20" && model.isMenuEnabled("MP3Player")>
	<a href="#mp3Player">MP3-Player</a>
</#if>
<#if model.isMenuEnabled("SwitchDisplay")>
	<a href="#switches">Schalter-/Geberzuordnungen</a>
</#if>
