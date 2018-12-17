<#if wingedModel??>
	<table>
		<caption><a name="wingMix0"></a>Flächenmix</caption>

		<thead>
			<tr class="<@d/>">
				<th align="center">Mischer</th>
				<th align="center">Wert</th>
				<th align="center">Schalter</th>
			</tr>
		</thead>

		<@reset/>
		
		<tbody>
			<#list wingedModel.phase[0].wingMixer as mixer>
				<tr class="<@d/>">
					<td align="center">${mixer.id}</td>
					<td align="center">${mixer.value[0]}%</td>
						<#if mixer.switch??>
							<td align="center"><@switch mixer.switch/></td>
						<#else>
							<td></td>
						</#if>
				</tr>
			</#list>
		</tbody>
	</table>
</#if>
