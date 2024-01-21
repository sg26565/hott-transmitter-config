dependencies {
    implementation("de.treichels.hott:hott-decoder:_")
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    implementation("org.apache.commons:commons-lang3:_")
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClass.set("de.treichels.hott.memorydump.MemoryDumpKt")
}
