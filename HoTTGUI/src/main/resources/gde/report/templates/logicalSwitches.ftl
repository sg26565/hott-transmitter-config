<#if model.logicalSwitch??>
	<#assign show=false/>
	<#list model.logicalSwitch as sw>
		<#if sw.switch[0].assignment.name() != "Unassigned" && sw.switch[1].assignment.name() != "Unassigned">
			<#assign show=true/>
			<#break>
		</#if>
	</#list>

	<table class="<@u show/>">
		<caption>logische Schalter</caption>
		
		<thead>
			<tr>
				<th align="center">Nummer</th>
				<th align="center">Schalter 1</th>
				<th align="center">Funktion</th>
				<th align="center">Schalter 2</th>
			</tr>
		</thead>

		<@reset/>
		
		<tbody>
			<#list model.logicalSwitch as sw>
				<tr class="<@d/> <@u sw.switch[0].assignment.name() != "Unassigned" && sw.switch[1].assignment.name() != "Unassigned"/>">
					<td align="center">L${sw_index+1}</td>
					<td align="center"><@switch sw.switch[0]/></td>
					<td align="center">${sw.mode}</td>
					<td align="center"><@switch sw.switch[1]/></td>
				</tr>
			</#list>
		</tbody>
	</table>
</#if>