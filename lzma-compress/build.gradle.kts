dependencies {
    implementation(project(":mz32-Downloader"))
    implementation(project(":HoTT-UI"))
    implementation(project(":lzma-sdk"))
}

plugins {
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.lzma.LzmaCompressKt"
}
