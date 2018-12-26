<#if model.isMenuEnabled("Channel1Curve")>
<#list model.phase as phase>
	<#if phase.phaseType.name() != "Unused">
		<table>
			<caption><a name="channel1Curve${phase.number}"></a>Kanal 1 Kurve - ${phase.toString()}</caption>
			
			<tbody>
				<@heliCurve phase.toString(), phase.channel1Curve/>
			</tbody>
		</table>
	</#if>
</#list>
</#if>
