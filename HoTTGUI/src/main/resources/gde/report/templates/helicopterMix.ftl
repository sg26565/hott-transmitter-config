<#if helicopterModel??>
	<#list helicopterModel.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<table>
				<caption><a name="helicopterMix${phase.number}"/>Helikoptermix - ${phase?string}</caption>
				
				<tbody>
					<@heliCurve "Pitchkurve" phase.helicopterMixer.pitchCurve/>

					<#if phase.phaseType.name() == "Autorotation">
						<tr class="<@d/>">
							<th align="right">Gasposition AR</th>
							<td align="left" colspan="5">${helicopterModel.autorotationThrottlePosition}</td>
						</tr>
						<tr class="<@d/>">
							<th align="right">Heckrotoroffset AR</th>
							<td align="left" colspan="5">${helicopterModel.autorotationTailPosition}</td>
						</tr>
					<#else>
						<@heliCurve "Gaskurve (K1 &rarr; Gas)" phase.helicopterMixer.throttleCurve/>
						<@heliCurve "Heckrotorkurve (K1 &rarr; Heck)" phase.helicopterMixer.tailCurve/>
					</#if>

					<tr class="<@d/>">
						<th align="right">Heckrotor &rarr; Gas</th>
						<td align="left" colspan="5">${phase.helicopterMixer.tail2ThrottleMix}%</td>
					</tr>	
					<tr class="<@d/>">
						<th align="right">Roll &rarr; Gas</th>
						<td align="left" colspan="5">${phase.helicopterMixer.roll2ThrottleMix}%</td>
					</tr>	
					<tr class="<@d/>">
						<th align="right">Roll &rarr; Heckrotor</th>
						<td align="left" colspan="5">${phase.helicopterMixer.roll2TailMix}%</td>
					</tr>	
					<tr class="<@d/>">
						<th align="right">Nick &rarr; Gas</th>
						<td align="left" colspan="5">${phase.helicopterMixer.nick2ThrottleMix}%</td>
					</tr>	
					<tr class="<@d/>">
						<th align="right">Nick &rarr; Heckrotor</th>
						<td align="left" colspan="5">${phase.helicopterMixer.nick2TailMix}%</td>
					</tr>	
					<tr class="<@d/>">
						<th align="right">Kreiselausblendung</th>
						<td align="left" colspan="5">${phase.gyroSuppression}%</td>
					</tr>	
					<tr class="<@d/>">
						<th align="right">Taumelscheibendrehung</th>
						<td align="left" colspan="5">${phase.helicopterMixer.swashplateRotation}&deg;</td>
					</tr>	
					<tr class="<@d/>">
						<th align="right">Taumelscheibenbegrenzung</th>
						<td align="left" colspan="5"><#if phase.helicopterMixer.swashplateLimit == 150>aus<#else>${phase.helicopterMixer.swashplateLimit}%</#if></td>
					</tr>
				</tbody>
			</table>
		</#if>
	</#list>
</#if>