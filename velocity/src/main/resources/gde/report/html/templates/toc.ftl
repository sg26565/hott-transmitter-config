<h1>Inhaltsverzeichnis</h1>
<a href="#baseSettings">Grundeinstellungen Modell</a>
<a href="#modelType">#if ($model.modelType.name() == "Helicopter")Helikoptertyp#{else}Modelltyp#end</a>
<a href="#servos">Servoeinstellungen</a>		
<a href="#stickSettings">Knüppeleinstellungen</a>
<div>Gebereinstellungen</div>
#foreach ($phase in $model.phase)
	#if ($phase.phaseType.name() != "Unused")
		<a class="i1" href="#controls${phase.number}">${phase}</a>
	#end
#end
<div>DualRate Expo</div>
#foreach ($phase in $model.phase)
	#if ($phase.phaseType.name() != "Unused")
		<a class="i1" href="#drExpo${phase.number}">${phase}</a>
	#end
#end
<div>Kanal 1 Kurve</div>
#foreach ($phase in $model.phase)
	#if ($phase.phaseType.name() != "Unused")
		<a class="i1" href="#channel1Curve${phase.number}">${phase}</a>
	#end
#end
<a href="#controlSwitches">Geberschalter</a>
#if ($model.logicalSwitch != $null)
	<a href="#logicalSwitches">logische Schalter</a>
#end
<a href="#phaseSettings">Phaseneinstellungen</a>
<a href="#phaseAssignments">Phasenzuweisung</a>
#if ($model.modelType.name() == "Winged")
	<a href="#phaseTrim">Phasentrimm</a>
#end
<a href="#nonDelayedChannels">unverzögerte Kanäle</a>
<a href="#timersGeneral">Uhren (allgemein)</a>
<a href="#phaseTimer">Flugphasenuhren</a>
#if ($model.modelType.name() == "Winged")
	<div>Flächenmischer</div>
	#foreach ($phase in $model.phase)
		#if ($phase.phaseType.name() != "Unused")
			<a class="i1" href="#wingMix${phase.number}">${phase}</a>
		#end
	#end
#end
#if ($model.modelType.name() == "Helicopter")
	<div>Helikoptermix</div>
	#foreach ($phase in $model.phase)
		#if ($phase.phaseType.name() != "Unused")
			<a class="i1" href="#helicopterMix${phase.number}">${phase}</a>
		#end
	#end
#end
<a href="#linearMixer">Linearmischer</a>
<a href="#curveMixer">Kurvenmischer</a>
<a href="#mixerActive">MIX aktiv / Phase</a>
<a href="#mixOnlyChannel">Nur MIX Kanal</a>
<a href="#dualMixer">Kreuzmischer</a>
#if ($model.modelType.name() == "Helicopter")
	<a href="#swashplateMixer">Taumelscheibenmischer</a>
#end
<a href="#failSafe">Fail Safe</a>
<a href="#trainerPupil">Lehrer/Schüler</a>
<a href="#outputChannel">Senderausgang</a>
<a href="#profiTrim">Profitrimm</a>
<a href="#trimMemory">Trimmspeicher</a>
<a href="#telemetry">Telemetrie</a>		
<a href="#channelSequencer">Kanal Sequenzer</a>
<a href="#multiChannel">Multikanal</a>
<a href="#ringLimiter">Ringbegrenzer</a>
#if ($model.transmitterType.name() != "mx20")
	<a href="#mp3Player">MP3-Player</a>
#end
<a href="#switches">Schalter-/Geberzuordnungen</a>