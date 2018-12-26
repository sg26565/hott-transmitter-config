<table>
	<caption><a name="stickTrim"></a>Knüppeltrimmung</caption>

	<thead>
		<tr>
			<th>Phase</th>
			<#switch model.modelType.name()>
				<#case "Heli">
					<th align="center">Pitch/Gas</th>
					<th align="center">Roll</th>
					<th align="center">Nick</th>
					<th align="center">Heckrotor</th>
					<#break>

				<#case "Winged">
					<th align="center"><#if model.motorOnC1Type.name() == "Idle_Front" || model.motorOnC1Type.name() == "Idle_Rear">Gas<#else>Kanal 1</#if></th>
					<th align="center">Querruder</th>
					<th align="center">Höhenruder</th>
					<th align="center">Seitenruder</th>
					<#break>

				<#case "Boat">
				<#case "Car">
					<th align="center"><#if model.motorOnC1Type.name() == "Idle_Front" || model.motorOnC1Type.name() == "Idle_Rear">Gas<#else>Kanal 1</#if></th>
					<th align="center">Steuerung</th>
					<th align="center">Kanal 3</th>
					<th align="center">Kanal 4</th>
					<#break>

				<#case "Copter">
					<th align="center">Schub</th>
					<th align="center">Roll</th>
					<th align="center">Nick</th>
					<th align="center">Gier</th>
				<#break>
			</#switch>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.number != -1 && phase.phaseType.name() != "Unused">
				<tr class="<@d/>">
					<td align="center">${phase.toString()}</td>
					<td align="center">${model.throttleSettings.throttleTrim}%</td>
					<#if model.modelType.name() == "Helicopter">
						<td align="center">${phase.helicopterTrim.rollStickTrim}%</td>
						<td align="center">${phase.helicopterTrim.nickStickTrim}%</td>
						<td align="center">${phase.helicopterTrim.tailStickTrim}%</td>
					<#else>
						<td align="center">${phase.wingTrim.aileronStickTrim}%</td>
						<td align="center">${phase.wingTrim.elevatorStickTrim}%</td>
						<td align="center">${phase.wingTrim.rudderStickTrim}%</td>
					</#if>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
