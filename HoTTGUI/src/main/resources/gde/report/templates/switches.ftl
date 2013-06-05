<a name="switches"/>
<table>
	<caption>Schalter-/Geberzuordnungen</caption>
	
	<thead>
		<tr>
			<th align="center">Funktion</th>
			<th align="center">Schalter/Geber</th>
			<th align="center">Typ</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.switch as sw>
			<#if sw?? && sw.assignment?? && sw.assignment.name() != "Unassigned">
				<tr class="<@d/>">
					<td align="center">${sw.id}</td>
					<td align="center"><@switch sw/></td>
					<td align="center">${sw.type}</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
