import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner

dependencies {
    implementation(project(":HoTT-Decoder:HoTT-Decoder"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Report-XML"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
    shadow
    launch4j
}

application {
    mainClassName = "de.treichels.hott.vdfeditor.ui.VDFEditorKt"
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
