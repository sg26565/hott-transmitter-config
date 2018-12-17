<table>
	<caption><a name="nonDelayedChannels"></a>unverzögerte Kanäle</caption>
	
	<thead>
		<tr>
			<th align="center">Phase</th>
			<#list model.channel as channel>
				<#if !channel.virtual>		
					<th align="center">S${channel.number?number+1}</th>
				</#if>
			</#list>
		</tr>
	</thead>

	<@reset/>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<tr class="<@d/>">
					<td align="center">${phase.toString()}</td>
					<#list model.channel as channel>
						<#if !channel.virtual>		
							<td align="center">${channel.phaseSetting[phase_index].nonDelayed?string("&times;","")}</td>
						</#if>
					</#list>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
