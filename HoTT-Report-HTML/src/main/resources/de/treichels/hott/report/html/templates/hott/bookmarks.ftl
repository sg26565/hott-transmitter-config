<bookmarks>
	<#if model.isMenuEnabled("BaseSetupModel")>
		<bookmark href="#baseSettings" name="Grundeinstellungen Modell"></bookmark>
	</#if>
	<#if model.isMenuEnabled("ModelType")>
		<bookmark href="#modelType" name="<#if model.modelType.name() == "Helicopter">Helicoptertyp<#else>Modelltyp</#if>"></bookmark>
	</#if>
	<#if model.isMenuEnabled("ServoAdjustment")>
		<bookmark href="#servos" name="Servoeinstellungen"></bookmark>
	</#if>
	<#if model.isMenuEnabled("StickMode")>
		<bookmark href="#stickSettings" name="Knüppeleinstellungen"></bookmark>
	</#if>
	<#if model.isMenuEnabled("ControlAdjustment")>
		<bookmark href="#controls0" name="Gebereinstellungen">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#controls${phase.number}" name="${phase.toString()}"></bookmark>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<#if model.isMenuEnabled("DualRateExpo")>
		<bookmark href="#drExpo0" name="DualRate Expo">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#drExpo${phase.number}" name="${phase.toString()}"></bookmark>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<#if model.isMenuEnabled("Channel1Curve")>
		<bookmark href="#channel1Curve0" name="Kanal 1 Kurve">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#channel1Curve${phase.number}" name="${phase.toString()}"></bookmark>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<#if model.isMenuEnabled("ControlSwitches")>
		<bookmark href="#controlSwitches" name="Geberschalter"></bookmark>
	</#if>
	<#if model.logicalSwitch?size &gt; 0 && model.isMenuEnabled("LogicalSwitches")>
		<bookmark href="#logicalSwitches" name="logische Schalter"></bookmark>
	</#if>
	<#if model.isMenuEnabled("Announcement")>
		<bookmarks href="#announcements0" name="Ankünden">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#announcements${phase.number}" name="${phase.toString()}"></bookmark>
				</#if>
			</#list>
		</bookmarks>
	</#if>
	<#if model.isMenuEnabled("PhaseSettings")>
		<bookmark href="#phaseSettings" name="Phaseneinstellungen"></bookmark>
	</#if>
	<#if model.phaseAssignment?? && model.isMenuEnabled("PhaseAssignments")>
		<bookmark href="#phaseAssignments" name="Phasenzuweisung"></bookmark>
	</#if>
	<#if model.modelType.name() == "Winged" && model.isMenuEnabled("PhaseTrim")>
		<bookmark href="#phaseTrim" name="Phasentrimm"></bookmark>
	</#if>
	<#if model.isMenuEnabled("NonDelayedChannels")>
		<bookmark href="#nonDelayedChannels" name="unverzögerte Kanäle"></bookmark>
	</#if>
	<#if model.isMenuEnabled("TimersGeneral")>
		<bookmark href="#timersGeneral" name="Uhren (allgemein)"></bookmark>
	</#if>
	<#if model.isMenuEnabled("PhaseTimers")>
		<bookmark href="#phaseTimer" name="Flugphasenuhren"></bookmark>
		<#if model.lapStore.laps?size &gt; 0>
			<bookmark href="#lapTimer" name="Rundenzähler"></bookmark>
		</#if>
	</#if>
	<#if model.isMenuEnabled("WingMixers")>
		<#if model.modelType.name() == "Winged">
			<bookmark href="#wingMix0" name="Flächenmischer">
				<#list model.phase as phase>
					<#if phase.phaseType.name() != "Unused">
						<bookmark href="#wingMix${phase.number}" name="${phase.toString()}"></bookmark>
					</#if>
				</#list>
			</bookmark>
		<#elseif model.modelType.name() == "Helicopter">
			<bookmark href="#helicopterMix0" name="Helikoptermix">
				<#list model.phase as phase>
					<#if phase.phaseType.name() != "Unused">
						<bookmark href="#helicopterMix${phase.number}" name="${phase.toString()}"></bookmark>
					</#if>
				</#list>
			</bookmark>
		</#if>
	</#if>
	<#if model.isMenuEnabled("FreeMixers")>
		<bookmark href="#linearMixer" name="Linearmischer"></bookmark>
		<bookmark href="#curveMixer" name="Kurvenmischer"></bookmark>
	</#if>
	<#if model.isMenuEnabled("MixerActivePhase")>
		<bookmark href="#mixerActive" name="MIX aktiv / Phase"></bookmark>
	</#if>
	<#if model.isMenuEnabled("MixOnlyChannel")>
		<bookmark href="#mixOnlyChannel" name="Nur MIX Kanal"></bookmark>
	</#if>
	<#if model.isMenuEnabled("DualMixer")>
		<bookmark href="#dualMixer" name="Kreuzmischer"></bookmark>
	</#if>
	<#if model.modelType.name() == "Helicopter" && model.isMenuEnabled("WingMixers") && helicopterModel.swashplateType.name() != "OneServo">
		<bookmark href="#swashplateMixer" name="Taumelscheibenmischer"></bookmark>
	</#if>
	<#if model.isMenuEnabled("FailSafeAdjustments")>
		<bookmark href="#failSafe" name="Fail Safe"></bookmark>
	</#if>
	<#if model.isMenuEnabled("TeacherPupil")>
		<bookmark href="#trainerPupil" name="Lehrer/Schüler"></bookmark>
	</#if>
	<#if model.isMenuEnabled("TransmitterOutputSwap")>
		<bookmark href="#outputChannel" name="Senderausgang"></bookmark>
	</#if>
	<#if model.transmitterType.name() != "mx20" && model.isMenuEnabled("ProfiTrim")>
		<bookmark href="#profiTrim" name="Profitrimm"></bookmark>
	</#if>
	<#if model.isMenuEnabled("TimMemory")>
		<bookmark href="#trimMemory" name="Trimmspeicher"></bookmark>
	</#if>
	<#if model.isMenuEnabled("Telemetry")>
		<bookmark href="#telemetry" name="Telemetrie"></bookmark>
	</#if>
	<#if model.channelSequencer?? && model.isMenuEnabled("ChannelSequencer")>
		<bookmark href="#channelSequencer" name="Kanal Sequenzer"></bookmark>
	</#if>
	<#if model.transmitterType.name() != "mx20" && model.isMenuEnabled("MultiChannel")>
		<bookmark href="#multiChannel" name="Multikanal"></bookmark>
	</#if>
	<#assign show=false/>
	<#list model.ringLimiter as limit>
		<#if limit.enabled>
			<#assign show=true/>
			<#break>
		</#if>
	</#list>
	<#if show>
		<bookmark href="#ringLimiter" name="Ringbegrenzer"></bookmark>
	</#if>
	<#if model.transmitterType.name() != "mx20" && model.isMenuEnabled("MP3Player")>
		<bookmark href="#mp3Player" name="MP3-Player"></bookmark>
	</#if>
	<#if model.isMenuEnabled("SwitchDisplay")>
		<bookmark href="#switches" name="Schalter-/Geberzuordnungen"></bookmark>
	</#if>
</bookmarks>
