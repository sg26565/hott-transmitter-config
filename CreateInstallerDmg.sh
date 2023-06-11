#!/bin/bash
#script to create application image, code sign it, package into dmg
if [ -z "$1" ]
  then
    echo usage: ./CreateInstallerDmg.sh "0.9.4.1401"
    exit -1
fi
VERSION=$1
#use commit count as plist version, since it supports only 1.1.1
COMMITCOUNT=$(echo $VERSION| cut -d'.' -f 4)

rm -f FirmwareUpdater\build\libs\FirmwareUpdater-%VERSION%.jar
jpackage --input FirmwareUpdater/build/libs --dest ../release --name FirmwareUpdater --main-jar FirmwareUpdater.jar --main-class "de.treichels.hott.update.FirmwareUpdaterKt" --icon FirmwareUpdater/icon.icns --app-version ${COMMITCOUNT} --type app-image --mac-package-identifier "de.treichels.hott.update.FirmwareUpdater" --mac-package-name FirmwareUpdater
codesign -f -s D9D82CX2Z9 --timestamp --verbose ../release/FirmwareUpdater.app
jpackage --name FirmwareUpdater --app-image ../release/FirmwareUpdater.app --type dmg --dest ../release --app-version ${VERSION}
rm -rf ../release/FirmwareUpdater.app

rm -f mz-16_mz-32_mc-32ex_Downloader\build\libs\mz-16_mz-32_mc-32ex_Downloader-%VERSION%.jar
jpackage --input mz-16_mz-32_mc-32ex_Downloader/build/libs --dest ../release --name "mz-16_mz-32_mc-32ex_Downloader" --main-jar mz-16_mz-32_mc-32ex_Downloader.jar --main-class "de.treichels.hott.mz32.Mz32DownloaderKt" --icon mz-16_mz-32_mc-32ex_Downloader/icon.icns --app-version ${COMMITCOUNT} --type app-image --mac-package-identifier "de.treichels.hott.mz32.Mz32DownloaderKt" --mac-package-name mz-16_mz-32_mc-32ex_Downloader
codesign -f -s D9D82CX2Z9 --timestamp --verbose ../release/mz-16_mz-32_mc-32ex_Downloader.app
jpackage --name mz-16_mz-32_mc-32ex_Downloader --app-image ../release/mz-16_mz-32_mc-32ex_Downloader.app --type dmg --dest ../release --app-version ${VERSION}
rm -rf ../release/mz-16_mz-32_mc-32ex_Downloader.app

rm -f HoTT-TTS\build\libs\HoTT-TTS-%VERSION%.jar
jpackage --input HoTT-TTS/build/libs --dest ../release --name "HoTT-TTS" --main-jar HoTT-TTS.jar --main-class "de.treichels.hott.tts.SpeechDialogKt" --icon HoTT-TTS/icon.icns --app-version ${COMMITCOUNT} --type app-image --mac-package-identifier "de.treichels.hott.tts.SpeechDialogKt" --mac-package-name HoTT-TTS
codesign -f -s D9D82CX2Z9 --timestamp --verbose ../release/HoTT-TTS.app
jpackage --name HoTT-TTS --app-image ../release/HoTT-TTS.app --type dmg --dest ../release --app-version ${VERSION}
rm -rf ../release/HoTT-TTS.app

rm -f VDFEditor\build\libs\VDFEditor-%VERSION%.jar
jpackage --input VDFEditor/build/libs --dest ../release --name "VDFEditor" --main-jar VDFEditor.jar --main-class "de.treichels.hott.vdfeditor.ui.VDFEditorKt" --icon VDFEditor/icon.icns --app-version ${COMMITCOUNT} --type app-image --mac-package-identifier "de.treichels.hott.vdfeditor.ui.VDFEditorKt" --mac-package-name VDFEditor
codesign -f -s D9D82CX2Z9 --timestamp --verbose ../release/VDFEditor.app
jpackage --name VDFEditor --app-image ../release/VDFEditor.app --type dmg --dest ../release --app-version ${VERSION}
rm -rf ../release/VDFEditor.app

rm -f MDLViewer\build\libs\MDLViewer-%VERSION%.jar
jpackage --input MDLViewer/build/libs --dest ../release --name "MDLViewer" --main-jar MDLViewer.jar --main-class "de.treichels.hott.mdlviewer.javafx.MdlViewerKt" --icon MDLViewer/icon.icns --app-version ${COMMITCOUNT} --type app-image --mac-package-identifier "de.treichels.hott.mdlviewer.javafx.MdlViewerKt" --mac-package-name MDLViewer
codesign -f -s D9D82CX2Z9 --timestamp --verbose ../release/MDLViewer.app
jpackage --name MDLViewer --app-image ../release/MDLViewer.app --type dmg --dest ../release --app-version ${VERSION}
rm -rf ../release/MDLViewer.app
