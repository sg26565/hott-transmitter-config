dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.upgrade.FirmwareUpgraderKt"
}
