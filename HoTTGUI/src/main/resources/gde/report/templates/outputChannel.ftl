<table>
	<caption>Senderausgang</caption>
	
	<thead>
		<tr>
			<th align="center">Eingang</th>
			<th/>
			<th align="center">Ausgang</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.channelMapping as mapping>
			<tr class="<@d/>">
				<td align="center">S${mapping.inputChannel+1} <#if model.channel[mapping.inputChannel].function??>(${model.channel[mapping.inputChannel].function})</#if></td>
				<td align="center">&rarr;</td>
				<td align="center">Ausgang ${mapping.outputChannel+1}</td>
			</tr>
		</#list>
	</tbody>		
</table>