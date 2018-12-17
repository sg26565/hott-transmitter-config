<bookmarks>
	<bookmark href="#baseSettings" name="Grundeinstellungen Modell"></bookmark>
	<bookmark href="#modelType" name="<#if helicopterModel??>Helicoptertyp<#else>Modelltyp</#if>"></bookmark>
	<bookmark href="#servos" name="Servoeinstellungen"></bookmark>
	<bookmark href="#stickSettings" name="Knüppeleinstellungen"></bookmark>
	<bookmark href="#controls0" name="Gebereinstellungen">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#controls${phase.number}" name="${phase.toString()}"></bookmark>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#drExpo0" name="DualRate Expo">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#drExpo${phase.number}" name="${phase.toString()}"></bookmark>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#channel1Curve0" name="Kanal 1 Kurve">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#channel1Curve${phase.number}" name="${phase.toString()}"></bookmark>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#controlSwitches" name="Geberschalter"></bookmark>
	<#if model.logicalSwitch??>
		<bookmark href="#logicalSwitches" name="logische Schalter"></bookmark>
	</#if>
	<bookmark href="#phaseSettings" name="Phaseneinstellungen"></bookmark>
	<bookmark href="#phaseAssignments" name="Phasenzuweisung"></bookmark>
	<#if wingedModel??>
		<bookmark href="#phaseTrim" name="Phasentrimm"></bookmark>
	</#if>
	<bookmark href="#nonDelayedChannels" name="unverzögerte Kanäle"></bookmark>
	<bookmark href="#timersGeneral" name="Uhren (allgemein)"></bookmark>
	<bookmark href="#phaseTimer" name="Flugphasenuhren"></bookmark>
	<#if wingedModel??>
		<bookmark href="#wingMix0" name="Flächenmischer">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#wingMix${phase.number}" name="${phase.toString()}"></bookmark>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<#if helicopterModel??>
		<bookmark href="#helicopterMix0" name="Helikoptermix">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#helicopterMix${phase.number}" name="${phase.toString()}"></bookmark>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<bookmark href="#linearMixer" name="Linearmischer"></bookmark>
	<bookmark href="#curveMixer" name="Kurvenmischer"></bookmark>
	<bookmark href="#mixerActive" name="MIX aktiv / Phase"></bookmark>
	<bookmark href="#mixOnlyChannel" name="Nur MIX Kanal"></bookmark>
	<bookmark href="#dualMixer" name="Kreuzmischer"></bookmark>
	<#if helicopterModel??>
		<bookmark href="#swashplateMixer" name="Taumelscheibenmischer"></bookmark>
	</#if>
	<bookmark href="#failSafe" name="Fail Safe"></bookmark>
	<bookmark href="#trainerPupil" name="Lehrer/Schüler"></bookmark>
	<bookmark href="#outputChannel" name="Senderausgang"></bookmark>
	<#if model.transmitterType.name() != "mx20">
		<bookmark href="#profiTrim" name="Profitrimm"></bookmark>
	</#if>
	<bookmark href="#trimMemory" name="Trimmspeicher"></bookmark>
	<bookmark href="#telemetry" name="Telemetrie"></bookmark>
	<bookmark href="#channelSequencer" name="Kanal Sequenzer"></bookmark>
	<#if model.transmitterType.name() != "mx20">
		<bookmark href="#multiChannel" name="Multikanal"></bookmark>
	</#if>
	<bookmark href="#ringLimiter" name="Ringbegrenzer"></bookmark>
	<#if model.transmitterType.name() != "mx20">
		<bookmark href="#mp3Player" name="MP3-Player"></bookmark>
	</#if>
	<bookmark href="#switches" name="Schalter-/Geberzuordnungen"></bookmark>
</bookmarks>
