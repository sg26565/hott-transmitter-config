<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<table>
			<caption><a name="channel1Curve${phase.number}"/>Kanal 1 Kurve - ${phase?string}</caption>
			
			<tbody>
				<@heliCurve phase?string, phase.channel1Curve/>
			</tbody>
		</table>
	</#if>
</#list>