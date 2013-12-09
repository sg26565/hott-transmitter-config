<bookmarks>
	<bookmark href="#baseSettings" name="Grundeinstellungen Modell"/>
	<bookmark href="#modelType" name="#if ($model.modelType.name() == "Helicopter")Helikoptertyp#{else}Modelltyp#end"/>
	<bookmark href="#servos" name="Servoeinstellungen"/>		
	<bookmark href="#stickSettings" name="Knüppeleinstellungen"/>
	<bookmark href="#controls0" name="Gebereinstellungen">
		#foreach ($phase in $model.phase)
			#if ($phase.phaseType.name() != "Unused")
				<bookmark href="#controls${phase.number}" name="${phase}"/>
			#end
		#end
	</bookmark>
	<bookmark href="#drExpo0" name="DualRate Expo">
		#foreach ($phase in $model.phase)
			#if ($phase.phaseType.name() != "Unused")
				<bookmark href="#drExpo${phase.number}" name="${phase}"/>
			#end
		#end
	</bookmark>
	<bookmark href="#channel1Curve0" name="Kanal 1 Kurve">
		#foreach ($phase in $model.phase)
			#if ($phase.phaseType.name() != "Unused")
				<bookmark href="#channel1Curve${phase.number}" name="${phase}"/>
			#end
		#end
	</bookmark>
	<bookmark href="#controlSwitches" name="Geberschalter"/>
	#if ($model.logicalSwitch != $null)
		<bookmark href="#logicalSwitches" name="logische Schalter"/>
	#end
	<bookmark href="#phaseSettings" name="Phaseneinstellungen"/>
	<bookmark href="#phaseAssignments" name="Phasenzuweisung"/>
	#if ($model.modelType.name() == "Winged")
		<bookmark href="#phaseTrim" name="Phasentrimm"/>
	#end
	<bookmark href="#nonDelayedChannels" name="unverzögerte Kanäle"/>
	<bookmark href="#timersGeneral" name="Uhren (allgemein)"/>
	<bookmark href="#phaseTimer" name="Flugphasenuhren"/>
	#if ($model.modelType.name() == "Winged")
		<bookmark href="#wingMix0" name="Flächenmischer">
			#foreach ($phase in $model.phase)
				#if ($phase.phaseType.name() != "Unused")
					<bookmark href="#wingMix${phase.number}" name="${phase}"/>
				#end
			#end
		</bookmark>
	#end
	#if ($model.modelType.name() == "Helicopter")
		<bookmark href="#helicopterMix0" name="Helikoptermix">
			#foreach ($phase in $model.phase)
				#if ($phase.phaseType.name() != "Unused")
					<bookmark href="#helicopterMix${phase.number}" name="${phase}"/>
				#end
			#end
		</bookmark>
	#end
	<bookmark href="#linearMixer" name="Linearmischer"/>
	<bookmark href="#curveMixer" name="Kurvenmischer"/>
	<bookmark href="#mixerActive" name="MIX aktiv / Phase"/>
	<bookmark href="#mixOnlyChannel" name="Nur MIX Kanal"/>
	<bookmark href="#dualMixer" name="Kreuzmischer"/>
	#if ($model.modelType.name() == "Helicopter")
		<bookmark href="#swashplateMixer" name="Taumelscheibenmischer"/>
	#end
	<bookmark href="#failSafe" name="Fail Safe"/>
	<bookmark href="#trainerPupil" name="Lehrer/Schüler"/>
	<bookmark href="#outputChannel" name="Senderausgang"/>
	<bookmark href="#profiTrim" name="Profitrimm"/>
	<bookmark href="#trimMemory" name="Trimmspeicher"/>
	<bookmark href="#telemetry" name="Telemetrie"/>		
	<bookmark href="#channelSequencer" name="Kanal Sequenzer"/>
	<bookmark href="#multiChannel" name="Multikanal"/>
	<bookmark href="#ringLimiter" name="Ringbegrenzer"/>
	#if ($model.transmitterType.name() != "mx20")
		<bookmark href="#mp3Player" name="MP3-Player"/>
	#end
	<bookmark href="#switches" name="Schalter-/Geberzuordnungen"/>
</bookmarks>