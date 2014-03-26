<#if helicopterModel??>
	<#if helicopterModel.swashplateType.name() == "OneServo">
		<#assign show=false/>
	<#else>
		<#assign show=true/>			
	</#if>
	<table class="<@u show/>">
		<caption><a name="swashplateMixer"/>Taumelscheibenmischer</caption>
		
		<@reset/>
		
		<tbody>
			<tr class="<@d/>">
				<th align="right">Pitch</th>
				<td align="center">${helicopterModel.swashplateMix.pitchMix}%</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Roll</th>
				<td align="center">${helicopterModel.swashplateMix.rollMix}%</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Nick</th>
				<td align="center">${helicopterModel.swashplateMix.nickMix}%</td>
			</tr>
		</tbody>
	</table>
</#if>