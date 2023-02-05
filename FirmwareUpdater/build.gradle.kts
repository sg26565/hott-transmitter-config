dependencies {
    implementation(fileTree(File(rootProject.projectDir, "libs")) { include("*.jar") })
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.update.FirmwareUpdaterKt"
}
