<#assign show=false/>
<#list model.dualMixer as mix>
	<#if mix.channel??>
		<#assign show=true/>
		<#break>
	</#if>
</#list>
<table class="<@u show/>">
	<caption><a name="dualMixer"/>Kreuzmischer</caption>
	
	<thead>
		<tr>
			<th align="center">Mischer</th>
			<th align="center">Kanal 1</th>
			<th align="center">Kanal 2</th>
			<th align="center">Differenzierung</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.dualMixer as mix>
			<tr class="<@d/> <@u mix.channel??/>">
				<td align="center">Mischer ${mix_index+1}</td>
				<td align="center">&uarr;<#if mix.channel?size &gt; 0>${mix.channel[0].number?number+1}<#else>??</#if>&uarr;</td>
				<td align="center">&uarr;<#if mix.channel?size &gt; 0>${mix.channel[1].number?number+1}<#else>??</#if>&darr;</td>
				<td align="center">${mix.diff}%</td>
			</tr>
		</#list>
	</tbody>
</table>