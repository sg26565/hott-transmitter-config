import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-UI"))
    implementation(project(":HoTT-Firmware"))
    compile(project(":lzma-sdk"))
    runtimeOnly(project(":Java8ComboBoxListViewSkin"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.mz32.Mz32DownloaderKt"
}
