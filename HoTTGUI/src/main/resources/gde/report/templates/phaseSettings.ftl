<table>
	<caption>Phaseneinstellungen</caption>
	
	<thead>
		<tr>
			<th align="center">Phase</th>
			<th align="center">Name</th>
			<th align="center">Uhr</th>
			<#if wingedModel??>
				<th align="center">Motor</th>
			</#if>
			<th align="center">Umschaltzeit</th>
		</tr>
	</thead>

	<@reset/>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<tr class="<@d/>">
					<td align="center">Phase ${phase.number?number+1}</td>
					<td align="center">${phase.phaseName}</td>
					<#if phase.phaseTimer??>
						<td align="center">${phase.phaseTimer.type}</td>
					<#else>
						<td align="center">---</td>
					</#if>
					<#if wingedModel??>
						<td align="center">${phase.motorOn?string("ja","nein")}</td>
					</#if>
					<td align="center">${phase.phaseSwitchTime?string("0.0")}s</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>