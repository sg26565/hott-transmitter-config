<#if model.transmitterType.name() != "mx20">
	<table>
		<caption><a name="mp3Player"/>MP3-Player</caption>
		
		<@reset/>
	
		<tbody>
			<tr class="<@d/>">
				<th align="center">Lautstärke</th>
				<td align="left">${model.mp3Player.volume}</td>
			</tr>
			<tr class="<@d/>">
				<th align="center">Album</th>
				<td align="left">${model.mp3Player.album}</td>
			</tr>
			<tr class="<@d/>">
				<th align="center">Titel</th>
				<td align="left">${model.mp3Player.track}</td>
			</tr>
			<tr class="<@d/>">
				<th align="center">Modus</th>
				<td align="left">${model.mp3Player.mode}</td>
			</tr>
			<#if model.getSwitch("Volume")??>
			<tr class="<@d/>">
				<th align="center">Lautstärke</th>
				<td align="left">${model.mp3Player.volume}</td>
			</tr>
			<tr class="<@d/>">
				<th align="center">Regler Lautstärke</th>
				<td align="left"><@switch model.getSwitch("Volume")/></td>
			</tr>
			<tr class="<@d/>">
				<th align="center">Schalter lauter</th>
				<td align="left"><@switch model.getSwitch("VolumeLeft")/></td>
			</tr>
			<tr class="<@d/>">
				<th align="center">Schalter leiser</th>
				<td align="left"><@switch model.getSwitch("VolumeRight")/></td>
			</tr>
			<tr class="<@d/>">
				<th align="center">Schalter Start/Stop</th>
				<td align="left"><@switch model.getSwitch("PlayPause")/></td>
			</tr>
			</#if>
		</tbody>
	</table>
</#if>