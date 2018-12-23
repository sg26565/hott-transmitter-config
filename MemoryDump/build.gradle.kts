dependencies {
    implementation(project(":HoTT-Decoder", configuration="obfuscated"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    implementation(Libs.commons_lang3)
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.memorydump.MemoryDumpKt"
}
