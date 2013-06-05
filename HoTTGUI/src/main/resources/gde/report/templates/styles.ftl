<style type="text/css" media="print">
	@page {
		size: a4;
		
		@top-left {
			content: "${model.transmitterType} - ${model.modelName}";
		}
	
		@top-center {
			content: "erzeugt am ${.now}";
		}
	
		@top-right {
			content: "Seite " counter(page);
		}
	
		@bottom-left {
			content: "${model.transmitterType} - ${model.modelName}";
		}
	
		@bottom-center {
			content: "erzeugt am ${.now}";
		}
	
		@bottom-right {
			content: "Seite " counter(page);
		}
	}
	
	body {
		font-size: 9pt;
	}
	
	#nav {
		visibility: hidden;
	}
</style>

<style type="text/css" media="screen">
	body {
		font-size: 14pt;
		min-width: 20 em;
	}
	
	#nav {
		position: fixed;
		font-size: 10pt;
		padding: 0;
		margin: 0;
	}
	
	#content {
		margin-left: 12em;
	}
	
	#nav li {
    	list-style: none;
    	margin: 0;
    	padding: 0;
  	}
</style>

<style type="text/css" media="all">
	@font-face {
		src: url('${programDir}/fonts/Arial.ttf');
		-fs-pdf-font-embed: embed;
		-fs-pdf-font-encoding: Identity-H;
	}

	svg {
		display: block;
	}

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
		margin: 0px;
		padding: 0px;
		white-space: nowrap;
		page-break-inside: avoid;
	}
	
	table caption {
		text-align: left;
		font-size: x-large;
		font-style: oblique;
		font-weight: bold;
		margin-top: 1em;
	}
	
	td {
		border-left-style: solid;
		border-right-style: solid;
		border-width: 1px;
		border-color: white;
		padding: 2px;
		margin: 0px;
	}
	
	th {
		background-color: #ccc;
		border-left-style: solid;
		border-right-style: solid;
		border-width: 1px;
		border-color: white;
		padding: 2px;
		margin: 0px;
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
	}
	
	tr.d1 td.d0 {
		background-color: #eee;
	}
	
	.unused {
		color: #aaa;
		font-style: italic;
	}
</style>