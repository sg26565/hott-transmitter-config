<table>
	<caption><a name="controls0"></a>Gebereinstellungen</caption>
	
	<thead>
		<tr>
			<th align="center">Eingang</th>
			<th align="center" colspan="2">Geber</th>
			<th align="center">Weg -</th>
			<th align="center">Weg +</th>
		</tr>
	</thead>

	<@reset/>

	<tbody>
		<#list model.phase[0].control as control>	
			<#if !helicopterModel?? && control.number?number &gt; 7>
				<#break>
			</#if>
			
			<tr class="<@d/>">
				<#if helicopterModel?? && control.number?number == 8>
					<td align="center">Gaslimiter</td>
				<#else>
					<td align="center">E${control.number?number+1}<#if control.function.name() != "Unknown"> (${control.function})</#if></td>
				</#if>
				<td algn="center" colspan="2"><@switch control.inputControl/></td>
				<td align="center">${control.travelLow}%</td>
				<td align="center">${control.travelHigh}%</td>
			</tr>
		</#list>
	</tbody>
</table>
