<table>
	<caption>Knüppeltrimmung</caption>

	<thead>
		<tr>
			<th/>
			<#if wingedModel??>
				<th align="center">Kanal 1</th>
				<th align="center">Querruder</th>
				<th align="center">Höhenruder</th>
				<th align="center">Seitenruder</th>
			<#else>
				<th align="center">Pitch/Gas</th>
				<th align="center">Roll</th>
				<th align="center">Nick</th>
				<th align="center">Heckrotor</th>
			</#if>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.number != "-1" && phase.phaseType.name() != "Unused">
				<tr class="<@d/>">
					<td align="center">${phase?string}</td>
					<td align="center">${model.throttleSettings.throttleTrim}%</td>
					<#if wingedModel??>
						<td align="center">${phase.wingTrim.aileronStickTrim}%</td>
						<td align="center">${phase.wingTrim.elevatorStickTrim}%</td>
						<td align="center">${phase.wingTrim.rudderStickTrim}%</td>
					<#else>
						<td align="center">${phase.helicopterTrim.rollStickTrim}%</td>
						<td align="center">${phase.helicopterTrim.nickStickTrim}%</td>
						<td align="center">${phase.helicopterTrim.tailStickTrim}%</td>
					</#if>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>