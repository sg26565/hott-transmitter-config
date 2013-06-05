<#if helicopterModel??>
	<#list helicopterModel.phase as phase>
		<#if phase.number != "-1" && phase.phaseType.name() != "Unused">
			<a name="helicopterMix"/>
			<table>
				<caption>Helimix - ${phase?string}</caption>
	
				<@reset/>
					
				<tbody>
					<@curve "Pitchkurve" phase.helicopterMixer.pitchCurve.point/>
					<@curve "Gaskurve (K1 -> Gas)" phase.helicopterMixer.throttleCurve.point/>
					<@curve "Heckrotorkurve (K1 -> Heck)" phase.helicopterMixer.tailCurve.point/>
					<tr class="<@d/>">
						<th align="right">Gyro</th>
						<td align="left" colspan="4">${phase.gyroGain}%</td>
					</tr>
					<#if model.transmitterType.name()="mx16">
						<tr class="<@d/>">
							<th align="right">Eingang 8</th>
							<td align="left" colspan="4">${phase.channel8Value}%</td>
						</tr>
					</#if>
					<tr class="<@d/>">
						<th align="right">Taumelscheibenlimit</th>
						<td align="left" colspan="4"><#if phase.helicopterMixer.swashplateLimit == 150>aus<#else>${phase.helicopterMixer.swashplateLimit}%</#if></td>
					</tr>
				</tbody>
			</table>
		</#if>
	</#list>
</#if>