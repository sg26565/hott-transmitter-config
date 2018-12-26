<#if model.isMenuEnabled("ControlAdjustment")>
<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<table>
			<caption><a name="controls${phase.number}"></a>Gebereinstellungen - ${phase.toString()}</caption>
			
			<thead>
				<tr>
					<th align="center">Eingang</th>
					<th align="center">Typ</th>
					<th align="center" colspan="2">Geber</th>
					<th align="center">Offset</th>
					<th align="center">Weg -</th>
					<th align="center">Weg +</th>
					<th align="center">Zeit -</th>
					<th align="center">Zeit +</th>
				</tr>
			</thead>
		
			<tbody>
				<@reset/>

				<#list phase.control as control>
					<tr class="<@d/> <@u (phase.number == 0 || control.mode.name() != "Global")/>">
						<td align="center">E${control.number?number+1}<#if control.function.name() != "Unknown"> (${control.function})</#if></td>
						<td align="center">${control.mode}</td>
						<#if control.inputControl.assignment.name() != "Unassigned">
							<td align="center" colspan="2"><@switch control.inputControl/></td>
						<#elseif control.toggleLowSwitch.assignment.name() != "Unassigned" || control.toggleHighSwitch.assignment.name() != "Unassigned">
							<td align="center"><@switch control.toggleLowSwitch/></td>
							<td align="center"><@switch control.toggleHighSwitch/></td>
						<#else>
							<td align="center" colspan="2">---</td>
						</#if>
						<td align="center">${control.offset}%</td>
						<td align="center">${control.travelLow}%</td>
						<td align="center">${control.travelHigh}%</td>
						<td align="center">${control.timeLow?string("0.0")}s</td>
						<td align="center">${control.timeHigh?string("0.0")}s</td>
					</tr>
				</#list>
			</tbody>
		</table>
	</#if>
</#list>
</#if>
