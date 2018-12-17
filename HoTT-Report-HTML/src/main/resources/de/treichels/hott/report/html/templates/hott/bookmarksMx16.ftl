<bookmarks>
	<bookmark href="#baseSettings" name="Grundeinstellungen Modell"></bookmark>
	<bookmark href="#servos" name="Servoeinstellungen"></bookmark>
	<bookmark href="#controls0" name="Gebereinstellungen"></bookmark>
	<bookmark href="#drExpo0" name="DualRate Expo">
		<#list model.phase as phase>
			<#if phase.number != -1 && phase.phaseType.name() != "Unused">
				<bookmark href="#drExpo${phase.number}" name="${phase.toString()}"></bookmark>
			</#if>
		</#list>
	</bookmark>
	<#if wingedModel??>
		<bookmark href="#phaseTrim" name="Phasentrimm"></bookmark>
		<bookmark href="#wingMix0" name="Flächenmischer"></bookmark>
	</#if>
	<#if helicopterModel??>
		<bookmark href="#helicopterMix0" name="Helikoptermix">
			<#list model.phase as phase>
				<#if phase.number != -1 && phase.phaseType.name() != "Unused">
					<bookmark href="#helicopterMix${phase.number}" name="${phase.toString()}"></bookmark>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<bookmark href="#linearMixer" name="Linearmischer"></bookmark>
	<#if helicopterModel??>
		<bookmark href="#swashplateMixer" name="Taumelscheibenmischer"></bookmark>
	</#if>
	<bookmark href="#failSafe" name="Fail Safe"></bookmark>
	<bookmark href="#announcements" name="Ankünden"></bookmark>
	<bookmark href="#telemetry" name="Telemetrie"></bookmark>
	<bookmark href="#trainerPupil" name="Lehrer/Schüler"></bookmark>
	<bookmark href="#stickTrim" name="Knüppeltrimmung"></bookmark>
	<bookmark href="#switches" name="Schalter-/Geberzuordnungen"></bookmark>
</bookmarks>
