<#assign show=false/>
<#list model.ringLimiter as limit>
	<#if limit.enabled>
		<#assign show=true/>
		<#break>
	</#if>
</#list>

<a name="ringLimiter"/>
<table class="<@u show/>">
	<caption>Ringbegrenzer</caption>
	
	<thead>
		<tr>
			<th align="center" colspan="2">Eingang</th>
			<th align="center" colspan="2">Ausgang</th>
			<th align="center" rowspan="2" valign="bottom">aktiv?</th>
			<th align="center" colspan="2">Limit</th>
			<th align="center" colspan="2">Offset</th>
		</tr>
		<tr>
			<th align="center">X</th>
			<th align="center">Y</th>
			<th align="center">X</th>
			<th align="center">Y</th>				
			<th align="center">max-X</th>
			<th align="center">max-Y</th>
			<th align="center">&#x25cb;&harr;&#x25a1;-X</th>
			<th align="center">&#x25cb;&harr;&#x25a1;-Y</th>				
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.ringLimiter as limit>
			<tr class="<@d/> <@u limit.enabled/>">
				<td align="center">${limit.inputChannel[0].number?number+1}</td>
				<td align="center">${limit.inputChannel[1].number?number+1}</td>
				<td align="center">${limit.outputChannel[0].number?number+1}</td>
				<td align="center">${limit.outputChannel[1].number?number+1}</td>
				<td align="center">${limit.enabled?string("aktiv","inaktiv")}</td>
				<td align="center">${limit.limit[0]}%</td>
				<td align="center">${limit.limit[1]}%</td>
				<td align="center">${limit.offset[0]}%</td>
				<td align="center">${limit.offset[1]}%</td>
			</tr>
		</#list>
	</tbody>
</table>