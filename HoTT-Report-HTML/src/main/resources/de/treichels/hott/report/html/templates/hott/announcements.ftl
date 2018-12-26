<#if model.isMenuEnabled("Announcement")>
<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<table class="<@u phase.switchAnnouncements?size &gt; 0/>">
			<caption><a name="announcements${phase.number}"></a>Ankünden - ${phase.toString()}</caption>
			
			<thead>
				<tr>
					<th align="center">Nummer</th>
					<th align="center">Schalter</th>
					<th align="center">Typ</th>
					<th align="center">Ansage an</th>
					<th align="center">Ansage aus</th>
				</tr>
			</thead>
		
			<@reset/>
			
			<tbody>
				<#list phase.switchAnnouncements as announcement>
					<tr class="<@d/> <@u announcement.switch.assignment.name() != "Unassigned"/>">
						<td align="center">${announcement_index + 1}</td>
						<td align="center"><@switch announcement.switch/></td>
						<td align="center">${announcement.announcementType}</td>
						<td align="center"><@sound announcement.name[0]/></td>
						<td align="center"><@sound announcement.name[1]/></td>
					</tr>
				</#list>
			</tbody>
		</table>
	</#if>
</#list>
</#if>
