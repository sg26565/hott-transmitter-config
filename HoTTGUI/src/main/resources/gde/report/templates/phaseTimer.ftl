<table>
	<caption><a name="phaseTimer"/>Flugphasenuhren</caption>
	
	<thead>
		<tr>
			<th/>
			<th align="center">Timer</th>
			<th align="center">Alarm</th>
			<th align="center">Schalter</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list 0..2 as index>
			<tr class="<@d/>">
				<th align="right">${model.clock[index].type}</th>
				<td align="center">${model.clock[index].minutes}:${model.clock[index].seconds?string("00")}</td>
				<td align="center">${model.clock[index].alarm}s</td>
				<td align="center"><@switch model.clock[index].switch/></td>
			</tr>
		</#list>
		<tr class="<@d/>">
			<th align="right">Rundenzähler/Zeittabelle</th>
			<td colspan="2" class="d0"/>
			<td align="center"><@switch model.getSwitch("Clock_LapTimer")/></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Rundenanzeige</th>
			<td colspan="2" class="d0"/>
			<td align="center"><@switch model.getSwitch("Clock_LapDisplay")/></td>
		</tr>
	</tbody>
</table>