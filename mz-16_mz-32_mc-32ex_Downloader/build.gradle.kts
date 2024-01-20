dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":lzma-sdk"))
}

plugins {
    application
}

application {
    mainClass.set("de.treichels.hott.mz32.Mz32DownloaderKt")
}
