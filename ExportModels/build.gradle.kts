dependencies {
    implementation(project(":HoTT-Decoder"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.ExportModelsKt"
}
