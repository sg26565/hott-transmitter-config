<bookmarks>
	<bookmark href="#baseSettings" name="Grundeinstellungen Modell"/>
	<bookmark href="#servos" name="Servoeinstellungen"/>		
	<bookmark href="#controls0" name="Gebereinstellungen"/>
	<bookmark href="#drExpo0" name="DualRate Expo">
		<#list model.phase as phase>
			<#if phase.number != "-1" && phase.phaseType.name() != "Unused">
				<bookmark href="#drExpo${phase.number}" name="${phase?string}"/>
			</#if>
		</#list>
	</bookmark>
	<#if wingedModel??>
		<bookmark href="#phaseTrim" name="Phasentrimm"/>
		<bookmark href="#wingMix0" name="Flächenmischer"/>
	</#if>
	<#if helicopterModel??>
		<bookmark href="#helicopterMix0" name="Helikoptermix">
			<#list model.phase as phase>
				<#if phase.number != "-1" && phase.phaseType.name() != "Unused">
					<bookmark href="#helicopterMix${phase.number}" name="${phase?string}"/>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<bookmark href="#linearMixer" name="Linearmischer"/>
	<#if helicopterModel??>
		<bookmark href="#swashplateMixer" name="Taumelscheibenmischer"/>
	</#if>
	<bookmark href="#failSafe" name="Fail Safe"/>
	<bookmark href="#telemetry" name="Telemetrie"/>		
	<bookmark href="#trainerPupil" name="Lehrer/Schüler"/>
	<bookmark href="#stickTrim" name="Knüppeltrimmung"/>		
	<bookmark href="#switches" name="Schalter-/Geberzuordnungen"/>
</bookmarks>