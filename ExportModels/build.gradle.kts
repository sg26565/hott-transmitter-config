dependencies {
    compile(project(":HoTT-Decoder:HoTT-Decoder"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.ExportModelsKt"
}
