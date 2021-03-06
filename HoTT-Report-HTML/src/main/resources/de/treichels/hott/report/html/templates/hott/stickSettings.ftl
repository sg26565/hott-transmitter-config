<#if model.isMenuEnabled("StickMode")>
<table>
	<caption><a name="stickSettings"></a>Knüppeleinstellungen</caption>
	
	<thead>
		<tr>
			<th align="center">Kanal</th>
			<th align="center">Trimm</th>
			<th align="center">Trimmschritte</th>
			<th align="center">Zeit -</th>
			<th align="center">Zeit +</th>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<#list 0..3 as i>
			<tr class="<@d/>">
				<#if model.modelType.name() == "Winged">
					<#switch i>
						<#case 0>
							<td align="center"><#if model.motorOnC1Type.name() == "Idle_Front" || model.motorOnC1Type.name() == "Idle_Rear">Gas<#else>Kanal 1</#if></td>
							<#break>
						
						<#case 1>	
							<td align="center">Querruder</td>
							<#break>

						<#case 2>	
							<td align="center">Höhenruder</td>
							<#break>

						<#case 3>	
							<td align="center">Seitenruder</td>
							<#break>
					</#switch>
				<#elseif model.modelType.name() == "Boat" || model.modelType.name() == "Car">
					<#switch i>
						<#case 0>
							<td align="center"><#if model.motorOnC1Type.name() == "Idle_Front" || model.motorOnC1Type.name() == "Idle_Rear">Gas<#else>Kanal 1</#if></td>
							<#break>

						<#case 1>
							<td align="center">Steuerung</td>
							<#break>

						<#case 2>
							<td align="center">Kanal 3</td>
							<#break>

						<#case 3>
							<td align="center">Kanal 4</td>
							<#break>
					</#switch>
				<#elseif model.modelType.name() == "Copter">
					<#switch i>
						<#case 0>
							<td align="center">Schub</td>
							<#break>

						<#case 1>
							<td align="center">Roll</td>
							<#break>

						<#case 2>
							<td align="center">Nick</td>
							<#break>

						<#case 3>
							<td align="center">Gier</td>
							<#break>
					</#switch>
				<#else>
					<#switch i>
						<#case 0>	
							<td align="center">Pitch/Gas</td>
							<#break>
						
						<#case 1>	
							<td align="center">Roll</td>
							<#break>

						<#case 2>	
							<td align="center">Nick</td>
							<#break>
						<#case 3>	
							<td align="center">Heckrotor</td>
							<#break>
					</#switch>
				</#if>				
				<td align="center">${model.stickTrim[i].mode}</td>
				<td align="center">${model.stickTrim[i].increment}</td>
				<td align="center">${model.stickTrim[i].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[i].timeHigh?string("0.0")}s</td>
			</tr>
		</#list>
	</tbody>			
</table>
</#if>
