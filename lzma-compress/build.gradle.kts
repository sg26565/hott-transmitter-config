dependencies {
    implementation(project(":mz-16_mz-32_mc-32ex_Downloader"))
    implementation(project(":HoTT-UI"))
    implementation(project(":lzma-sdk"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
}

plugins {
    application
}

application {
    mainClass.set("de.treichels.hott.lzma.LzmaCompressKt")
}
