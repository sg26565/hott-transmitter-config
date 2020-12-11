dependencies {
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-HTML"))
    val libs = File(rootProject.projectDir, "libs").listFiles { file -> file.isFile && file.extension == "jar" }
    implementation(files(libs))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Voice"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Util"))
    implementation(project(":FirmwareUpdater"))
    implementation("commons-net:commons-net:_")
    implementation("org.apache.commons:commons-math3:_")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
    runtimeOnly("com.fazecast:jSerialComm:_")
    runtimeOnly("org.freemarker:freemarker:_")
    runtimeOnly(project(":jSerialCommPort"))
}
