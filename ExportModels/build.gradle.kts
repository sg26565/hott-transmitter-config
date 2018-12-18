dependencies {
    implementation(project(":HoTT-Decoder"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.ExportModelsKt"
}
