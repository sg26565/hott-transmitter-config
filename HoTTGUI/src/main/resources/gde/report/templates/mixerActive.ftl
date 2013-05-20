<table>
	<caption>MIX aktiv / Phase</caption>
	
	<thead>
		<tr>
			<th align="center">Mixer</th>
			<th align="center">von &rarr; zu</th>
			<#list model.phase as phase>
				<#if phase.phaseType.name() != "Unused">
					<th align="center">Ph.${phase.number?number+1}</th>
				</#if>
			</#list>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.freeMixer as mixer>
			<tr class="<@d/>">
				<#if mixer_index < 8>
					<td align="right">LinearMix ${mixer.number?number+1}</td>
				<#else>
					<td align="right">KurvenMix ${mixer.number?number+1}</td>
				</#if>
				<td align="center"><#if mixer.fromChannel.number?number == 17>S<#else>${mixer.fromChannel.number}</#if> &rarr; ${mixer.toChannel.number}</td>
				<#list model.phase as phase>
					<#if phase.phaseType.name() != "Unused">
						<td align="center">${mixer.phaseSetting[phase_index].enabled?string("&times;","")}</td>
					</#if>
				</#list>
			</tr>
		</#list>
	</tbody>
</table>