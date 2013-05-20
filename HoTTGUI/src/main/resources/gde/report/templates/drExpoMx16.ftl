<table>
	<caption>DualRate Expo</caption>
	
	<thead>
		<tr>
			<th/>
			<th align="center">Dual Rate</th>
			<th align="center">Expo</th>
			<th align="center">Schalter</th>
		</tr>
	</thead>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.number != "-1" && phase.phaseType.name() != "Unused">
				<tr>
					<th class="d2" colspan="4">${phase?string}</th>
				</tr>

				<@reset/>

				<#list 0..2 as i>
					<tr class="<@d/>">			  	
						<th align="right">${phase.dualRate[i].function}</th>
						<#if model.phase[0].dualRate[i].switch.assignment.name() != "Unassigned">
							<td align="center">${phase.dualRate[i].values[0]}% / ${phase.dualRate[i].values[1]}%</td>
							<td align="center">${phase.expo[i].values[0]}% / ${phase.expo[i].values[1]}%</td>
						<#else>
							<td align="center">${phase.dualRate[i].values[0]}%</td>
							<td align="center">${phase.expo[i].values[0]}%</td>
						</#if>
						<td align="center"><@switch model.phase[0].dualRate[i].switch/></td>
					</tr>
				</#list>
			</#if>
		</#list>
	</tbody>
</table>
