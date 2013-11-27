<#assign show=false/>
<#list model.multichannel as multi>
	<#if multi.enabled>
		<#assign show=true/>
		<#break>
	</#if>
</#list>
<table class="<@u show/>">
	<caption><a name="multiChannel"/>Multikanal</caption>

	<thead>
		<tr>
			<th align="center">Kanal</th>
			<th align="center">aktiv?</th>
			<th align="center">Steuerkanal</th>
			<th align="center">Kanalzahl</th>
			<th align="center">Eingang</th>
			<th colspan="2" align="center">Geber</th>
			<th align="center">Offset</th>
			<th align="center">Weg -</th>
			<th align="center">Weg +</th>
		</tr>
	</thead>

	<@reset/>

	<tbody>
		<#list model.multichannel as multi>
			<#list multi.control as control>
			
				<#-- skip extra controls for four channel module -->			
				<#if control_index &gt; 3 && multi.mode.name() == "FourCh">
					<#break>
				</#if>
				
				<tr class="<@d/> <@u multi.enabled/>">
					<#if control_index == 0>
						<td align="center">Multikanal ${multi_index+1}</td>
						<td align="center">${multi.enabled?string("aktiv","inaktiv")}</td>
						<td align="center">K${multi.inputChannel.number?number+1}</td>
						<td align="center">${multi.mode}</td>
					<#else>
						<td colspan="4"/>
					</#if>

					<td>Eingang ${control_index+1}</td>

					<#if control.controlSwitch?? && control.controlSwitch.assignment.name() != "Unassigned">
						<td align="center" colspan="2"><@switch control.controlSwitch/></td>
					<#elseif control.toggleLowSwitch?? && control.toggleLowSwitch.assignment.name() != "Unassigned" || control.toggleHighSwitch?? && control.toggleHighSwitch.assignment.name() != "Unassigned">
						<td align="center"><@switch control.toggleLowSwitch/></td>
						<td align="center"><@switch control.toggleHighSwitch/></td>
					<#else>
						<td align="center" colspan="2">---</td>
					</#if>
					
					<td align="center">${control.offset}%</td>
					<td align="center">${control.travelLow}%</td>
					<td align="center">${control.travelHigh}%</td>
				</tr>
			</#list>
		</#list>
	</tbody>
</table>