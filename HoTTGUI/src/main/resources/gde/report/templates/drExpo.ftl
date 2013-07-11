<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<table>
			<caption><a name="drExpo${phase.number}"/>DualRate Expo - ${phase?string}</caption>
			
			<thead>
				<tr>
					<th/>
					<th align="center" colspan="2">Dual Rate</th>
					<th align="center" colspan="2">Expo</th>
					<th align="center" style="font-size: small;">DR aus</td>						
					<th align="center" style="font-size: small;">DR aus</th>						
					<th align="center" style="font-size: small;">DR an</th>						
					<th align="center" style="font-size: small;">DR an</th>						
				</tr>
				<tr style="font-size: small;">
					<th/>
					<th align="center">Schalter</th>
					<th align="center">Wert</th>
					<th align="center">Schalter</th>
					<th align="center">Wert</th>
					<th align="center">Expo aus</th>
					<th align="center">Expo an</th>
					<th align="center">Expo aus</th>
					<th align="center">Expo an</th>
				</tr>
			</thead>
			
			<tbody>
				<@reset/>

				<#list 0..2 as i>
					<tr class="<@d/>">			  	
						<th align="right">${phase.dualRateExpo[i].dualRate.function}</th>
						<td align="center"><@switch phase.dualRateExpo[i].dualRate.switch/></td>
						<#if phase.dualRateExpo[i].dualRate.switch.assignment.name() != "Unassigned">
							<#assign max_dr=2/>
							<td align="center">${phase.dualRateExpo[i].dualRate.values[0]}% / ${phase.dualRateExpo[i].dualRate.values[1]}%</td>
						<#else>
							<#assign max_dr=1/>
							<td align="center">${phase.dualRateExpo[i].dualRate.values[0]}%</td>
						</#if>
						<td align="center"><@switch phase.dualRateExpo[i].expo.switch/></td>
						<#if phase.dualRateExpo[i].expo.switch.assignment.name() != "Unassigned">
							<#assign max_expo=2/>
							<td align="center">${phase.dualRateExpo[i].expo.values[0]}% / ${phase.dualRateExpo[i].expo.values[1]}%</td>
						<#else>
							<#assign max_expo=1/>
							<td align="center">${phase.dualRateExpo[i].expo.values[0]}%</td>
						</#if>
						
						<#list 0..1 as dr>
							<#list 0..1 as expo>
								<td align="center">
									<#if dr&lt;max_dr && expo&lt;max_expo>
										<img src="${phase.dualRateExpo[i].getCurve()[dr*2+expo].getImageSource(0.5,false)}"/>
									</#if>
								</td>
							</#list>
						</#list>
					</tr>
				</#list>
			</tbody>
		</table>
	</#if>
</#list>