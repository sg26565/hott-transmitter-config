<#assign n=0 />
<#list model.switch?sort_by("assignment") as sw>
	<#if sw?? && sw.assignment?? && sw.assignment.name() != "Unassigned">
		<#if n % 40 == 0>
			<#if n &gt; 0>
				</tbody>
			</table>
			</#if>
			
			<table>
				<caption><#if n == 0><a name="switches"/></#if> Schalter-/Geberzuordnungen<#if n &gt; 0> (Fortsetzung)</#if></caption>
				
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
		</#if>
		<#assign n=n+1/>
		<tr class="<@d/>">
			<td align="center">${sw.name}</td>
			<td align="center">${sw.assignment.name()}</td>
			<td align="center"><@switch sw/></td>
			<td align="center">${sw.type}</td>
		</tr>
	</#if>
</#list>
<#if n &gt; 0>
	</tbody>
</table>
</#if>