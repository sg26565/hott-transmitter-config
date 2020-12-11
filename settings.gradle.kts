import de.fayard.refreshVersions.bootstrapRefreshVersions

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
    dependencies.classpath("com.pascalwelsch.gitversioner:gitversioner:0.5.0")
}

bootstrapRefreshVersions()

rootProject.name = "hott-transmitter-config"

include(":ExportModels")
include(":FirmwareUpdater")
include(":HoTT-Decoder")
include(":MdlViewer")
include(":MdlViewer-SWT")
include(":HoTT-Model")
include(":HoTT-Report-HTML")
include(":HoTT-Report-PDF")
include(":HoTT-Report-XML")
include(":HoTT-Serial")
include(":HoTT-TTS")
include(":HoTT-UI")
include(":HoTT-Util")
include(":HoTT-Voice")
include("jSerialCommPort")
include(":lzma-compress")
include(":lzma-sdk")
include(":MemoryDump")
include(":mz32-Downloader")
include(":Test")
include(":VDFEditor")
