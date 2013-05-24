<table>
	<caption>Kurvenmischer</caption>
	
	<thead>
		<tr class="d0">
			<th align="center">Mixer</th>
			<th align="center">Input</th>
			<th align="center">von &rarr; zu</th>
			<th align="center">Schalter</th>
			<th align="center">Kurve</th>
			<th align="center">Punkt</th>				
			<th align="center">aktiv</th>
			<th align="center">Eingang</th>
			<th align="center">Ausgang</th>
			<th/>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.freeMixer as mixer>
			<#if mixer_index &gt;= 8>
				<#list mixer.curve.point as point>
					<tr class="<@d/>">
						<#if point_index==0>
							<td rowspan="${mixer.curve.point?size}" align="center" valign="top">KurvenMix ${mixer.number?number+1}</td>
							<td rowspan="${mixer.curve.point?size}" align="center" valign="top">${mixer.inputType}</td>
							<td rowspan="${mixer.curve.point?size}" align="center" valign="top"><#if mixer.fromChannel.number?number == 17>S<#else>${mixer.fromChannel.number}</#if> &rarr; ${mixer.toChannel.number}</td>
							<td rowspan="${mixer.curve.point?size}" align="center" valign="top"><@switch mixer.switch/></td>
							<td rowspan="${mixer.curve.point?size}" align="center" valign="top">${mixer.curve.smoothing?string("an","aus")}</td>
						</#if>
						<td align="center">${point.number?number+1}</td>
						<td align="center">${point.enabled?string("ja","nein")}</td>
						<#if point.enabled>
							<td align="center">${point.position}%</td>
							<td align="center">${point.value}%</td>
						<#else>
							<td align="center" colspan="2">---</td>
						</#if>
						<#if point_index==0>
							<td rowspan="${mixer.curve.point?size}" align="center"><@svg 200 200 mixer.curve.point/></td>									
						</#if>						
					</tr>
				</#list>
			</#if>
		</#list>
	</tbody>		
</table>