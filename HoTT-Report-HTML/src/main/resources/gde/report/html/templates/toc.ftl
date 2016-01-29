<h1>Inhaltsverzeichnis</h1>
<a href="#baseSettings">Grundeinstellungen Modell</a>
<a href="#modelType"><#if helicopterModel??>Helicoptertyp<#else>Modelltyp</#if></a>
<a href="#servos">Servoeinstellungen</a>		
<a href="#stickSettings">Knüppeleinstellungen</a>
<div>Gebereinstellungen</div>
<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<a class="i1" href="#controls${phase.number}">${phase?string}</a>
	</#if>
</#list>
<div>DualRate Expo</div>
<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<a class="i1" href="#drExpo${phase.number}">${phase?string}</a>
	</#if>
</#list>
<div>Kanal 1 Kurve</div>
<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<a class="i1" href="#channel1Curve${phase.number}">${phase?string}</a>
	</#if>
</#list>
<a href="#controlSwitches">Geberschalter</a>
<#if model.logicalSwitch??>
	<a href="#logicalSwitches">logische Schalter</a>
</#if>
<a href="#phaseSettings">Phaseneinstellungen</a>
<a href="#phaseAssignments">Phasenzuweisung</a>
<#if wingedModel??>
	<a href="#phaseTrim">Phasentrimm</a>
</#if>
<a href="#nonDelayedChannels">unverzögerte Kanäle</a>
<a href="#timersGeneral">Uhren (allgemein)</a>
<a href="#phaseTimer">Flugphasenuhren</a>
<#if wingedModel??>
	<div>Flächenmischer</div>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<a class="i1" href="#wingMix${phase.number}">${phase?string}</a>
		</#if>
	</#list>
</#if>
<#if helicopterModel??>
	<div>Helikoptermix</div>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<a class="i1" href="#helicopterMix${phase.number}">${phase?string}</a>
		</#if>
	</#list>
</#if>
<a href="#linearMixer">Linearmischer</a>
<a href="#curveMixer">Kurvenmischer</a>
<a href="#mixerActive">MIX aktiv / Phase</a>
<a href="#mixOnlyChannel">Nur MIX Kanal</a>
<a href="#dualMixer">Kreuzmischer</a>
<#if helicopterModel??>
	<a href="#swashplateMixer">Taumelscheibenmischer</a>
</#if>
<a href="#failSafe">Fail Safe</a>
<a href="#trainerPupil">Lehrer/Schüler</a>
<a href="#outputChannel">Senderausgang</a>
<#if model.transmitterType.name() != "mx20">
	<a href="#profiTrim">Profitrimm</a>
</#if>
<a href="#trimMemory">Trimmspeicher</a>
<a href="#telemetry">Telemetrie</a>		
<a href="#channelSequencer">Kanal Sequenzer</a>
<#if model.transmitterType.name() != "mx20">
	<a href="#multiChannel">Multikanal</a>
</#if>
<a href="#ringLimiter">Ringbegrenzer</a>
<#if model.transmitterType.name() != "mx20">
	<a href="#mp3Player">MP3-Player</a>
</#if>
<a href="#switches">Schalter-/Geberzuordnungen</a>