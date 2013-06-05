<a name="telemetry"/>
<table>
	<caption>Telemetrie</caption>
	
	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right" rowspan="2" valign="top">Ansage wiederholen</th>
			<td align="left">${model.telemetry.voiceDelay}s</td>
		</tr>
		<tr class="<@d/>">
			<td align="left"><@switch model.telemetry.voiceRepeat/></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">nächste Ansage</th>
			<td align="left"><@switch model.telemetry.voiceTrigger/></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Varioton</th>
			<td align="left"><@switch model.telemetry.varioTone/></td>
		</tr>
		<#list model.telemetry.selectedSensor as sensor>
			<tr class="<@d/>">
				<#if sensor_index == 0>
					<th align="right" rowspan="${model.telemetry.selectedSensor?size}" valign="top">ausgewählte Sensoren</th>
				</#if>
					<td align="left">${sensor}</td>
			</tr>
		</#list>				
		<tr class="<@d/>">
			<th align="right">aktueller Sensor</th>
			<td align="left">${model.telemetry.currentSensor}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">aktuelle Sensorseite</th>
			<td align="left">${model.telemetry.currentSensorPage}</td>
		</tr>
	</tbody>
</table>
