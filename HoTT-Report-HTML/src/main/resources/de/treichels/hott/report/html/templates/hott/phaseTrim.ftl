<#if model.modelType.name() == "Winged" && model.isMenuEnabled("PhaseTrim")>
	<table>
		<caption><a name="phaseTrim"></a>Phasentrimm</caption>
		
		<thead>
			<tr>
				<th align="center">Phase</th>
				<th align="center">HR</th>
				<th align="center">QR</th>
				<th align="center">QR2</th>
				<th align="center">WK</th>
				<th align="center">WK2</th>
			</tr>
		</thead>
		
		<@reset/>
		
		<tbody>
			<#list wingedModel.phase as phase>
				<#if phase.number != -1 && phase.phaseType.name() != "Unused">
					<tr class="<@d/>">
						<td align="center">${phase.toString()}</td>
						<td align="center">${phase.wingTrim.elevatorPhaseTrim}%</td>
						<td align="center">${phase.wingTrim.aileronPhaseTrim[0]}%</td>
						<td align="center">${phase.wingTrim.aileronPhaseTrim[1]}%</td>
						<td align="center">${phase.wingTrim.flapPhaseTrim[0]}%</td>
						<td align="center">${phase.wingTrim.flapPhaseTrim[1]}%</td>
					</tr>
				</#if>
			</#list>
		</tbody>
	</table>
</#if>
