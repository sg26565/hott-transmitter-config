<a name="trimMemory"/>
<table>
	<caption>Trimmspeicher</caption>
	
	<thead>
		<tr>
			<th/>
			<#if wingedModel??>
				<th align="center" colspan="2">Kanal 1</th>
				<th align="center" colspan="2">Querruder</th>
				<th align="center" colspan="2">Höhenruder</th>
				<th align="center" colspan="2">Seitenruder</th>
			<#else>
				<th align="center" colspan="2">Pitch/Gas</th>
				<th align="center" colspan="2">Roll</th>
				<th align="center" colspan="2">Nick</th>
				<th align="center" colspan="2">Heckrotor</th>
			</#if>
		</tr>
		<tr>
			<th/>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<tr class="<@d/>">
					<td align="center">${phase?string}</td>					
					<#if wingedModel??>
						<td align="center">${model.throttleSettings.throttleTrim}%</td>
						<td align="center">${phase.control[0].trim}%</td>
						<td align="center">${phase.wingTrim.aileronStickTrim}%</td>
						<td align="center">${phase.control[1].trim}%</td>
						<td align="center">${phase.wingTrim.elevatorStickTrim}%</td>
						<td align="center">${phase.control[2].trim}%</td>
						<td align="center">${phase.wingTrim.rudderStickTrim}%</td>
						<td align="center">${phase.control[3].trim}%</td>
					<#else>
						<td align="center">${model.throttleSettings.throttleTrim}%</td>
						<td align="center">${phase.control[0].trim}%</td>
						<td align="center">${phase.helicopterTrim.rollStickTrim}%</td>
						<td align="center">${phase.control[1].trim}%</td>
						<td align="center">${phase.helicopterTrim.nickStickTrim}%</td>
						<td align="center">${phase.control[2].trim}%</td>
						<td align="center">${phase.helicopterTrim.tailStickTrim}%</td>
						<td align="center">${phase.control[3].trim}%</td>
					</#if>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>