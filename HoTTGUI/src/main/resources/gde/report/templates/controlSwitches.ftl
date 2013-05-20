<table>
	<caption>Geberschalter</caption>
	
	<thead>
		<tr>
			<th align="center">Nummer</th>
			<th align="center">Geber</th>
			<th align="center">Position</th>
			<th align="center">Richtung</th>
			<th align="center">Schalter</th>
		</tr>
	</thead>

	<@reset/>
	
	<tbody>
		<#list model.controlSwitch as sw>
			<tr class="<@d/>">
				<td align="center">G${sw_index+1}</td>
				<td align="center"><#if sw.assignment.name() == "Unassigned">---<#else>${sw.assignment}</#if></td>
				<td align="center">${sw.position}%</td>
				<td align="center"><#if sw.direction==0>&rarr;<#else>&larr;</#if></td>
				<td align="center"><@switch sw.combineSwitch/></td>
			</tr>
		</#list>
	</tbody>
</table>