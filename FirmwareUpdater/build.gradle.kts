dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Firmware"))
    implementation(project(":HoTT-UI"))
    implementation(Libs.jserialcomm)
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.update.FirmwareUpdaterKt"
}
