<bookmarks>
	<bookmark href="#baseSettings" name="${m["Section.baseSettings"]}"/>
	<bookmark href="#modelType" name="<#if helicopterModel??>Helicoptertyp<#else>${m["Section.modelType"]}</#if>"/>
	<bookmark href="#servos" name="${m["Section.servos"]}"/>		
	<bookmark href="#stickSettings" name="${m["Section.stickSettings"]}"/>
	<bookmark href="#controls0" name="${m["Section.control0"]}">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#controls${phase.number}" name="${phase?string}"/>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#drExpo0" name="${m["Section.drExpo0"]}">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#drExpo${phase.number}" name="${phase?string}"/>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#channel1Curve0" name="${m["Section.channel1Curv0"]}">
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<bookmark href="#channel1Curve${phase.number}" name="${phase?string}"/>
			</#if>
		</#list>
	</bookmark>
	<bookmark href="#controlSwitches" name="${m["Section.controlSwitches"]}"/>
	<#if model.logicalSwitch??>
		<bookmark href="#logicalSwitches" name="${m["Section.logiacalSwitches"]}"/>
	</#if>
	<bookmark href="#phaseSettings" name="${m["Section.phaseSettings"]}"/>
	<bookmark href="#phaseAssignments" name="${m["Section.phaseAssignments"]}"/>
	<#if wingedModel??>
		<bookmark href="#phaseTrim" name="${m["Section.phaseTrim"]}"/>
	</#if>
	<bookmark href="#nonDelayedChannels" name="${m["Section.nonDelayedChannels"]}"/>
	<bookmark href="#timersGeneral" name="${m["Section.timersGeneral"]}"/>
	<bookmark href="#phaseTimer" name="${m["Section.phaseTime"]}"/>
	<#if wingedModel??>
		<bookmark href="#wingMix0" name="${m["Section.wingMix0"]}">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#wingMix${phase.number}" name="${phase?string}"/>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<#if helicopterModel??>
		<bookmark href="#helicopterMix0" name="${m["Section.helicopterMix0"]}">
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<bookmark href="#helicopterMix${phase.number}" name="${phase?string}"/>
				</#if>
			</#list>
		</bookmark>
	</#if>
	<bookmark href="#linearMixer" name="${m["Section.linearMixer"]}"/>
	<bookmark href="#curveMixer" name="${m["Section.curveMixer"]}"/>
	<bookmark href="#mixerActive" name="${m["Section.mixerActive"]}"/>
	<bookmark href="#mixOnlyChannel" name="${m["Section.mixOnlyChannel"]}"/>
	<bookmark href="#dualMixer" name="${m["Section.dualMixer"]}"/>
	<#if helicopterModel??>
		<bookmark href="#swashplateMixer" name="${m["Section.swashplateMixer"]}"/>
	</#if>
	<bookmark href="#failSafe" name="${m["Section.failSafe"]}"/>
	<bookmark href="#trainerPupil" name="${m["Section.trainerPupil"]}"/>
	<bookmark href="#outputChannel" name="${m["Section.outputChannel"]}"/>
	<bookmark href="#profiTrim" name="${m["Section.profiTrim"]}"/>
	<bookmark href="#trimMemory" name="${m["Section.trimMemory"]}"/>
	<bookmark href="#telemetry" name="${m["Section.telemetry"]}"/>		
	<bookmark href="#channelSequencer" name="${m["Section.channelSequencer"]}"/>
	<bookmark href="#multiChannel" name="${m["Section.multichannel"]}"/>
	<bookmark href="#ringLimiter" name="${m["Section.ringLimiter"]}"/>
	<#if model.transmitterType.name() != "mx20">
		<bookmark href="#mp3Player" name="${m["Section.mp3Player"]}"/>
	</#if>
	<bookmark href="#switches" name="switches"/>
</bookmarks>