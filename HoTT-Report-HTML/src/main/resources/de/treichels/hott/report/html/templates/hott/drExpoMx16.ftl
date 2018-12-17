<#list model.phase as phase>
	<#if phase.number != -1 && phase.phaseType.name() != "Unused">
		<table>
			<caption><a name="drExpo${phase.number}"></a>DualRate Expo - ${phase.toString()}</caption>
			
			<thead>
				<tr>
					<th></th>
					<th align="center">Dual Rate</th>
					<th align="center">Expo</th>
					<th align="center">Schalter</th>
					<th align="center">aus</th>
					<th align="center">an</th>
				</tr>
			</thead>
			
			<tbody>
				<@reset/>

				<#list 0..2 as i>
					<tr class="<@d/>">			  	
						<th align="right">${phase.dualRateExpo[i].dualRate.function}</th>
						<#if model.phase[0].dualRateExpo[i].dualRate.switch.assignment.name() != "Unassigned">
							<td align="center">${phase.dualRateExpo[i].dualRate.values[0]}% / ${phase.dualRateExpo[i].dualRate.values[1]}%</td>
							<td align="center">${phase.dualRateExpo[i].expo.values[0]}% / ${phase.dualRateExpo[i].expo.values[1]}%</td>
						<#else>
							<td align="center">${phase.dualRateExpo[i].dualRate.values[0]}%</td>
							<td align="center">${phase.dualRateExpo[i].expo.values[0]}%</td>
						</#if>
						<td align="center"><@switch model.phase[0].dualRateExpo[i].dualRate.switch/></td>
						<#if model.phase[0].dualRateExpo[i].dualRate.switch.assignment.name() != "Unassigned">
							<#assign max=2/>
						<#else>
							<#assign max=1/>
						</#if>
						<#list 0..1 as j>
							<td align="center">
								<#if j&lt;max>
									<img src="${png.getImageSource(phase.dualRateExpo[i].getCurve()[j*3],0.5,false)}" alt="curve"/>
								</#if>
							</td>
						</#list>
					</tr>
				</#list>
			</tbody>
		</table>
	</#if>
</#list>
