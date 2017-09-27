<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<#include "macros.ftl"/>
	<head>
		<title>${name} - ${title}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<style type="text/css" media="print">
			@page {
				size: a4;
				
				@top-left {
					content: "${name}";
				}
			
				@top-right {
					content: "${.now}";
				}
			
				@bottom-left {
					content: "${version}";
				}
			
				@bottom-right {
					content: "Seite " counter(page);
				}
			}
			
			body {
				font-size: 9pt;
			}
			
			div.navigation {
				display: none;
			}
		</style>
				
		<style type="text/css" media="all">	
			body {
				font-family: Arial;
			}
			
			table {
				border-collapse: collapse;
				color: black;
				border-style: solid;
				border-color: black;
				border-width: 2px;
				empty-cells: show;
				margin: 0;
				padding: 0;
				white-space: nowrap;
				page-break-inside: avoid;
				margin-bottom: 1em;
			}
			
			table caption {
				text-align: left;
				font-size: x-large;
				font-style: italic;
				font-weight: bold;
				margin-top: 0;
			}
			
			td {
				border-left-style: solid;
				border-right-style: solid;
				border-width: 1px;
				border-color: white;
				padding: 2px;
				margin: 0;
			}
			
			th {
				background-color: #ccc;
				border-left-style: solid;
				border-right-style: solid;
				border-width: 1px;
				border-color: white;
				padding: 2px;
				margin: 0;
				text-align: center;
				page-break-before: auto;
			}
			
			tr.even td {
				background-color: #eee;
			}
			
			tr.odd td {
				background-color: #ddd;
			}
			
			thead th {
				border-bottom-width: 2px;
				border-bottom-color: black;
				border-bottom-style: solid;
			}
			
			th.d2 {
				text-align: right;
				border-left-width: 2px;
				border-left-color: black;
				padding-right: 0.5em;
			}
		</style>
	</head>
	<body>
		<table>
			<caption>Voice Data File</caption>

			<tr>
				<th class="d2">VDF Typ</th>
				<td>${voicefile.vdfType}</td>
			</tr>
			<tr>
				<th class="d2">VDF Version</th>
				<td>v${(voicefile.vdfVersion/1000)?string["0.0"]}</td>
			</tr>
			<tr>
				<th class="d2">Sendertyp</th>
				<td>${voicefile.transmitterType}</td>
			</tr>
			<tr>
				<th class="d2">L&auml;nderkennung</th>
				<td>${voicefile.country}</td>
			</tr>
			<tr>
				<th class="d2">Ansagen</th>
				<td>${voicefile.voiceCount}</td>
			</tr>
			<tr>
				<th class="d2">Gesamtgr&ouml;&szlig;e</th>
				<td>${(voicefile.dataSize / 1024)?round} kb</td>
			</tr>
		</table>

		<table style="border-width: 0px;">
			<caption>Ansagen</caption>
			
			<colgroup>
				<col width="33%"/>
				<col width="33%"/>
				<col width="33%"/>
			</colgroup>
							
			<tr valign="top">
				<#assign size=(voicefile.voiceData?size / 3)?ceiling>
				<#list voicefile.voiceData?chunk(size) as column><td>
					<table style="width: 100%">
						<colgroup>
							<col width="6em"/>
							<col width="100%"/>
							<col width="8em"/>
						</colgroup>
						
						<thead>
							<tr>
								<th class="d2">Pos.</th>
								<th>Name</th>
								<th>Gr&ouml;&szlig;e</th>
							</tr>
						</thead>
						
						<tbody>
							<#list column as cell><tr class="${cell?item_parity}">
								<th class="d2">${cell?counter + size * column?index}</th>
								<td>${cell.name}</td>
								<td align="right">${cell.rawData?size}</td>
							</tr></#list>
						</tbody>		
					</table>
				</td></#list>
			</tr>
		</table>
	</body>
</html>