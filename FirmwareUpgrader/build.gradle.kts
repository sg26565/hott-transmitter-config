dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-Serial"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.upgrade.FirmwareUpgraderKt"
}
