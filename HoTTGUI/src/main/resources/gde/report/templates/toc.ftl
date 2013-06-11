<h1>Inhaltsverzeichnis</h1>
<a href="#baseSettings">Grundeinstellungen Modell</a>
<a href="#modelType"><#if helicopterModel??>Helicoptertyp<#else>Modelltyp</#if></a>
<a href="#servos">Servoeinstellungen</a>		
<a href="#stickSettings">Knüppeleinstellungen</a>
<a href="#controls">Gebereinstellungen</a>
<a href="#drExpo">DualRate Expo</a>
<a href="#channel1Curve">Kanal 1 Kurve</a>
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
	<a href="#wingMix">Flächenmischer</a>
</#if>
<#if helicopterModel??>
<a href="#helicopterMix">Helikoptermix</a>
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
<a href="#profiTrim">Profitrimm</a>
<a href="#trimMemory">Trimmspeicher</a>
<a href="#telemetry">Telemetrie</a>		
<a href="#channelSequencer">Kanal Sequenzer</a>
<a href="#multiChannel">Multikanal</a>
<a href="#ringLimiter">Ringbegrenzer</a>
<a href="#mp3Player">MP3-Player</a>
<a href="#switches">Schalter-/Geberzuordnungen</a>