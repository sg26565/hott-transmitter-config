<table>
	<caption><a name="telemetry"/>Telemetrie</caption>
	
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
			<td align="left"><#if model.varioToneSensor??>über ${model.varioToneSensor}, </#if><@switch model.telemetry.varioTone/></td>
		</tr>
		<#list model.telemetry.selectedSensor as sensor>
			<tr class="<@d/>">
				<#if sensor_index == 0>
					<th align="right" rowspan="${model.telemetry.selectedSensor?size}" valign="top">erkannte Sensoren</th>
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
		<tr class="<@d/>">
			<th align="right">RX-Data</th>
			<#if model.telemetryDataReceiveTime == 0>
			<td align="left">Ein</td>
			<#else>
			<td align="left">${model.telemetryDataReceiveTime}</td>
			</#if>
		</tr>
		<#if model.userAlarmList??>		
		<#list model.userAlarmList as alarm>
		<tr class="<@d/>">
			<#if alarm_index == 0>
			<th align="right" rowspan="${model.userAlarmList?size}" valign="top">Alarmeinstellung</th>
			</#if>
			<td align="left">Alarm ${alarm_index+1}: <@sound alarm/></td>
		</tr>
		</#list>
		</#if>						
	</tbody>
</table>
