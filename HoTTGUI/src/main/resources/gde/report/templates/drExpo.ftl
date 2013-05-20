<table>
	<caption>DualRate Expo</caption>
	
	<thead>
		<tr>
			<th/>
			<th align="center" colspan="2">Dual Rate</th>
			<th align="center" colspan="2">Expo</th>
		</tr>
		<tr>
			<th/>
			<th align="center">Schalter</th>
			<th align="center">Wert</th>
			<th align="center">Schalter</th>
			<th align="center">Wert</th>
		</tr>
	</thead>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<tr>
					<th class="d2" colspan="5">${phase?string}</th>
				</tr>

				<@reset/>

				<#list 0..2 as i>
					<tr class="<@d/>">			  	
						<th align="right">${phase.dualRate[i].function}</th>
						<td align="center"><@switch phase.dualRate[i].switch/></td>
						<#if phase.dualRate[i].switch.assignment.name() != "Unassigned">
							<td align="center">${phase.dualRate[i].values[0]}% / ${phase.dualRate[i].values[1]}%</td>
						<#else>
							<td align="center">${phase.dualRate[i].values[0]}%</td>
						</#if>
						<td align="center"><@switch phase.expo[i].switch/></td>
						<#if phase.expo[i].switch.assignment.name() != "Unassigned">
							<td align="center">${phase.expo[i].values[0]}% / ${phase.expo[i].values[1]}%</td>
						<#else>
							<td align="center">${phase.expo[i].values[0]}%</td>
						</#if>
					</tr>
				</#list>
			</#if>
		</#list>
	</tbody>
</table>
