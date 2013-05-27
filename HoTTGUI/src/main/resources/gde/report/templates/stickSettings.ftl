<table>
	<caption>Knüppeleinstellungen</caption>
	
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
		<#if wingedModel??>
			<tr class="<@d/>">
				<td align="center">Kanal 1</td>
				<td align="center">${model.stickTrim[0].mode}</td>
				<td align="center">${model.stickTrim[0].increment}</td>
				<td align="center">${model.stickTrim[0].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[0].timeHigh?string("0.0")}s</td>
			</tr>
			<tr class="<@d/>">
				<td align="center">Querruder</td>
				<td align="center">${model.stickTrim[1].mode}</td>
				<td align="center">${model.stickTrim[1].increment}</td>
				<td align="center">${model.stickTrim[1].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[1].timeHigh?string("0.0")}s</td>
			</tr>
			<tr class="<@d/>">
				<td align="center">Höhenruder</td>
				<td align="center">${model.stickTrim[2].mode}</td>
				<td align="center">${model.stickTrim[2].increment}</td>
				<td align="center">${model.stickTrim[2].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[2].timeHigh?string("0.0")}s</td>
			</tr>
			<tr class="<@d/>">
				<td align="center">Seitenruder</td>
				<td align="center">${model.stickTrim[3].mode}</td>
				<td align="center">${model.stickTrim[3].increment}</td>
				<td align="center">${model.stickTrim[3].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[3].timeHigh?string("0.0")}s</td>
			</tr>
		<#else>
			<tr class="<@d/>">
				<td align="center">Pitch/Gas</td>
				<td align="center">${model.stickTrim[0].mode}</td>
				<td align="center">${model.stickTrim[0].increment}</td>
				<td align="center">${model.stickTrim[0].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[0].timeHigh?string("0.0")}s</td>
			</tr>
			<tr class="<@d/>">
				<td align="center">Roll</td>
				<td align="center">${model.stickTrim[1].mode}</td>
				<td align="center">${model.stickTrim[1].increment}</td>
				<td align="center">${model.stickTrim[1].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[1].timeHigh?string("0.0")}s</td>
			</tr>
			<tr class="<@d/>">
				<td align="center">Nick</td>
				<td align="center">${model.stickTrim[2].mode}</td>
				<td align="center">${model.stickTrim[2].increment}</td>
				<td align="center">${model.stickTrim[2].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[2].timeHigh?string("0.0")}s</td>
			</tr>
			<tr class="<@d/>">
				<td align="center">Heckrotor</td>
				<td align="center">${model.stickTrim[3].mode}</td>
				<td align="center">${model.stickTrim[3].increment}</td>
				<td align="center">${model.stickTrim[3].timeLow?string("0.0")}s</td>
				<td align="center">${model.stickTrim[3].timeHigh?string("0.0")}s</td>
			</tr>
		</#if>
	</tbody>			
</table>