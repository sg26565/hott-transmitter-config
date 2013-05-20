<table>
	<caption>Grundeinstellungen Modell</caption>
	
	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">Hersteller</th>
			<td align="left" colspan="3">${model.vendor}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Sendertyp</th>
			<td align="left" colspan="3">${model.transmitterType}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Sender ID</th>
			<td align="left" colspan="3">${hex(model.transmitterId?c)}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Firmware Version</th>
			<td align="left" colspan="3">${model.appVersion}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Datei Version</th>
			<td align="left" colspan="3">${model.memoryVersion}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Modelltyp</th>
			<td align="left" colspan="3">${model.modelType}</td>
		</tr>
		<#if model.modelNumber &gt; 0>			
			<tr class="<@d/>">
				<th align="right">Modellspeicher</th>
				<td align="left" colspan="3">${model.modelNumber}</td>
			</tr>
		</#if>
		<tr class="<@d/>">
			<th align="right">Modellname</th>
			<td align="left" colspan="3">${model.modelName}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Steueranordnung</th>
			<td align="left" colspan="3">${model.stickMode}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Modul</th>
			<td align="left" colspan="3">${model.module.type}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">DSC-Ausgang</th>
			<td align="left" colspan="3">${model.dscOutputType}</td>
		</tr>
		<#if helicopterModel??>
			<tr class="<@d/>">
				<th align="right">Autorotation</th>
				<td align="left" colspan="3"><@switch model.getSwitch("Autorotation")/></td>
			</tr>	
			<tr class="<@d/>">
				<th align="right">Autorotation K1 Position</th>
				<td align="left"><i>Position:</i> ${helicopterModel.autorotationC1TriggerPosition}</td>
				<td align="left" colspan="2"><i>Schalter:</i> <@switch model.getSwitch("AutorotationC1")/></td>
			</tr>
		</#if>
		<tr class="<@d/>">
			<th align="right" valign="top">Motor-Stopp</th>
			<td align="left"><i>Position:</i> ${model.throttleSettings.throttleCutOf.position}%</td>
			<td align="left"><i>Limit:</i> ${model.throttleSettings.throttleCutOf.threshold}%</td>
			<td align="left"><i>Schalter:</i> <@switch model.throttleSettings.throttleCutOf.switch/></td>							
		</tr>
		<#if helicopterModel??>
			<tr class="<@d/>">
				<th align="right">Markierung</th>
				<td align="left" colspan="3"><@switch model.getSwitch("MarkerKey")/></td>
			</tr>
		</#if>
		<tr class="<@d/>">
			<th align="right">Einschaltwarnung</th>
			<td align="left" colspan="3"><@switch model.getSwitch("PowerWarning")/></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Auto Trimm</th>
			<td align="left" colspan="3"><@switch model.getSwitch("AutoTrim")/></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Auto rücksetzen Uhr</th>
			<td align="left" colspan="3">${model.autoTimerReset?string("ja","nein")}</td>
		</tr>
		<#if model.module.type.name() == "HoTT">
			<#list model.receiver as receiver>
				<tr class="<@d/>">
					<th class="d2" colspan="4">Empfänger ${receiver.number?number+1}</th>
				</tr>
				<tr class="<@d/>">
					<th align="right">gebunden</th>
					<td align="left" colspan="3">${receiver.bound?string("ja","nein")}</td>
				</tr>
				<#if receiver.bound>
					<tr class="<@d/>">
						<th align="right">Telemetrie</th>
						<td align="left" colspan="3">${receiver.telemetry?string("ja","nein")}</td>
					</tr>
					<tr class="<@d/>">
						<th align="right">Empfänger ID</th>
						<td align="left" colspan="3">${hex(receiver.rfid?c)}</td>
					</tr>
					<tr class="<@d/>">
						<th align="right">Empfängerausgang</th>
						<th align="center">Eingang</th>
						<th/>
						<th align="center">Ausgang</th>
					</tr>
					<#list receiver.channelMapping as mapping>
					<tr class="<@d/>">
						<th/>
						<td align="center">S${mapping.inputChannel+1}<#if model.channel[mapping.inputChannel].function??> (${model.channel[mapping.inputChannel].function})</#if></td>
						<td align="center">&rarr;</td>
						<td align="center">Ausgang ${mapping.outputChannel+1}</td>
					</tr>
					</#list>
				</#if>
			</#list>
		</#if>
	</tbody>
</table>