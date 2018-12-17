<#if helicopterModel??>
	<#list helicopterModel.phase as phase>
		<#if phase.number != -1 && phase.phaseType.name() != "Unused">
			<table>
				<caption><a name="helicopterMix${phase.number}"></a>Helimix - ${phase.toString()}</caption>
	
				<@reset/>
					
				<tbody>
					<@curve "Pitchkurve" phase.helicopterMixer.pitchCurve/>
					<@curve "Gaskurve (K1 -> Gas)" phase.helicopterMixer.throttleCurve/>
					<@curve "Heckrotorkurve (K1 -> Heck)" phase.helicopterMixer.tailCurve/>
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
