<table>
	<caption><a name="trimMemory"></a>Trimmspeicher</caption>
	
	<thead>
		<tr>
			<th>Phase</th>
			<#if helicopterModel??>
				<th align="center" colspan="2">Pitch/Gas</th>
				<th align="center" colspan="2">Roll</th>
				<th align="center" colspan="2">Nick</th>
				<th align="center" colspan="2">Heckrotor</th>
			<#else>
				<#switch model.modelType.name()>
					<#case "Winged">
						<th align="center" colspan="2"><#if model.motorOnC1Type.name() == "Idle_Front" || model.motorOnC1Type.name() == "Idle_Rear">Gas<#else>Kanal 1</#if></th>
						<th align="center" colspan="2">Querruder</th>
						<th align="center" colspan="2">Höhenruder</th>
						<th align="center" colspan="2">Seitenruder</th>
						<#break>

					<#case "Boat">
					<#case "Car">
						<th align="center" colspan="2"><#if model.motorOnC1Type.name() == "Idle_Front" || model.motorOnC1Type.name() == "Idle_Rear">Gas<#else>Kanal 1</#if></th>
						<th align="center" colspan="2">Steuerung</th>
						<th align="center" colspan="2">Kanal 3</th>
						<th align="center" colspan="2">Kanal 4</th>
						<#break>

					<#case "Copter">
						<th align="center" colspan="2">Schub</th>
						<th align="center" colspan="2">Roll</th>
						<th align="center" colspan="2">Nick</th>
						<th align="center" colspan="2">Gier</th>
						<#break>
				</#switch>
			</#if>
		</tr>
		<tr style="font-size: 75%;">
			<th></th>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
			<th align="center">Knüppel</th>				
			<th align="center">Speicher</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<tr class="<@d/>">
					<td align="center">${phase.toString()}</td>
					<#if helicopterModel??>
						<td align="center">${model.throttleSettings.throttleTrim}%</td>
						<td align="center">${phase.control[0].trim}%</td>
						<td align="center">${phase.helicopterTrim.rollStickTrim}%</td>
						<td align="center">${phase.control[1].trim}%</td>
						<td align="center">${phase.helicopterTrim.nickStickTrim}%</td>
						<td align="center">${phase.control[2].trim}%</td>
						<td align="center">${phase.helicopterTrim.tailStickTrim}%</td>
						<td align="center">${phase.control[3].trim}%</td>
					<#else>
						<td align="center">${model.throttleSettings.throttleTrim}%</td>
						<td align="center">${phase.control[0].trim}%</td>
						<td align="center">${phase.wingTrim.aileronStickTrim}%</td>
						<td align="center">${phase.control[1].trim}%</td>
						<td align="center">${phase.wingTrim.elevatorStickTrim}%</td>
						<td align="center">${phase.control[2].trim}%</td>
						<td align="center">${phase.wingTrim.rudderStickTrim}%</td>
						<td align="center">${phase.control[3].trim}%</td>
					</#if>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
