dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Firmware"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-Decoder", configuration="obfuscated"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.update.FirmwareUpdaterKt"
}
