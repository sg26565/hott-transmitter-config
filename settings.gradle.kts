pluginManagement {
    plugins {
        id("de.fayard.refreshVersions") version "0.60.3"
        kotlin("jvm") version "1.9.22"
        id("com.github.johnrengelman.shadow") version "8.1.1"
        id("edu.sc.seis.launch4j") version "3.0.1" // versions 3.0.2 through 3.0.5 do not work
        id("com.google.osdetector") version "1.7.3"
        id("org.openjfx.javafxplugin") version "0.1.0"
        id("com.diffplug.eclipse.mavencentral") version "3.44.0"
    }
}

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions")
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
