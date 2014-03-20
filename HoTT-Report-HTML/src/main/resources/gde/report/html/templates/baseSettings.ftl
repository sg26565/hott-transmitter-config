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
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.module"]}</th>
			<td align="left" colspan="3">${model.module.type}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">${m["BaseModel.dscOutputType"]}</th>
			<td align="left" colspan="3">${model.dscOutputType}</td>
		</tr>
		<#if helicopterModel??>
			<tr class="<@d/>">
				<th align="right">${m["HelicopterModel.autorotationSwitch"]}</th>
				<td align="left" colspan="3"><@switch model.getSwitch("Autorotation")/></td>
			</tr>	
			<tr class="<@d/>">
				<th align="right">${m["HelicopterModel.autorotationC1TriggerPosition"]}</th>
				<td align="left"><i>${m["HelicopterModel.position"]}:</i> ${helicopterModel.autorotationC1TriggerPosition}%</td>
				<td align="left" colspan="2"><i>${m["BaseModel.switch"]}:</i> <@switch model.getSwitch("AutorotationC1")/></td>
			</tr>
		</#if>
		<tr class="<@d/>">
			<th align="right" valign="top">${m["BaseModel.throttleSettings"]}</th>
			<td align="left"><i>${m["ThrottleCutOf.position"]}:</i> ${model.throttleSettings.throttleCutOf.position}%</td>
			<td align="left"><i>${m["ThrottleCutOf.threshold"]}:</i> ${model.throttleSettings.throttleCutOf.threshold}%</td>
			<td align="left"><i>${m["ThrottleCutOf.switch"]}:</i> <@switch model.throttleSettings.throttleCutOf.switch/></td>							
		</tr>
		<#if model.transmitterType.name() != "mx20">
			<#if helicopterModel??>
				<tr class="<@d/>">
					<th align="right">${m["HelicopterModel.markerSwitch"]}</th>
					<td align="left" colspan="3"><@switch model.getSwitch("MarkerKey")/></td>
				</tr>
			</#if>
			<tr class="<@d/>">
				<th align="right">${m["BaseModel.powerWarning"]}</th>
				<td align="left" colspan="3"><@switch model.getSwitch("PowerWarning")/></td>
			</tr>
			<tr class="<@d/>">
				<th align="right">${m["BaseModel.autoTrimSwitch"]}</th>
				<td align="left" colspan="3"><@switch model.getSwitch("AutoTrim")/></td>
			</tr>
			<tr class="<@d/>">
				<th align="right">${m["BaseModel.autoTimerReset"]}</th>
				<td align="left" colspan="3"><@yesno model.autoTimerReset/></td>
			</tr>
		</#if>
		<#if model.module.type.name() == "HoTT">
			<#list model.receiver as receiver>
				<tr class="<@d/> <@u receiver.bound/>">
					<th class="d2" colspan="4">${m["BaseModel.receiver"]} ${receiver.number?number+1}</th>
				</tr>
				<tr class="<@d/> <@u receiver.bound/>">
					<th align="right">${m["BaseModel.bound"]}</th>
					<td align="left" colspan="3"><#if receiver.bound>${model.receiverType}<#else><@yesno receiver.bound/></#if></td>
				</tr>
				<#if receiver.bound>
					<tr class="<@d/>">
						<th align="right">${m["Receiver.telemetry"]}</th>
						<td align="left" colspan="3"><@yesno receiver.telemetry/></td>
					</tr>
					<tr class="<@d/>">
						<th align="right">${m["Receiver.rfid"]}</th>
						<td align="left" colspan="3">${hex(receiver.rfid?c)}</td>
					</tr>
					<tr class="<@d/>">
						<th align="right">${m["Receiver.channelMapping"]}</th>
						<th align="center">${m["Receiver.input"]}</th>
						<th/>
						<th align="center">${m["Receiver.output"]}</th>
					</tr>
					<#list receiver.channelMapping as mapping>
					<tr class="<@d/>">
						<th/>
						<td align="center">S${mapping.inputChannel+1}<#if model.channel[mapping.inputChannel].function??> (${model.channel[mapping.inputChannel].function})</#if></td>
						<td align="center">&rarr;</td>
						<td align="center">${m["Receiver.output"]} ${mapping.outputChannel+1}</td>
					</tr>
					</#list>
				</#if>
			</#list>
		</#if>
	</tbody>
</table>