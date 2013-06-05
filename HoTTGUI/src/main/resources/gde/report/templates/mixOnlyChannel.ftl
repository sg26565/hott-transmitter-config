<a name="mixOnlyChannel"/>
<table>
	<caption>Nur MIX Kanal</caption>
	
	<thead>
		<tr>
			<th/>
			<#list model.channel as channel>
				<#if !channel.virtual>		
					<th align="center">S${channel.number?number+1}</th>
				</#if>
			</#list>
		</tr>
	</thead>
	
	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">Nur Mix</th>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<td align="center">${channel.mixOnly?string("&times;","")}</td>
				</#if>
			</#list>
		</tr>
		<tr class="<@d/>">
			<th align="right">normal</th>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<td align="center">${channel.mixOnly?string("","&times;")}</td>
				</#if>
			</#list>
		</tr>
	</tbody>
</table>