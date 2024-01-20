dependencies {
    implementation(fileTree(File(rootProject.projectDir, "libs")) { include("*.jar") })
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
