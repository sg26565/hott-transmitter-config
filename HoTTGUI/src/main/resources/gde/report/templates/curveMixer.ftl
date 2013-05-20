<table>
	<caption>Kurvenmischer</caption>
	
	<thead>
		<tr>
			<th align="center">Mixer</th>
			<th align="center">Input</th>
			<th align="center">von &rarr; zu</th>
			<th align="center">Schalter</th>
			<th align="center">Kurve</th>
			<th align="center">Punkt</th>				
			<th align="center">aktiv</th>
			<th align="center">Eingang</th>
			<th align="center">Ausgang</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.freeMixer as mixer>
			<#if mixer_index &gt;= 8>
				<#list mixer.curve.point as point>
					<tr class="<@d/>">
						<#if point.number==0>
							<td align="center">KurvenMix ${mixer.number?number+1}</td>
							<td align="center">${mixer.inputType}</td>
							<td align="center"><#if mixer.fromChannel.number?number == 17>S<#else>${mixer.fromChannel.number}</#if> &rarr; ${mixer.toChannel.number}</td>
							<td align="center"><@switch mixer.switch/></td>
							<td align="center">${mixer.curve.smoothing?string("an","aus")}</td>
						<#else>
							<td colspan="5"/>
						</#if>
						<td align="center">${point.number?number+1}</td>
						<td align="center">${point.enabled?string("ja","nein")}</td>
						<#if point.enabled>
							<td align="center">${point.position}%</td>
							<td align="center">${point.value}%</td>
						<#else>
							<td align="center" colspan="2">---</td>
						</#if>
					</tr>
				</#list>
			</#if>
		</#list>
	</tbody>		
</table>