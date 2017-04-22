<table>
	<caption><a name="switches"/>Schalter-/Geberzuordnungen</caption>
	
	<thead>
		<tr>
			<th align="center" rowspan="2" valign="top">Funktion</th>
			<th align="center" colspan="3">Schalter/Geber</th>
		</tr>
		<tr style="font-size: 75%;">
			<th align="center">ID</th>
			<th align="center">Name</th>
			<th align="center">Typ</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.switch as sw>
			<#if sw?? && sw.assignment?? && sw.assignment.name() != "Unassigned">
				<tr class="<@d/>">
					<td align="center">${sw.name}</td>
					<td align="center">${sw.assignment.name()}</td>
					<td align="center"><@switch sw/></td>
					<td align="center">${sw.type}</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>