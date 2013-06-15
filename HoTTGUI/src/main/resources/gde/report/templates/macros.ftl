<#macro switch sw>
	<#if sw.assignment.name() == "Unassigned">
		---
	<#else>
		${sw.assignment}
		<#switch sw.direction>
			<#case 2>normal<#break>				
			<#case 1>invers<#break>
		</#switch>
	</#if>
</#macro>

<#macro reset><#assign row="even"/></#macro>

<#macro d><#if row=="even">d0<#assign row="odd"/><#else>d1<#assign row="even"/></#if></#macro>

<#macro u b><#if b>used<#else>unused</#if></#macro>

<#macro curve title curve>
	<#if title != "">
		<tr>
			<th class="d2" colspan="5">${title}</th>
		</tr>
	</#if>

	<tr class="d0">
		<th align="center">Punkt</th>
		<th align="center">aktiv</th>
		<th align="center">Eingang</th>
		<th align="center">Ausgang</th>
		<td rowspan="${curve.point?size+1}" align="center"><img src="${curve.getImageSource(0.5)}"/></td>
	</tr>

	<@reset/>

	<#list curve.point as point>
		<tr class="<@d/> <@u point.enabled/>">
			<td align="center">${point.number?number+1}</td>
			<td align="center">${point.enabled?string("ja","nein")}</td>
			<#if point.enabled>
				<td align="center">${point.position}%</td>
				<td align="center">${point.value}%</td>
			<#else>
				<td colspan="2"/>
			</#if>
		</tr>
	</#list>
</#macro>

<#macro heliCurve title curve>
	<tr>
		<th class="d2" colspan="6">${title}</th>
	</tr>
	
	<tr class="d0">
		<th align="right">Kurve</th>
		<td colspan="4" align="left">${curve.smoothing?string("an","aus")}</td>
		<td rowspan="${curve.point?size+2}" align="center"><img src="${curve.getImageSource(1.0)}"/></td>
	</tr>

	<tr>
		<th/>
		<th align="center">Punkt</th>				
		<th align="center">aktiv</th>
		<th align="center">Eingang</th>
		<th align="center">Ausgang</th>
	</tr>

	<@reset/>

	<#list curve.point as point>
		<tr class="<@d/> <@u point.enabled/>">
			<th/>
			<td align="center">${point.number?number+1}</td>
			<td align="center">${point.enabled?string("ja","nein")}</td>
			<#if point.enabled>
				<td align="center">${point.position}%</td>
				<td align="center">${point.value}%</td>
			<#else>
				<td colspan="2" align="center">---</td>
			</#if>
		</tr>
	</#list>
</#macro>

<#macro wingCurve title curve>
	<tr>
		<th class="d2" colspan="9">${title}</th>
	</tr>
	
	<@reset/>
	
	<tr class="<@d/>">
		<th align="right">Kurve</th>
		<td colspan="4" align="left">${curve.smoothing?string("an","aus")}</td>
		<td colspan="4" rowspan="${curve.point?size+2}" align="center"><img src="${curve.getImageSource(1.0)}"/></td>
	</tr>

	<tr>
		<th/>
		<th align="center">Punkt</th>				
		<th align="center">aktiv</th>
		<th align="center">Eingang</th>
		<th align="center">Ausgang</th>
	</tr>

	<@reset/>

	<#list curve.point as point>
		<tr class="<@d/> <@u point.enabled/>">
			<th/>
			<td align="center">${point.number?number+1}</td>
			<td align="center">${point.enabled?string("ja","nein")}</td>
			<#if point.enabled>
				<td align="center">${point.position}%</td>
				<td align="center">${point.value}%</td>
			<#else>
				<td colspan="2" align="center">---</td>
			</#if>
		</tr>
	</#list>
</#macro>