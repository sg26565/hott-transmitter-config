<table>
	<caption><a name="baseSettings"/>Grundeinstellungen Modell</caption>
	
	#reset
	
	<tbody>
		<tr class="$d">
			<th align="right">Hersteller</th>
			<td align="left" colspan="3">${model.vendor}</td>
		</tr>
		<tr class="$d">
			<th align="right">Sendertyp</th>
			<td align="left" colspan="3">${model.transmitterType}</td>
		</tr>
		<tr class="$d">
			<th align="right">Sender ID</th>
			<td align="left" colspan="3">${hex.toHexString($model.transmitterId)}</td>
		</tr>
		<tr class="$d">
			<th align="right">Firmware Version</th>
			<td align="left" colspan="3">${model.appVersion}</td>
		</tr>
		<tr class="$d">
			<th align="right">Datei Version</th>
			<td align="left" colspan="3">${model.memoryVersion}</td>
		</tr>
		<tr class="$d">
			<th align="right">Modelltyp</th>
			<td align="left" colspan="3">${model.modelType}</td>
		</tr>
		#if ($model.modelNumber > 0)			
			<tr class="$d">
				<th align="right">Modellspeicher</th>
				<td align="left" colspan="3">${model.modelNumber}</td>
			</tr>
		#end
		<tr class="$d">
			<th align="right">Modellname</th>
			<td align="left" colspan="3">${model.modelName}</td>
		</tr>
		<tr class="$d">
			<th align="right">Steueranordnung</th>
			<td align="left" colspan="3">${model.stickMode}</td>
		</tr>
		<tr class="$d">
			<th align="right">Modul</th>
			<td align="left" colspan="3">${model.module.type}</td>
		</tr>
		<tr class="$d">
			<th align="right">DSC-Ausgang</th>
			<td align="left" colspan="3">${model.dscOutputType}</td>
		</tr>
		#if ($model.modelType.name() == "Helicopter")
			<tr class="$d">
				<th align="right">Autorotation</th>
				<td align="left" colspan="3">#switch($model.getSwitch("Autorotation"))</td>
			</tr>	
			<tr class="$d">
				<th align="right">Autorotation K1 Position</th>
				<td align="left"><i>Position:</i> ${model.autorotationC1TriggerPosition}%</td>
				<td align="left" colspan="2"><i>Schalter:</i> #switch($model.getSwitch("AutorotationC1"))</td>
			</tr>
		#end
		<tr class="$d">
			<th align="right" valign="top">Motor-Stopp</th>
			<td align="left"><i>Position:</i> ${model.throttleSettings.throttleCutOf.position}%</td>
			<td align="left"><i>Limit:</i> ${model.throttleSettings.throttleCutOf.threshold}%</td>
			<td align="left"><i>Schalter:</i> #switch($model.throttleSettings.throttleCutOf.switch)</td>							
		</tr>
		#if ($model.transmitterType.name() != "mx20")
			#if ($model.modelType.name() == "Helicopter")
				<tr class="$d">
					<th align="right">Markierung</th>
					<td align="left" colspan="3">#switch($model.getSwitch("MarkerKey"))</td>
				</tr>
			#end
			<tr class="$d">
				<th align="right">Einschaltwarnung</th>
				<td align="left" colspan="3">#switch($model.getSwitch("PowerWarning"))</td>
			</tr>
			<tr class="$d">
				<th align="right">Auto Trimm</th>
				<td align="left" colspan="3">#switch($model.getSwitch("AutoTrim"))</td>
			</tr>
			<tr class="$d">
				<th align="right">Auto rücksetzen Uhr</th>
				<td align="left" colspan="3">#yesno($model.autoTimerReset)</td>
			</tr>
		#end
		#if ($model.module.type.name() == "HoTT")
			#foreach ($receiver in $model.receiver)
				<tr class="$d #u($receiver.bound)">
					<th class="d2" colspan="4">Empfänger $math.add($receiver.number,1)</th>
				</tr>
				<tr class="$d #u($receiver.bound)">
					<th align="right">gebunden</th>
					<td align="left" colspan="3">#yesno($receiver.bound)</td>
				</tr>
				#if ($receiver.bound)
					<tr class="$d">
						<th align="right">Telemetrie</th>
						<td align="left" colspan="3">#yesno($receiver.telemetry)</td>
					</tr>
					<tr class="$d">
						<th align="right">Empfänger ID</th>
						<td align="left" colspan="3">${hex.toHexString($receiver.rfid)}</td>
					</tr>
					<tr class="$d">
						<th align="right">Empfängerausgang</th>
						<th align="center">Eingang</th>
						<th/>
						<th align="center">Ausgang</th>
					</tr>
					#foreach ($mapping in $receiver.channelMapping)
					<tr class="$d">
						<th/>
						<td align="center">S$math.add($mapping.inputChannel,1)#if($model.channel[$mapping.inputChannel].function!=$null) (${model.channel[$mapping.inputChannel].function})#end</td>
						<td align="center">&rarr;</td>
						<td align="center">Ausgang $math.add($mapping.outputChannel,1)</td>
					</tr>
					#end
				#end
			#end
		#end
	</tbody>
</table>
