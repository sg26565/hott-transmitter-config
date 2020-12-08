dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-Decoder"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.update.FirmwareUpdaterKt"
}
