<table>
	<caption>Uhren (allgemein)</caption>
	
	<thead>
		<tr>
			<th/>
			<th align="center">Uhr</th>
			<th align="center">Timer</th>
			<th align="center">Alarm</th>
			<th align="center">Schalter</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">Modellzeit</th>
			<td colspan="3" class="d0"/>
			<td align="center"><@switch model.getSwitch("Clock_ModelTime")/></td>
		</tr>
		<#list 4..5 as index>
			<tr class="<@d/>">
				<th align="right">${model.clock[index].type}</th>
				<td align="center">${model.clock[index].function}</td>
				<td align="center">${model.clock[index].minutes}:${model.clock[index].seconds?string("00")}</td>
				<td align="center">${model.clock[index].alarm}s</td>
				<td align="center"><@switch model.clock[index].switch/></td>
			</tr>
		</#list>
	</tbody>
</table>