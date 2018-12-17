import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":HoTT-Decoder:HoTT-Decoder"))
    implementation(project(":HoTT-UI"))
    implementation(Libs.commons_lang3)
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.memorydump.MemoryDumpKt"
}
