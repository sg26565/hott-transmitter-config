dependencies {
    implementation(project(":HoTT-Decoder"))
    implementation(project(":HoTT-UI"))
    implementation(Libs.commons_lang3)
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.memorydump.MemoryDumpKt"
}
