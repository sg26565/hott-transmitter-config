<table>
	<caption><a name="failSafe"/>Fail Safe</caption>

	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">Verzögerung</th>
			<td align="left" colspan="2">${model.failSafeDelay}s</td>
			<td colspan="${model.channel?size-4}" class="d0"/>				
		</tr>
		<tr class="<@d/>">
			<th align="right">FlailSafe Prüfung</th>
			<td align="left" colspan="2">${model.failSafeSettingCheck?string("ja", "nein")}</td>
			<td colspan="${model.channel?size-4}" class="d0"/>
		</tr>
		<tr class="<@d/>">
			<th/>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<th align="center">S${channel.number?number+1}</th>
				</#if>
			</#list>
		</tr>
		
		<@reset/>
		
		<tr class="<@d/>">
			<th align="right">Position</th>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<td align="center"><#if channel.failSafeMode.name()=="Position">${channel.failSafePosition}%</#if></td>
				</#if>
			</#list>
		</tr>
		<tr class="<@d/>">
			<th align="right">Hold</th>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<td align="center"><#if channel.failSafeMode.name()=="Hold">&times;</#if></td>
				</#if>
			</#list>
		</tr>
	</tbody>
</table>