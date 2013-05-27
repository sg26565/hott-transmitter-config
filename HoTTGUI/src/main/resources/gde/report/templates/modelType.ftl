<table>
	<#if helicopterModel??>
		<caption>Helicoptertyp</caption>
	
		<@reset/>
		
		<tbody>
			<tr class="<@d/>">
				<th align="right">Taumelscheibe</th>
				<td align="left">${helicopterModel.swashplateType}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Taumelscheibenlinearisierung</th>
				<td align="left">${helicopterModel.swashplateLinearization?string("ja","nein")}</td>
			</tr>							
			<tr class="<@d/>">
				<th align="right">Rotor Drehrichtung</th>
				<td align="left">${helicopterModel.rotorDirection}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Pitch Minimum</th>
				<td align="left">${helicopterModel.pitchMin}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Expo Gaslimit</th>
				<td align="left">${helicopterModel.expoThrottleLimit}%</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Gaslimit Warnung</th>
				<td align="left">${helicopterModel.throttleLimitWarning}%</td>
			</tr>
		</tbody>
	<#else>
		<caption>Modelltyp</caption>

		<@reset/>
		
		<tbody>
			<tr class="<@d/>">
				<th align="right">Motor an K1</th>
				<td align="left" colspan="2">${wingedModel.motorOnC1Type}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Leitwerk</th>
				<td align="left" colspan="2">${wingedModel.tailType}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Querruder/Wölbklappen</th>
				<td align="left" colspan="2">${wingedModel.aileronFlapType}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">Bremse</th>
				<td align="left"><i>Offset:</i> ${wingedModel.brakeOffset}</td>
				<td align="left"><i>Eingang:</i> ${wingedModel.brakeInputChannel.number?number+1}</td>
			</tr>
		</tbody>
	</#if>
</table>
