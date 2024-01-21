dependencies {
    implementation("de.treichels.hott:hott-decoder:_")
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClass.set("de.treichels.hott.update.FirmwareUpdaterKt")
}
