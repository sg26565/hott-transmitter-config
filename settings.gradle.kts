import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard:dependencies:0.5.7")
}

bootstrapRefreshVersionsAndDependencies()

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
