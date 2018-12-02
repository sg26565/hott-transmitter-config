dependencies {
    compile(project(":HoTT-Model"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.upgrade.FirmwareUpgraderKt"
}
