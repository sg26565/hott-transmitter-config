import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner

dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-Voice"))
    implementation(Libs.aws_java_sdk_polly)
    implementation(Libs.jackson_databind)
    runtimeOnly(Libs.mp3spi)
    runtimeOnly(Libs.vorbisspi)
}

plugins {
    application
    shadow
    launch4j
}

application {
    mainClassName = "de.treichels.hott.tts.SpeechDialogKt"
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
