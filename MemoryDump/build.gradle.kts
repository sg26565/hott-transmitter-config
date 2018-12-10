dependencies {
    compile(project(":HoTT-Decoder:HoTT-Decoder"))
    compile(Libs.commons_lang3)
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.memorydump.MemoryDumpKt"
}
