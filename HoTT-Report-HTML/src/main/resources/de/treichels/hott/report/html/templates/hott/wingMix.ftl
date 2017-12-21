<#if wingedModel??>
	<#list model.phase as phase>
		<#if phase.phaseType.name() != "Unused">
			<table>
				<caption><a name="wingMix${phase.number}"/>Flächenmischer - ${phase?string}</caption>
				
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

					<#if wingedModel.aileronFlapType.name() == "OneAilOneFlap">
						<tr class="<@d/>">
							<th align="right">Höhenruder 3&rarr;6 Wölbklappen</th>
							<td colspan="1" align="center">${phase.wingMixer[1].value[0]}%</td>
							<td colspan="1" align="center">${phase.wingMixer[1].value[1]}%</td>
							<td colspan="6" align="left"><@switch phase.wingMixer[1].switch/></td>
						</tr>
					</#if>

					<#if wingedModel.aileronFlapType.flaps&gt;0>
						<tr class="<@d/>">
							<th align="right">Wölbklappen 6&rarr;3 Höhenruder</th>
							<td colspan="1" align="center">${phase.wingMixer[2].value[0]}%</td>
							<td colspan="1" align="center">${phase.wingMixer[2].value[1]}%</td>
							<td colspan="6" align="left"><@switch phase.wingMixer[2].switch/></td>
						</tr>
					</#if>

					<#if wingedModel.aileronFlapType.name() == "TwoAil">
						<tr class="<@d/>">
							<th align="right">Höhenruder 3&rarr;5 Querruder</th>
							<td colspan="1" align="center">${phase.wingMixer[3].value[0]}%</td>
							<td colspan="1" align="center">${phase.wingMixer[3].value[1]}%</td>
							<td colspan="6" align="left"><@switch phase.wingMixer[3].switch/></td>
						</tr>
					</#if>					

					<#if wingedModel.aileronFlapType.name() == "TwoAil" || wingedModel.aileronFlapType.name() == "TwoAilOneFlap">
						<tr class="<@d/>">
							<th align="right">Querruderdifferenzierung</th>
							<td colspan="2" align="center">${phase.multiFlapMixer[2].value[0]}%</td>
							<td colspan="6"/>
						</tr>
					</#if>

					<#if wingedModel.aileronFlapType.ailerons&gt;1 && wingedModel.aileronFlapType.flaps&gt;0>
						<tr>
							<th colspan="9" class="d2">Multi-Klappen-Menü</th>
						</tr>									
						<tr>
							<th/>
							<th colspan="2" align="center">QR</th>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<th colspan="2" align="center">QR2</th>
							<#else>
								<th colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<th colspan="2" align="center">WK</th>
							<#else>
								<th colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<th colspan="2" align="center">WK2</th>				
							<#else>
								<th colspan="2"/>
							</#if>
						</tr>
	
						<@reset/>
	
						<tr class="<@d/>">
							<th align="right">&uarr;QR&darr;</th>
							<td colspan="2" align="center">${phase.multiFlapMixer[0].value[0]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td colspan="2" align="center">${phase.multiFlapMixer[0].value[1]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td colspan="2" align="center">${phase.multiFlapMixer[0].value[2]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td colspan="2" align="center">${phase.multiFlapMixer[0].value[3]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
						<tr class="<@d/>">
							<th align="right">Querrudertrimmung</th>
							<td colspan="2" align="center">${phase.multiFlapMixer[1].value[0]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td colspan="2" align="center">${phase.multiFlapMixer[1].value[1]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td colspan="2" align="center">${phase.multiFlapMixer[1].value[2]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td colspan="2" align="center">${phase.multiFlapMixer[1].value[3]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
						<tr class="<@d/>">
							<th align="right">Querruderdifferenzierung</th>
							<td colspan="2" align="center">${phase.multiFlapMixer[2].value[0]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td colspan="2" align="center">${phase.multiFlapMixer[2].value[1]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td colspan="2" align="center">${phase.multiFlapMixer[2].value[2]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td colspan="2" align="center">${phase.multiFlapMixer[2].value[3]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
						<tr class="<@d/>">
							<th align="right">Wölbklappenposition</th>
							<td colspan="2" align="center">${phase.wingTrim.aileronPhaseTrim[0]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td colspan="2" align="center">${phase.wingTrim.aileronPhaseTrim[1]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td colspan="2" align="center">${phase.wingTrim.flapPhaseTrim[0]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td colspan="2" align="center">${phase.wingTrim.flapPhaseTrim[1]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
						<tr class="<@d/>">
							<th align="right">&uarr;WK&uarr;</th>
							<td align="center">${phase.multiFlapMixer[3].value[0]}%</td>
							<td align="center">${phase.multiFlapMixer[3].value[1]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td align="center">${phase.multiFlapMixer[3].value[2]}%</td>
								<td align="center">${phase.multiFlapMixer[3].value[3]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td align="center">${phase.multiFlapMixer[3].value[4]}%</td>								
								<td align="center">${phase.multiFlapMixer[3].value[5]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td align="center">${phase.multiFlapMixer[3].value[6]}%</td>								
								<td align="center">${phase.multiFlapMixer[3].value[7]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
						<tr class="<@d/>">
							<th align="right">HR&rarr;WK</th>
							<td align="center">${phase.multiFlapMixer[4].value[0]}%</td>
							<td align="center">${phase.multiFlapMixer[4].value[1]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td align="center">${phase.multiFlapMixer[4].value[2]}%</td>
								<td align="center">${phase.multiFlapMixer[4].value[3]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td align="center">${phase.multiFlapMixer[4].value[4]}%</td>								
								<td align="center">${phase.multiFlapMixer[4].value[5]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td align="center">${phase.multiFlapMixer[4].value[6]}%</td>								
								<td align="center">${phase.multiFlapMixer[4].value[7]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
						<tr class="<@d/>">
							<th align="right">HR&rarr;WK-Offset</th>
							<td align="center">${phase.multiFlapMixer[5].value[0]}%</td>								
							<td colspan="7" align="left"><@switch phase.multiFlapMixer[5].switch/></td>								
						</tr>
					</#if>

					<#if wingedModel.aileronFlapType.ailerons&gt;1>
						<tr>
							<th colspan="9" class="d2">Bremseinstellungen</th>
						</tr>
	
						<@reset/>

						<tr class="<@d/>">
							<th align="right">Butterfly</th>
							<td colspan="2" align="center">${phase.brakeMixer[0].value[0]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td colspan="2" align="center">${phase.brakeMixer[0].value[1]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td colspan="2" align="center">${phase.brakeMixer[0].value[2]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td colspan="2" align="center">${phase.brakeMixer[0].value[3]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
						<tr class="<@d/>">
							<th align="right">Diff.-Reduction</th>
							<td colspan="2" align="center">${phase.brakeMixer[1].value[0]}%</td>
							<#if wingedModel.aileronFlapType.ailerons==4>
								<td colspan="2" align="center">${phase.brakeMixer[1].value[1]}%</td>
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps&gt;1>
								<td colspan="2" align="center">${phase.brakeMixer[1].value[2]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
							<#if wingedModel.aileronFlapType.flaps==4>
								<td colspan="2" align="center">${phase.brakeMixer[1].value[3]}%</td>								
							<#else>
								<td colspan="2"/>
							</#if>
						</tr>
					</#if>

					<@wingCurve "Bremskurve" phase.brakeElevatorCurve/>
				</tbody>				
			</table>
		</#if>
	</#list>
</#if>