dependencies {
    implementation(project(":HoTT-Decoder", configuration="obfuscated"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.ExportModelsKt"
}
