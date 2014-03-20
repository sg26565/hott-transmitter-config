<table>
	<caption><a name="baseSettings"/>${m["Section.baseSettings"]}</caption>

	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.vendor"]}</th>
			<td align="left" colspan="3">${model.vendor}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.transmitterType"]}</th>
			<td align="left" colspan="3">${model.transmitterType}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.transmitterId"]}</th>
			<td align="left" colspan="3">${hex(model.transmitterId?c)}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.appVersion"]}</th>
			<td align="left" colspan="3">${model.appVersion}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.memoryVersion"]}</th>
			<td align="left" colspan="3">${model.memoryVersion}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.modelType"]}</th>
			<td align="left" colspan="3">${model.modelType}</td>
		</tr>
		<#if model.modelNumber &gt; 0>			
			<tr class="<@d/>">
				<th align="right">${m["BaseModel.modelNumber"]}</th>
				<td align="left" colspan="3">${model.modelNumber}</td>
			</tr>
		</#if>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.modelName"]}</th>
			<td align="left" colspan="3">${model.modelName}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.stickMode"]}</th>
			<td align="left" colspan="3">${model.stickMode}</td>
		</tr>
		<#if helicopterModel??>
			<tr class="<@d/>">
				<th align="right">Taumelscheibe</th>
				<td align="left" colspan="3">${helicopterModel.swashplateType}</td>
			</tr>
		<#else>
			<tr class="<@d/>">
				<th align="right">Motor an K1</th>
				<td align="left" colspan="3">${wingedModel.motorOnC1Type}</td>
			</tr>
		</#if>
		<tr class="<@d/>">
			<th align="right" valign="top">${m["BaseModel.throttleSettings"]}</th>
			<td align="left"><i>${m["ThrottleCutOf.position"]}:</i> ${model.throttleSettings.throttleCutOf.position}%</td>
			<td align="left"><i>${m["ThrottleCutOf.threshold"]}:</i> ${model.throttleSettings.throttleCutOf.threshold}%</td>
			<td align="left"><i>${m["ThrottleCutOf.switch"]}:</i> <@switch model.throttleSettings.throttleCutOf.switch/></td>							
		</tr>
		<#if helicopterModel??>
			<tr class="<@d/>">
				<th align="right">Rotor Drehrichtung</th>
				<td align="left" colspan="3">${helicopterModel.rotorDirection}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Pitch Minimum</th>
				<td align="left" colspan="3">${helicopterModel.pitchMin}</td>
			</tr>
		</#if>
		<#if wingedModel?? && model.transmitterType.name()="mx16">
			<tr class="<@d/>">
				<th align="right">Kanal 8 verzögert</th>
				<td align="left" colspan="3">${wingedModel.channel8Delay?string("ja","nein")}</td>
			</tr>
		</#if>
		<tr class="<@d/>">
			<th align="right">Gastrimm</th>
			<td align="left" colspan="3">${model.throttleSettings.throttleTrim}%</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">letzte Leerlaufposition</th>
			<td align="left" colspan="3">${model.throttleSettings.throttleLastIdlePosition}%</td>
		</tr>
		<#if wingedModel??>
			<tr class="<@d/>">
				<th align="right">Leitwerk</th>
				<td align="left" colspan="3">${wingedModel.tailType}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Querruder/Wölbklappen</th>
				<td align="left" colspan="3">${wingedModel.aileronFlapType}</td>
			</tr>
		</#if>

		<tr>
			<th colspan="4" class="d2">Uhren</th>
		</tr>
		<tr>
			<th/>
			<th align="center">Typ</th>
			<th align="center">Wert</th>
			<th align="center">Schalter</th>
		</tr>

		<@reset/>
		<#list model.clock as clock>
			<tr class="<@d/>">
				<th/>
				<td align="center">${clock.mode!""}${clock.type!""}</td>
				<td align="center">${clock.minutes}:${clock.seconds?string("00")}</td>
				<td align="center"><@switch clock.switch/></td>
			</tr>
		</#list>
		
		<tr>
			<th colspan="4" class="d2">Flugphasen</th>
		</tr>
		<tr>
			<th/>
			<th align="center">Name</th>
			<th align="center" colspan="2">Schalter</th>
		</tr>

		<@reset/>
		<#list model.phase as phase>
			<#if phase.number != "-1" && phase.phaseType.name() != "Unused">
				<tr class="<@d/>">
					<th align="right">Phase ${phase.number?number+1}</th>
					<td align="center">${phase.phaseName}</td>
					<#if phase.phaseSwitch??>
							<td align="center" colspan="2"><@switch phase.phaseSwitch/></td>
					<#else>
						<td colspan="2"/>
					</#if>
				</tr>
			</#if>
		</#list>

		<tr>
			<th colspan="4" class="d2">Empfänger</th>
		</tr>
		<tr>
			<th align="right" valign="top">Empfängerausgang</th>
			<th align="left">Eingang</th>
			<th/>
			<th align="center">Ausgang</th>
		</tr>

		<@reset/>
		<#list model.channelMapping as mapping>
			<tr class="<@d/>">
				<th/>
				<td align="center">S${mapping.inputChannel+1}<#if model.channel[mapping.inputChannel].function??> (${model.channel[mapping.inputChannel].function})</#if></td>
				<td align="center">&rarr;</td>
				<td align="center">Ausgang ${mapping.outputChannel+1}</td>
			</tr>
		</#list>

		<tr class="<@d/>">
			<th align="right">Empfänger gebunden</th>
			<td align="left" colspan="3">${model.bound?string("ja","nein")}</td>
		</tr>			
		<tr class="<@d/>">
			<th align="right">Empfänger ID</th>
			<td align="left" colspan="3">${hex(model.receiver[0].rfid?c)}</td>
		</tr>
	</tbody>
</table>