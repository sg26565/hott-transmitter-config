<#if model.isMenuEnabled("ServoAdjustment")>
<table>
	<caption><a name="servos"></a>Servoeinstellungen</caption>
	
	<thead>
		<tr>
			<th align="center">Servo</th>
			<th align="center">Umkehr</th>
			<th align="center">Mitte</th>
			<th align="center">Weg -</th>
			<th align="center">Weg +</th>
			<#if model.transmitterType.name()!="mx16" && model.transmitterType.name()!="mx12">
				<th align="center">Begrenzung -</th>
				<th align="center">Begrenzung +</th>
			</#if>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.channel as channel>
			<#if !channel.virtual>
				<tr class="<@d/>">
					<td align="center">S${channel.number?number+1}<#if channel.function.name() != "Unknown"> (${channel.function})</#if></td>
					<td align="center"><#if channel.reverse>&larr;<#else>&rarr;</#if></td>
					<td align="center">${channel.center}%</td>
					<td align="center">${channel.travelLow}%</td>
					<td align="center">${channel.travelHigh}%</td>
					<#if model.transmitterType.name()!="mx16" && model.transmitterType.name()!="mx12">
						<td align="center">${channel.limitLow}%</td>
						<td align="center">${channel.limitHigh}%</td>
					</#if>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
</#if>
