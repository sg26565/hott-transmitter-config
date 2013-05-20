<table>
	<caption>Kanal 1 Kurve</caption>
	
	<tbody>
		<#list model.phase as phase>
			<#if phase.phaseType.name() != "Unused">
				<@heliCurve phase?string, phase.channel1Curve/>
			</#if>
		</#list>
	</tbody>
</table>
