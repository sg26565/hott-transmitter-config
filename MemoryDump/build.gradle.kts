dependencies {
    compile(project(":HoTT-Decoder:HoTT-Decoder"))
    compile(Libs.commons_lang3)
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.memorydump.MemoryDumpKt"
}
