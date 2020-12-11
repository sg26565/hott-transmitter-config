dependencies {
    val libs = File(rootProject.projectDir, "libs").listFiles { file -> file.isFile && file.extension == "jar" }
    implementation(files(libs))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Serial"))
    implementation("org.apache.commons:commons-lang3:_")
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.memorydump.MemoryDumpKt"
}
