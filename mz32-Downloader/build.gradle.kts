dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Decoder"))
    implementation(project(":lzma-sdk"))
}

plugins {
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.mz32.Mz32DownloaderKt"
}
