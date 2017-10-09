<#if model.switchAnnouncements??>
	<table>
		<caption><a name="announcements"/>Ankünden</caption>
		
		<thead>
			<tr>
				<th align="center">Nummer</th>
				<th align="center">Schalter</th>
				<th align="center">Ansage an</th>
				<th align="center">Ansage aus</th>
			</tr>
		</thead>
	
		<@reset/>
		
		<tbody>
			<#list model.switchAnnouncements as announcement>
				<tr class="<@d/> <@u announcement.switch.assignment?? && announcement.switch.assignment.name() != "Unassigned"/>">
					<td align="center">${announcement_index + 1}</td>
					<td align="center"><@switch announcement.switch/></td>
					<td align="center"><@sound announcement.name[0]/></td>
					<td align="center"><@sound announcement.name[1]/></td>
				</tr>
			</#list>
		</tbody>
	</table>
</#if>