<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<#include "macros.ftl"/>
	<head>
		<title>${name} - ${title}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1"/>
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
		
		<style type="text/css" media="screen">
			body {
				font-size: 14pt;
				padding: 0;
				margin-top: 0;
			}
			
			div.navigation {
				position: fixed;
				font-size: 10pt;
				padding: 0;
				margin: 0;
			}
		
			.navigation a[href] {
				display: block;
			}
			
			.navigation h1 {
				font-size: 14pt;
				margin-top: 0;
			}
			
			div.data {
				margin-left: 10em;
			}
			
			.navigation a.i1 {
				margin-left: 1em;
			}
		</style>
		
		<style type="text/css" media="(max-width: 80em)">
			body {
				font-size: 9pt;
				padding: 0;
				margin-top: 0;
			}
		
			div.navigation {
				display: none;
			}
		
			div.data {
				margin-left: 0em;
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
			}
			
			tr.d0 td {
				background-color: #eee;
			}
			
			tr.d1 td {
				background-color: #ddd;
			}
			
			th.d2 {
				background-color: #bbb;
				font-size: large;
				text-align: center;
				page-break-before: auto;
			}
			
			tr.d1 td.d0 {
				background-color: #eee;
			}
			
			.unused {
				color: #aaa;
				font-style: italic;
			}
		</style>
	</head>
	<body>
		<table>
			<caption>Voice Data File</caption>

			<tr>
				<th>VDF Typ</th>
				<td>${voicefile.vdfType}</td>
			</tr>
			<tr>
				<th>VDF Version</th>
				<td>v${(voicefile.vdfVersion/1000)?string["0.0"]}</td>
			</tr>
			<tr>
				<th>Sendertyp</th>
				<td>${voicefile.transmitterType}</td>
			</tr>
			<tr>
				<th>L&auml;nderkennung</th>
				<td>${voicefile.country}</td>
			</tr>
			<tr>
				<th>Ansagen</th>
				<td>${voicefile.voiceCount}</td>
			</tr>
			<tr>
				<th>Gesamtgr&ouml;&szlig;e</th>
				<td>${(voicefile.dataSize / 1024)?round} kb</td>
			</tr>
		</table>
					
		<table>
			<caption>Ansagen</caption>
			
			<thead>
				<tr>
					<th>Position</th>
					<th>Name</th>
					<th>Gr&ouml;&szlig;e</th>
				</tr>
			</thead>
			
			<@reset/>

			<tbody><#list voicefile.voiceData as data>
				<tr class="<@d/>">
					<td>${data_index + 1}</td>
					<td>${data.name}</td>
					<td>${data.rawData?size}</td>
				</tr></#list>
			</tbody>		
		</table>
	</body>
</html>