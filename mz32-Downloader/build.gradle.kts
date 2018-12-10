dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-UI"))
    compile(project(":HoTT-Firmware"))
    compile(project(":lzma-sdk"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.mz32.Mz32DownloaderKt"
}
