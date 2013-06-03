<table>
	<caption>DualRate Expo</caption>
	
	<thead>
		<tr>
			<th/>
			<th align="center" colspan="2">Dual Rate</th>
			<th align="center" colspan="2">Expo</th>
			<th colspan="2">
		</tr>
		<tr>
			<th/>
			<th align="center">Schalter</th>
			<th align="center">Wert</th>
			<th align="center">Schalter</th>
			<th align="center">Wert</th>
			<th colspan="2"/>
		</tr>
	</thead>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<tr>
					<th class="d2" colspan="7">${phase?string}</th>
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
						<#if phase.expo[i].switch.assignment.name() != "Unassigned">
							<#list 0..1 as j>
								<td align="center">
									<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="100" height="125">
										<rect x="0" y="0" width="100" height="125" fill="none" stroke="darkGrey" stroke-width="2"/>
										<line x1="0" y1="12.5" x2="100" y2="12.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
										<line x1="0" y1="62.5" x2="100" y2="62.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
										<line x1="0" y1="112.5" x2="100" y2="112.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
										<line x1="50" y1="0" x2="50" y2="125" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
										<path d="M 0,${((125.0+phase.dualRate[i].values[j])/2.0)?c} Q 12.5,${(62.5+phase.dualRate[i].values[j]*0.375*(1.0-phase.expo[i].values[j]/100.0))?c} 50,62.5 T 100,${((125-phase.dualRate[i].values[j])/2)?c}" stroke="black" stroke-width="2" fill="none"/>	
									</svg>
								</td>
							</#list>
						<#else>
							<td colspan="2" align="center">
								<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="100" height="125">
									<rect x="0" y="0" width="100" height="125" fill="none" stroke="darkGrey" stroke-width="2"/>
									<line x1="0" y1="12.5" x2="100" y2="12.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
									<line x1="0" y1="62.5" x2="100" y2="62.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
									<line x1="0" y1="112.5" x2="100" y2="112.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
									<line x1="50" y1="0" x2="50" y2="125" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
									<path d="M 0,${((125.0+phase.dualRate[i].values[0])/2.0)?c} Q 12.5,${(62.5+phase.dualRate[i].values[0]*0.375*(1.0-phase.expo[i].values[0]/100.0))?c} 50,62.5 T 100,${((125-phase.dualRate[i].values[0])/2)?c}" stroke="black" stroke-width="2" fill="none"/>	
								</svg>
							</td>
						</#if>
					</tr>
				</#list>
			</#if>
		</#list>
	</tbody>
</table>
