<#if wingedModel??>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<table>
				<caption>Flächenmischer - ${phase?string}</caption>
				
				<tbody>
					<tr>
						<th colspan="9" class="d2">Flächenmischer</th>
					</tr>

					<@reset/>

					<tr class="<@d/>">
						<th align="right">Querruder 2&rarr;4 Seitenruder</th>
						<td colspan="2" align="center">${phase.wingMixer[0].value[0]}%</td>
						<td colspan="6" align="left"><@switch phase.wingMixer[0].switch/></td>
					</tr>
					<tr class="<@d/>">
						<th align="right">Höhenruder 3&rarr;6 Wölbklappen</th>
						<td colspan="1" align="center">${phase.wingMixer[1].value[0]}%</td>
						<td colspan="1" align="center">${phase.wingMixer[1].value[1]}%</td>
						<td colspan="6" align="left"><@switch phase.wingMixer[1].switch/></td>
					</tr>
					<tr class="<@d/>">
						<th align="right">Wölbklappen 6&rarr;3 Höhenruder</th>
						<td colspan="1" align="center">${phase.wingMixer[2].value[0]}%</td>
						<td colspan="1" align="center">${phase.wingMixer[2].value[1]}%</td>
						<td colspan="6" align="left"><@switch phase.wingMixer[2].switch/></td>
					</tr>
					<tr class="<@d/>">
						<th align="right">Höhenruder 3&rarr;5 Querruder</th>
						<td colspan="1" align="center">${phase.wingMixer[3].value[0]}%</td>
						<td colspan="1" align="center">${phase.wingMixer[3].value[1]}%</td>
						<td colspan="6" align="left"><@switch phase.wingMixer[3].switch/></td>
					</tr>
					
					<tr>
						<th colspan="9" class="d2">Multi-Klappen-Menü</th>
					</tr>
									
					<tr>
						<th/>
						<th colspan="2" align="center">QR</th>
						<th colspan="2" align="center">QR2</th>
						<th colspan="2" align="center">WK</th>
						<th colspan="2" align="center">WK2</th>				
					</tr>

					<@reset/>

					<tr class="<@d/>">
						<th align="right">&uarr;QR&darr;</th>
						<#list phase.multiFlapMixer[0].value as mix>			
							<td colspan="2" align="center">${mix}%</td>
						</#list>
					</tr>
					<tr class="<@d/>">
						<th align="right">Querrudertrimmung</th>
						<#list phase.multiFlapMixer[1].value as mix>			
							<td colspan="2" align="center">${mix}%</td>
						</#list>
					</tr>
					<tr class="<@d/>">
						<th align="right">Querruderdifferenzierung</th>
						<#list phase.multiFlapMixer[2].value as mix>			
							<td colspan="2" align="center">${mix}%</td>
						</#list>
					</tr>
					<tr class="<@d/>">
						<th align="right">Wölbklappenposition</th>
						<td colspan="2" align="center">${phase.wingTrim.aileronPhaseTrim[0]}%</td>
						<td colspan="2" align="center">${phase.wingTrim.aileronPhaseTrim[1]}%</td>
						<td colspan="2" align="center">${phase.wingTrim.flapPhaseTrim[0]}%</td>
						<td colspan="2" align="center">${phase.wingTrim.flapPhaseTrim[1]}%</td>
					</tr>
					<tr class="<@d/>">
						<th align="right">&uarr;WK&uarr;</th>
						<#list phase.multiFlapMixer[3].value as mix>			
							<td align="center">${mix}%</td>
						</#list>
					</tr>
					<tr class="<@d/>">
						<th align="right">HR&rarr;WK</th>
						<#list phase.multiFlapMixer[4].value as mix>			
							<td align="center">${mix}%</td>
						</#list>
					</tr>

					<tr>
						<th colspan="9" class="d2">Bremseinstellungen</th>
					</tr>

					<@reset/>

					<tr class="<@d/>">
						<th align="right">Butterfly</th>
						<#list phase.brakeMixer[0].value as mix>			
							<td colspan="2" align="center">${mix}%</td>
						</#list>
					</tr>
					<tr class="<@d/>">
						<th align="right">Diff.-Reduction</th>
						<#list phase.brakeMixer[1].value as mix>			
							<td colspan="2" align="center">${mix}%</td>
						</#list>
					</tr>

					<@wingCurve "Bremskurve" phase.brakeElevatorCurve/>
				</tbody>				
			</table>
		</#if>
	</#list>
</#if>