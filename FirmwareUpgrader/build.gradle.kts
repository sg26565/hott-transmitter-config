dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-UI"))
    compile(project(":HoTT-Serial"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.upgrade.FirmwareUpgraderKt"
}
