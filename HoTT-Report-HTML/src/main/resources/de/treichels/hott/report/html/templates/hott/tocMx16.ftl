<#--noinspection HtmlUnknownAnchorTarget-->
<h1>Inhaltsverzeichnis</h1>
<a href="#baseSettings">Grundeinstellungen Modell</a>
<a href="#servos">Servoeinstellungen</a>		
<a href="#controls0">Gebereinstellungen</a>
<div>DualRate Expo</div>
<#list model.phase as phase>
	<#if phase.number != -1 && phase.phaseType.name() != "Unused">
		<a class="i1" href="#drExpo${phase.number}">${phase.toString()}</a>
	</#if>
</#list>
<#if wingedModel??>
	<a href="#phaseTrim">Phasentrimm</a>
	<a href="#wingMix0">Flächenmischer</a>
</#if>
<#if helicopterModel??>
	<div>Helikoptermix</div>
	<#list model.phase as phase>
		<#if phase.number != -1 && phase.phaseType.name() != "Unused">
			<a class="i1" href="#helicopterMix${phase.number}">${phase.toString()}</a>
		</#if>
	</#list>
</#if>
<a href="#linearMixer">Linearmischer</a>
<#if helicopterModel??>
	<a href="#swashplateMixer">Taumelscheibenmischer</a>
</#if>
<a href="#failSafe">Fail Safe</a>
<a href="#announcements">Ankünden</a>		
<a href="#telemetry">Telemetrie</a>		
<a href="#trainerPupil">Lehrer/Schüler</a>
<a href="#stickTrim">Knüppeltrimmung</a>		
<a href="#switches">Schalter-/Geberzuordnungen</a>
