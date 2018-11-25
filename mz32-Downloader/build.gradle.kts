dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":lzma-sdk"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.mz32.Mz32DownloaderKt"
}
