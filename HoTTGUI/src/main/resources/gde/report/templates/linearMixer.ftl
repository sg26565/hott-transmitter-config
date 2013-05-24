<table>
	<caption>Linearmischer</caption>
	
	<thead>
		<tr>
			<th align="center">Mixer</th>
			<th align="center">Input</th>
			<th align="center">von &rarr; zu</th>
			<th align="center">Schalter</th>
			<th align="center">Weg -</th>
			<th align="center">Weg +</th>
			<th align="center">Offset</th>
			<th/>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>	
		<#list model.freeMixer as mixer>
			<#if mixer_index < 8>
				<tr class="<@d/>">
					<td align="center">LinearMix ${mixer.number?number+1}</td>
					<td align="center">${mixer.inputType}</td>
					<td align="center"><#if mixer.fromChannel.virtual>S<#else>${mixer.fromChannel.number}</#if> &rarr; ${mixer.toChannel.number}</td>
					<td align="center"><@switch mixer.switch/></td>
					<td align="center">${mixer.travelLow}%</td>
					<td align="center">${mixer.travelHigh}%</td>
					<td align="center">${mixer.offset}%</td>
					<td align="center">
						<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="100" height="100" viewbox="-100 -125 200 250">
							<rect x="-100" y="-125" width="200" height="250" fill="none" stroke="darkGrey" stroke-width="2"/>
							<line x1="-100" y1="100" x2="100" y2="100" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<line x1="-100" y1="0" x2="100" y2="0" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<line x1="-100" y1="-100" x2="100" y2="-100" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<line x1="0" y1="-125" x2="0" y2="125" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<polyline points="-100,${-mixer.travelLow-mixer.offset} 100,${-mixer.travelHigh-mixer.offset}" stroke="black" stroke-width="2" fill="none"/>	
						</svg>
					</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>