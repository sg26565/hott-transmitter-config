<#if model.isMenuEnabled("FreeMixers")>
<#assign show=false/>
<#list model.freeMixer as mixer>
	<#if mixer.fromChannel.number != 0 && mixer.toChannel.number != 0 && mixer_index < 8>
		<#assign show=true/>
		<#break>
	</#if>
</#list>
<table class="<@u show/>">
	<caption><a name="linearMixer"></a>Linearmischer</caption>
	
	<thead>
		<tr>
			<th align="center">Mixer</th>
			<th align="center">Input</th>
			<th align="center">von &rarr; zu</th>
			<th align="center">Schalter</th>
			<th align="center">Weg -</th>
			<th align="center">Weg +</th>
			<th align="center">Offset</th>
			<th></th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>	
		<#list model.freeMixer as mixer>
			<#if mixer_index < 8>
				<tr class="<@d/> <@u mixer.fromChannel.number != 0 && mixer.toChannel.number != 0/>">
					<td align="center">LinearMix ${mixer.number?number+1}</td>
					<td align="center">${mixer.inputType}</td>
					<td align="center"><#if mixer.fromChannel.virtual>S<#else>${mixer.fromChannel.number}</#if> &rarr; ${mixer.toChannel.number}</td>
					<td align="center"><@switch mixer.switch/></td>
					<td align="center">${mixer.travelLow}%</td>
					<td align="center">${mixer.travelHigh}%</td>
					<td align="center">${mixer.XOffset}%</td>
					<td align="center"><img src="${png.getImageSource(mixer.curve,0.25,false)}" alt="curve"/></td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
</#if>
