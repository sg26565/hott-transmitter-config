dependencies {
    implementation(project(":mz32-Downloader"))
    implementation(project(":HoTT-UI"))
    implementation(project(":lzma-sdk"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}

plugins {
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.lzma.LzmaCompressKt"
}
