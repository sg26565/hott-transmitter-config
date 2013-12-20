#macro (switch $sw)
	#if ($sw.assignment.name() == "Unassigned")
		---
	#elseif ($sw.type.name() == "ToggleSwitch")
		#if ($sw.direction == 1)
			<span style="text-decoration: overline;">${sw.assignment}</span>
		#else
			${sw.assignment}
		#end
	#elseif ($sw.type.name() == "InputControl")
		${sw.assignment}#if ($sw.direction == 0)&rarr;#{else}&larr;#end
	#else
		${sw.assignment}						
	#end
#end

#macro (reset)
	#if($d.current=="d1")
		$d.next
	#end
#end

#set($d=$alternator.auto("d0","d1"))

#macro(u $b)
	#if($b)used#{else}unused#end
#end

#macro(yesno $b)
	#if($b)Ja#{else}Nein#end
#end

#macro(onoff $b)
	#if($b)An#{else}Aus#end
#end

#macro (curve $title $curve)
	#if ($!title != "")
		<tr>
			<th class="d2" colspan="5">${title}</th>
		</tr>
	#end

	<tr class="d0">
		<th align="center">Punkt</th>
		<th align="center">aktiv</th>
		<th align="center">Eingang</th>
		<th align="center">Ausgang</th>
		<td rowspan="$evaluate($curve.point.size()+1)" align="center"><img src="${png.getImageSource($curve,0.5,true)}"/></td>
	</tr>

	#reset

	#foreach ($point in $curve.point)
		<tr class="$d #u($point.enabled)">
			<td align="center">$evaluate($point.number+1)</td>
			<td align="center">#yesno($point.enabled)</td>
			#if ($point.enabled)
				<td align="center">${point.position}%</td>
				<td align="center">${point.value}%</td>
			#else
				<td colspan="2"/>
			#end
		</tr>
	#end
#end

#macro (heliCurve $title $curve)
	<tr>
		<th class="d2" colspan="6">${title}</th>
	</tr>
	
	<tr class="d0">
		<th align="right">Kurve</th>
		<td colspan="4" align="left">#onoff($curve.smoothing)</td>
		<td rowspan="$evaluate($curve.point.size()+2)" align="center"><img src="${png.getImageSource($curve,1.0,true)}"/></td>
	</tr>

	<tr>
		<th/>
		<th align="center">Punkt</th>				
		<th align="center">aktiv</th>
		<th align="center">Eingang</th>
		<th align="center">Ausgang</th>
	</tr>

	#reset

	#foreach ($point in $curve.point)
		<tr class="$d #u($point.enabled)">
			<th/>
			<td align="center">$evaluate($point.number+1)</td>
			<td align="center">#yesno($point.enabled)</td>
			#if ($point.enabled)
				<td align="center">${point.position}%</td>
				<td align="center">${point.value}%</td>
			#else
				<td colspan="2" align="center">---</td>
			#end
		</tr>
	#end
#end

#macro (wingCurve $title $curve)
	<tr>
		<th class="d2" colspan="9">${title}</th>
	</tr>
	
	#reset
	
	<tr class="$d">
		<th align="right">Kurve</th>
		<td colspan="4" align="left">#onoff($curve.smoothing)</td>
		<td colspan="4" rowspan="$evaluate($curve.point.size()+2)" align="center"><img src="${png.getImageSource($curve,1.0,false)}"/></td>
	</tr>

	<tr>
		<th/>
		<th align="center">Punkt</th>				
		<th align="center">aktiv</th>
		<th align="center">Eingang</th>
		<th align="center">Ausgang</th>
	</tr>

	#reset()

	#foreach ($point in $curve.point)
		<tr class="$d #u($point.enabled)">
			<th/>
			<td align="center">$evaluate($point.number+1)</td>
			<td align="center">#yesno($point.enabled)</td>
			#if ($point.enabled)
				<td align="center">${point.position}%</td>
				<td align="center">${point.value}%</td>
			#else
				<td colspan="2" align="center">---</td>
			#end
		</tr>
	#end
#end