<#if model.modelType.name() == "Helicopter" && model.isMenuEnabled("WingMixers") && helicopterModel.swashplateType.name() != "OneServo">
	<table>
		<caption><a name="swashplateMixer"></a>Taumelscheibenmischer</caption>
		
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
