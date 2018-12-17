import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.upgrade.FirmwareUpgraderKt"
}
