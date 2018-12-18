import com.diffplug.gradle.eclipse.MavenCentralExtension
import com.diffplug.gradle.pde.EclipseRelease
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner

dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(Libs.commons_math3)
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    eclipse
    application
    shadow
    launch4j
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.swt.LauncherKt"
}

eclipseMavenCentral {
    release("4.9.0") {
        implementation("org.eclipse.swt")
        useNativesForRunningPlatform()
    }
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

