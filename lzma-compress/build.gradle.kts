import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner

dependencies {
    implementation(project(":mz32-Downloader"))
}

plugins {
    application
    shadow
    launch4j
}

application {
    mainClassName = "de.treichels.hott.lzma.LzmaCompressKt"
}

launch4j {
    val shadowJar = project.tasks.shadowJar.get()
    val gitVersioner =rootProject.the<GitVersioner>()

    mainClassName = application.mainClassName
    copyConfigurable = shadowJar.outputs.files
    jar = "lib/${shadowJar.archiveName}"
    icon = "$projectDir/icon.ico"
    version =  "${project.version}.${gitVersioner.versionCode}"
    textVersion = "${project.version}.${gitVersioner.versionName}"
    outfile = "${project.name}-$version.exe"
    copyright = "GPLv3"
}
