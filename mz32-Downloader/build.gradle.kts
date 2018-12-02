dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-Firmware"))
    compile(project(":lzma-sdk"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.mz32.Mz32DownloaderKt"
}
