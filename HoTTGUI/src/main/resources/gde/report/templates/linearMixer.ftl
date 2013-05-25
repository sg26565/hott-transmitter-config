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
						<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="100" height="125">
							<rect x="0" y="0" width="100" height="125" fill="none" stroke="darkGrey" stroke-width="2"/>
							<line x1="0" y1="12.5" x2="100" y2="12.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<line x1="0" y1="62.5" x2="100" y2="62.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<line x1="0" y1="112.5" x2="100" y2="112.5" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<line x1="50" y1="0" x2="50" y2="125" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
							<polyline points="0,${((125-mixer.travelLow-mixer.offset)/2)?c} 100,${((125-mixer.travelHigh-mixer.offset)/2)?c}" stroke="black" stroke-width="2" fill="none"/>	
						</svg>
					</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>