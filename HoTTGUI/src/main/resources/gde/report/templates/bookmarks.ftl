<bookmarks>
	<bookmark href="#baseSettings" name="Grundeinstellungen Modell"/>
	<bookmark href="#modelType" name="<#if helicopterModel??>Helicoptertyp<#else>Modelltyp</#if>"/>
	<bookmark href="#servos" name="Servoeinstellungen"/>		
	<bookmark href="#stickSettings" name="Knüppeleinstellungen"/>
	<bookmark href="#controls0" name="Gebereinstellungen">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#controls${phase.number}" name="${phase?string}"/>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#drExpo0" name="DualRate Expo">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#drExpo${phase.number}" name="${phase?string}"/>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#channel1Curve0" name="Kanal 1 Kurve">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#channel1Curve${phase.number}" name="${phase?string}"/>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#controlSwitches" name="Geberschalter"/>
	<#if model.logicalSwitch??>
		<bookmark href="#logicalSwitches" name="logische Schalter"/>
	</#if>
	<bookmark href="#phaseSettings" name="Phaseneinstellungen"/>
	<bookmark href="#phaseAssignments" name="Phasenzuweisung"/>
	<#if wingedModel??>
		<bookmark href="#phaseTrim" name="Phasentrimm"/>
	</#if>
	<bookmark href="#nonDelayedChannels" name="unverzögerte Kanäle"/>
	<bookmark href="#timersGeneral" name="Uhren (allgemein)"/>
	<bookmark href="#phaseTimer" name="Flugphasenuhren"/>
	<#if wingedModel??>
		<bookmark href="#wingMix0" name="Flächenmischer">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#wingMix${phase.number}" name="${phase?string}"/>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<#if helicopterModel??>
		<bookmark href="#helicopterMix0" name="Helikoptermix">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#helicopterMix${phase.number}" name="${phase?string}"/>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<bookmark href="#linearMixer" name="Linearmischer"/>
	<bookmark href="#curveMixer" name="Kurvenmischer"/>
	<bookmark href="#mixerActive" name="MIX aktiv / Phase"/>
	<bookmark href="#mixOnlyChannel" name="Nur MIX Kanal"/>
	<bookmark href="#dualMixer" name="Kreuzmischer"/>
	<#if helicopterModel??>
		<bookmark href="#swashplateMixer" name="Taumelscheibenmischer"/>
	</#if>
	<bookmark href="#failSafe" name="Fail Safe"/>
	<bookmark href="#trainerPupil" name="Lehrer/Schüler"/>
	<bookmark href="#outputChannel" name="Senderausgang"/>
	<bookmark href="#profiTrim" name="Profitrimm"/>
	<bookmark href="#trimMemory" name="Trimmspeicher"/>
	<bookmark href="#telemetry" name="Telemetrie"/>		
	<bookmark href="#channelSequencer" name="Kanal Sequenzer"/>
	<bookmark href="#multiChannel" name="Multikanal"/>
	<bookmark href="#ringLimiter" name="Ringbegrenzer"/>
	<#if model.transmitterType.name() != "mx20">
		<bookmark href="#mp3Player" name="MP3-Player"/>
	</#if>
	<bookmark href="#switches" name="Schalter-/Geberzuordnungen"/>
</bookmarks>