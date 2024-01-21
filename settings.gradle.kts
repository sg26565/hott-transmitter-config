import de.fayard.refreshVersions.core.StabilityLevel

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.3"
}

refreshVersions {
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}

rootProject.name = "hott-transmitter-config"

include(":ExportModels")
include(":FirmwareUpdater")
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
include(":mz-16_mz-32_mc-32ex_Downloader")
include(":Test")
include(":VDFEditor")
include(":SaveModels")
