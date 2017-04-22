<#if model.lapStore??>
	<table>
		<caption><a name="lapTimer"/>Rundenzähler</caption>
		
		<@reset/>
		
		<tbody>
			<tr>
				<th colspan="2" class="d2">Allgemein</th>
			</tr>
			<tr class="<@d/>">
				<th align="right">Rundenzähler aktiv?</th>
				<td align="center">${model.lapStore.active?string("ja", "nein")}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">angezeigte Runde</th>
				<td align="center">${model.lapStore.viewLap}</td>
			</tr>
			<tr class="<@d/>">
				<th align="right">aktuelle Runde</th>
				<td align="center">${model.lapStore.currentLap}</td>
			</tr>
			<#if model.lapStore.currentLap &gt; 0>
				<tr>
					<th colspan="2" class="d2">Rundenzeiten</th>
				</tr>
				<#list model.lapStore.laps as lap>
					<#if lap_index <= model.lapStore.currentLap>
						<tr class="<@d/>">
							<th align="right">Runde ${lap_index +1}</th>
							<td align="center">${lap.minute?string("00")}:${lap.second?string("00")}.${lap.millisecond?string("000")}</td>
						</tr>
					</#if>				
				</#list>
			</#if>
		</tbody>
	</table>
</#if>