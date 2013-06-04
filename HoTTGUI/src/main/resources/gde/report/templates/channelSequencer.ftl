<#assign show=false/>
<#list model.channelSequencer.sequence as seq>
	<#if seq.enabled>
		<#assign show=true/>
		<#break>
	</#if>
</#list>

<table class="<@u show/>">

	<caption>Kanal Sequenzer</caption>
	
	<thead>
		<tr>
			<th align="center">Kanal</th>
			<th align="center">aktiv?</th>
			<th align="center">Start</th>
			<#list model.channelSequencer.stepTime as stepTime>
				<#if stepTime_index < model.channelSequencer.maxStep>
							<th align="center">${stepTime}s</th>
				</#if>
			</#list>
		</tr>
	</thead>
	
	<@reset/>

	<tbody>
		<#list model.channelSequencer.sequence as seq>
			<tr class="<@d/> <@u seq.enabled/>">
				<td align="center">Kanal ${seq.outputChannel.number}</td>
				<td align="center">${seq.enabled?string("aktiv","inaktiv")}</td>

				<#if seq.enabled>
					<#list seq.stepPosition as pos>
						<#if pos_index <= model.channelSequencer.maxStep>
							<td align="center">${pos}</td>
						</#if>
					</#list>
				<#else>
					<td align="center" colspan="${model.channelSequencer.maxStep+1}" class="d0"/>
				</#if>
			</tr>
		</#list>
		
		<tr class="<@d/>">
			<th colspan="2" align="right">Schalter</th>
			<td align="left" colspan="${model.channelSequencer.maxStep+1}"><@switch model.channelSequencer.switch/></td>
		</tr>
	</tbody>
</table>