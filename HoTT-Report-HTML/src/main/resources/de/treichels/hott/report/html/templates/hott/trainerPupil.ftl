<table>
	<caption><a name="trainerPupil"></a>Lehrer/Schüler</caption>
			
	<@reset/>
	
	<tbody>
		<tr class="<@d/>">
			<th align="right">Kabellos</th>
			<td align="left" colspan="6">${model.trainerConfig.wireless?string("ja", "nein")}</td>
			<td colspan="${model.channel?size-8}" class="d0"></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Lehrer ID</th>
			<td align="left" colspan="6">${hex(model.trainerConfig.trainerId?c)}</td>
			<td colspan="${model.channel?size-8}" class="d0"></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Schüler ID</th>
			<td align="left" colspan="6">${hex(model.trainerConfig.pupilId?c)}</td>
			<td colspan="${model.channel?size-8}" class="d0"></td>
		</tr>
		<tr class="<@d/>">
			<th align="right">Schalter</th>
			<td align="left" colspan="6"><@switch model.trainerConfig.trainerSwitch/></td>
			<td colspan="${model.channel?size-8}" class="d0"></td>
		</tr>
		<tr>
			<th></th>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<th align="center">S${channel.number?number+1}</th>
				</#if>
			</#list>
		</tr>

		<@reset/>

		<tr class="<@d/>">
			<th align="right">Schüler</th>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<td align="center"><#if channel.trainerMode.name()=="Pupil">&times;</#if></td>
				</#if>
			</#list>
		</tr>
		<tr class="<@d/>">
			<th align="right">Lehrer</th>
			<#list model.channel as channel>
				<#if !channel.virtual>
					<td align="center"><#if channel.trainerMode.name()=="Trainer">&times;</#if></td>
				</#if>
			</#list>
		</tr>
	</tbody>
</table>
