import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner

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
    launch4j
}

application {
    mainClassName = "de.treichels.hott.mz32.Mz32DownloaderKt"
}

val gitVersioner = rootProject.the<GitVersioner>()

tasks {
    shadowJar {
        version = "${project.version}.${gitVersioner.versionCode}"
    }
}

launch4j {
    val shadowJar = project.tasks.shadowJar.get()

    mainClassName = application.mainClassName
    copyConfigurable = shadowJar.outputs.files
    jar = "lib/${shadowJar.archiveName}"
    icon = "$projectDir/icon.ico"
    version = "${project.version}.${gitVersioner.versionCode}"
    textVersion = "${project.version}.${gitVersioner.versionName}"
    outfile = "${project.name}-$version.exe"
    copyright = "GPLv3"
}
