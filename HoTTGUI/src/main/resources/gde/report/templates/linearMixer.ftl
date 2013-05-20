<table>
	<caption>Linearmischer</caption>
	
	<thead>
		<tr>
			<th align="center">Mixer</th>
			<th align="center">Input</th>
			<th align="center">von &rarr; zu</th>
			<th align="center">Schalter</th>
			<th align="center">Weg -</th>
			<th align="center">Weg +</th>
			<th align="center">Offset</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>	
		<#list model.freeMixer as mixer>
			<#if mixer_index < 8>
				<tr class="<@d/>">
					<td align="center">LinearMix ${mixer.number?number+1}</td>
					<td align="center">${mixer.inputType}</td>
					<td align="center"><#if mixer.fromChannel.virtual>S<#else>${mixer.fromChannel.number}</#if> &rarr; ${mixer.toChannel.number}</td>
					<td align="center"><@switch mixer.switch/></td>
					<td align="center">${mixer.travelLow}%</td>
					<td align="center">${mixer.travelHigh}%</td>
					<td align="center">${mixer.offset}%</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>