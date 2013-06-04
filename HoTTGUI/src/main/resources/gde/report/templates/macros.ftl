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

<#macro svg points size=1>
	<#if points[0].position == 0>
		<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="${(200*size)?c}" height="${(250*size)?c}">
			<rect x="0" y="0" width="${(200*size)?c}" height="${(250*size)?c}" fill="none" stroke="darkGrey" stroke-width="2"/>
			<line x1="0" y1="${(25*size)?c}" x2="${(200*size)?c}" y2="${(25*size)?c}" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
			<line x1="0" y1="${(225*size)?c}" x2="${(200*size)?c}" y2="${(225*size)?c}" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
			<polyline points="<#list points as point><#if point.enabled>${point.position*2*size},${(225-point.value*2)*size} </#if></#list>" stroke="black" stroke-width="2" fill="none"/>
		</svg>
	<#else>
		<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="${(200*size)?c}" height="${(250*size)?c}">
			<rect x="0" y="0" width="${(200*size)?c}" height="${(250*size)?c}" fill="none" stroke="darkGrey" stroke-width="2"/>
			<line x1="0" y1="${(25*size)?c}" x2="${(200*size)?c}" y2="${(25*size)?c}" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
			<line x1="0" y1="${(125*size)?c}" x2="${(200*size)?c}" y2="${(125*size)?c}" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
			<line x1="0" y1="${(225*size)?c}" x2="${(200*size)?c}" y2="${(225*size)?c}" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
			<line x1="${(100*size)?c}" y1="0" x2="${(100*size)?c}" y2="${(250*size)?c}" stroke="darkGrey" stroke-width="2" stroke-dasharray="4,4"/>
			<polyline points="<#list points as point><#if point.enabled>${((point.position+100)*size)?c},${((125-point.value)*size)?c} </#if></#list>" stroke="black" stroke-width="2" fill="none"/>
		</svg>
	</#if>
</#macro>

<#macro curve title points>
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
		<td rowspan="${points?size+1}" align="center"><@svg points/></td>
	</tr>

	<@reset/>

	<#list points as point>
		<tr class="<@d/>">
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
		<td rowspan="${curve.point?size+2}" align="center"><@svg curve.point/></td>
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
		<tr class="<@d/>">
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
		<td colspan="4" rowspan="${curve.point?size+2}" align="center"><@svg curve.point/></td>
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
		<tr class="<@d/>">
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