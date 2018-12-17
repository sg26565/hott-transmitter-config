import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":mz32-Downloader"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.lzma.LzmaCompressKt"
}
