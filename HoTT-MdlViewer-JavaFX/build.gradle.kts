import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner

dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(Libs.commons_math3)
    implementation(Libs.javafx_swing)
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
    shadow
    launch4j
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.javafx.MainKt"
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

