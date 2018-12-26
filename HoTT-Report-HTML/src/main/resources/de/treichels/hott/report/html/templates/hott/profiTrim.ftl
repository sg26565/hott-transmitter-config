<#if model.transmitterType.name() != "mx20" && (model.modelType.name() == "Winged" || model.modelType.name() == "Helicopter") && model.isMenuEnabled("ProfiTrim")>
	<#assign show=false/>
	<#if wingedModel??>
		<#list wingedModel.profiTrim as trim>
			<#if trim.enabled>
				<#assign show=true/>
				<#break>
			</#if>
		</#list>
	</#if>
	<#if helicopterModel??>
		<#list helicopterModel.profiTrim as trim>
			<#if trim.inputControl.assignment.name() != "Unassigned">
				<#assign show=true/>
				<#break>
			</#if>
		</#list>
	</#if>
	<table class="<@u show/>">
		<caption><a name="profiTrim"></a>Profitrimm</caption>
	
		<#if wingedModel??>
			<thead>	
				<tr>
					<th></th>
					<th align="center">&uarr;QR&darr;</th>
					<th align="center">&uarr;QR&uarr;</th>
					<th align="center">&uarr;WK&darr;</th>
					<th align="center">&uarr;WK&uarr;</th>
				</tr>
			</thead>
					
			<@reset/>
	
			<tbody>
				<tr class="<@d/>">
					<th align="right">aktiv</th>
					<#list wingedModel.profiTrim as trim>
						<td align="center">${trim.enabled?string("an","aus")}</td>
					</#list>
				</tr>
				<tr class="<@d/>">
					<th align="right">Geber</th>
					<#list wingedModel.profiTrim as trim>
						<td align="center"><#if trim.inputControl.assignment.name() == "Unassigned">frei<#else>${trim.inputControl.assignment}</#if></td>
					</#list>
				</tr>
				<tr class="<@d/>">
					<th align="right">EIN/AUS</th>
					<td align="left" colspan="4"><@switch wingedModel.profiTrimSwitch/></td>
				</tr>
			</tbody>
		</#if>
		<#if helicopterModel??>
			<thead>
				<tr>
					<th align="center">Timmgeber</th>
					<th align="center">Mischer</th>
					<th align="center">Punkt</th>
					<th align="center">Phase</th>
				</tr>
			</thead>
			
			<@reset/>
			
			<tbody>
				<#list helicopterModel.profiTrim as trim>
					<tr class="<@d/> <@u trim.inputControl.assignment.name() != "Unassigned"/>">
						<td align="center"><#if trim.inputControl.assignment.name() == "Unassigned">frei<#else>${trim.inputControl.assignment}</#if></td>
						<td align="center">${trim.curveType}</td>
						<td align="center">${trim.point}</td>
						<td align="center">Phase ${trim.phase.number?number+1}: ${trim.phase.phaseName}</td>
					</tr>
				</#list>
			</tbody>
		</#if>
	</table>
</#if>
