@REM Script to build self extrracting archive with launch4j Windows tools
@IF "%~1"=="" (
	@echo usage: CreateToolsArchiiveExe	1.0.1404
	@GOTO end 
)
SET VERSION=%~1

cd ..\release
copy FirmwareUpdater.exe "Graupner HoTT Tools/FirmwareUpdater.exe"
copy HoTT-TTS.exe "Graupner HoTT Tools/HoTT-TTS.exe"
copy MdlViewer.exe "Graupner HoTT Tools/MdlViewer.exe"
copy mz-16_mz-32_mc-32ex_Downloader.exe "Graupner HoTT Tools/mz-16_mz-32_mc-32ex_Downloader.exe"
copy VDFEditor.exe "Graupner HoTT Tools/VDFEditor.exe"
cd "Graupner HoTT Tools"
7z a -r -sfx ../"Graupner HoTT Tools "%VERSION%.exe  *
cd ../../hott-transmitter-config
:end