<table>
	<caption><a name="phaseAssignments"/>Phasenzuweisung</caption>
	
	<thead>
		<tr>
			<th/>
			<th align="center">Schalter</th>
			<th align="center">Zuweisung</th>
		</tr>
	</thead>

	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">Prioritätsschalter A</th>
			<td align="center"><@switch model.phaseAssignment.priorityASwitch/></td>
			<td align="center">${model.phaseAssignment.assignment[1]?string}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Prioritätsschalter B</th>
			<td align="center"><@switch model.phaseAssignment.priorityBSwitch/></td>
			<td align="center">${model.phaseAssignment.assignment[2]?string}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Kombinationsschalter C</th>
			<td align="center"><@switch model.phaseAssignment.combiCSwitch/></td>
			<td class="d0"/>
		</tr>
		<tr class="<@d/>">
			<th align="right">Kombinationsschalter D</th>
			<td align="center"><@switch model.phaseAssignment.combiDSwitch/></td>
			<td class="d0"/>
		</tr>
		<tr class="<@d/>">
			<th align="right">Kombinationsschalter E</th>
			<td align="center"><@switch model.phaseAssignment.combiESwitch/></td>
			<td class="d0"/>
		</tr>
		<tr class="<@d/>">
			<th align="right">Kombinationsschalter F</th>
			<td align="center"><@switch model.phaseAssignment.combiFSwitch/></td>
			<td class="d0"/>
		</tr>
	</tbody>
</table>

<table>
	<caption>Kombinationsphasenzuweisungen</caption>
	
	<thead>
		<tr>
			<th align="center">C</th>
			<th align="center">D</th>
			<th align="center">E</th>
			<th align="center">F</th>
			<th align="center">Phase</th>
		</tr>
	</thead>

	<@reset/>
	
	<tbody>
		<#list model.phaseAssignment.assignment as assignment>
			<#if (assignment_index > 1)>
				<tr class="<@d/>">
					<td align="center">${((assignment_index-2)/8%2==1)?string("an","aus")}</td>
					<td align="center">${((assignment_index-2)/4%2==1)?string("an","aus")}</td>
					<td align="center">${((assignment_index-2)/2%2==1)?string("an","aus")}</td>
					<td align="center">${((assignment_index-2)%2==1)?string("an","aus")}</td>
					<#if (assignment_index == 2)>
						<td align="center">${model.phaseAssignment.assignment[0]?string}</td>
					<#else>
						<td align="center">${assignment?string}</td>
					</#if>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>