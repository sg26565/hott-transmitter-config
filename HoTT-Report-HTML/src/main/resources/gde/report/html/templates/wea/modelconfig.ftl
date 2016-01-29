<#escape x as x?html>
<table>
	<caption><a name="modelconfig"/>Modell Konfiguration</caption>
	
	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">Modellname</th>
			<td align="left">${model.modelConfig.modelName.value}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Bild</th>
			<td align="left">${model.modelConfig.image.value}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Info Text</th>
			<td align="left">${model.modelConfig.infoText.value}</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Auflösung</th>
			<td align="left">
				<#switch model.modelConfig.outputResolution.value>
					<#case 1>0.1 %<#break>
					<#case 2>0.5 %<#break>
					<#case 3>1 %<#break>
				</#switch>
			</td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Leitwerk</th>
			<td align="left">
				<#switch model.modelConfig.modelTypeDependent.tailType.value>
					<#case 1>Normal<#break>
					<#case 2>V-Leitwerk<#break>
					<#case 3>Delta<#break>
				</#switch>
			</td>
		</tr>
		<tr>
			<th class="d2" colspan="2">Anzahl Steuerflächen</th>
		</tr>
		<tr class="<@d/>">
			<th align="right">Flügel</th>
			<td align="left">${model.modelConfig.modelTypeDependent.wing.value}</td>
		</tr>		
		<tr class="<@d/>">
			<th align="right">Höhenruder</th>
			<td align="left">${model.modelConfig.modelTypeDependent.elevator.value}</td>
		</tr>		
		<tr class="<@d/>">
			<th align="right">Seitenruder</th>
			<td align="left">${model.modelConfig.modelTypeDependent.rudder.value}</td>
		</tr>		
	</tbody>
</table>
</#escape>