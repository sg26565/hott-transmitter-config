@REM Script to build Windows install packages using jpackage of Java 17
@IF "%~1"=="" (
	@echo usage: CreateInstallerMsi "0.9.4.1401"
	@GOTO end 
)
SET VERSION=%~1

del FirmwareUpdater\build\libs\FirmwareUpdater-%VERSION%.jar
jpackage --input FirmwareUpdater\build\libs --dest ..\release --name FirmwareUpdater --main-jar FirmwareUpdater.jar --main-class "de.treichels.hott.update.FirmwareUpdaterKt" --icon FirmwareUpdater\icon.ico --app-version %VERSION% --type msi --win-menu --win-menu-group "Graupner HoTT Tools" --win-shortcut --win-shortcut-prompt --vendor "Graupner" --win-dir-chooser
ren ..\release\FirmwareUpdater-%VERSION%.msi FirmwareUpdater.msi

del mz-16_mz-32_mc-32ex_Downloader\build\libs\mz-16_mz-32_mc-32ex_Downloader-%VERSION%.jar
jpackage --input mz-16_mz-32_mc-32ex_Downloader\build\libs --dest ..\release --name "mz-16_mz-32_mc-32ex_Downloader" --main-jar mz-16_mz-32_mc-32ex_Downloader.jar --main-class "de.treichels.hott.mz32.Mz32DownloaderKt" --icon mz-16_mz-32_mc-32ex_Downloader\icon.ico --app-version %VERSION% --type msi --win-menu --win-menu-group "Graupner HoTT Tools" --win-shortcut --win-shortcut-prompt --vendor "Graupner" --win-dir-chooser
ren ..\release\mz-16_mz-32_mc-32ex_Downloader-%VERSION%.msi mz-16_mz-32_mc-32ex_Downloader.msi

del HoTT-TTS\build\libs\HoTT-TTS-%VERSION%.jar
jpackage --input HoTT-TTS\build\libs --dest ..\release --name "HoTT-TTS" --main-jar HoTT-TTS.jar --main-class "de.treichels.hott.tts.SpeechDialogKt" --icon HoTT-TTS\icon.ico --app-version %VERSION% --type msi --win-menu --win-menu-group "Graupner HoTT Tools" --win-shortcut --win-shortcut-prompt --vendor "Graupner" --win-dir-chooser
ren ..\release\HoTT-TTS-%VERSION%.msi HoTT-TTS.msi

del VDFEditor\build\libs\VDFEditor-%VERSION%.jar
jpackage --input VDFEditor\build\libs --dest ..\release --name "VDFEditor" --main-jar VDFEditor.jar --main-class "de.treichels.hott.vdfeditor.ui.VDFEditorKt" --icon VDFEditor\icon.ico --app-version %VERSION% --type msi --win-menu --win-menu-group "Graupner HoTT Tools" --win-shortcut --win-shortcut-prompt --vendor "Graupner" --win-dir-chooser
ren ..\release\VDFEditor-%VERSION%.msi VDFEditor.msi

del MDLViewer\build\libs\MDLViewer-%VERSION%.jar
jpackage --input MDLViewer\build\libs --dest ..\release --name "MDLViewer" --main-jar MDLViewer.jar --main-class "de.treichels.hott.mdlviewer.javafx.MdlViewerKt" --icon MDLViewer\icon.ico --app-version %VERSION% --type msi --win-menu --win-menu-group "Graupner HoTT Tools" --win-shortcut --win-shortcut-prompt --vendor "Graupner" --win-dir-chooser
ren ..\release\MDLViewer-%VERSION%.msi MDLViewer.msi

del lzma-compress\build\libs\lzma-compress-%VERSION%.jar
jpackage --input lzma-compress\build\libs --dest ..\release --name "lzma-compress" --main-jar lzma-compress.jar --main-class "de.treichels.hott.lzma.LzmaCompressKt" --icon lzma-compress\icon.ico --app-version %VERSION% --type msi --win-menu --win-menu-group "Graupner HoTT Tools" --win-shortcut --win-shortcut-prompt --vendor "Graupner" --win-dir-chooser
ren ..\release\lzma-compress-%VERSION%.msi lzma-compress.msi

:end