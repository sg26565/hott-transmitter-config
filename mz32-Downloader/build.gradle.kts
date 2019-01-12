dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-UI"))
    implementation(project(":HoTT-Decoder"))
    compile(project(":lzma-sdk"))
    runtimeOnly(project(":Java8ComboBoxListViewSkin"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.mz32.Mz32DownloaderKt"
}
