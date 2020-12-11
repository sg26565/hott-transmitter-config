dependencies {
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Decoder"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Voice"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Util"))
    implementation(project(":FirmwareUpdater"))
    implementation("commons-net:commons-net:_")
    implementation("org.apache.commons:commons-math3:_")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    runtimeOnly("com.fazecast:jSerialComm:_")
    runtimeOnly("org.freemarker:freemarker:_")
    runtimeOnly(project(":jSerialCommPort"))
}
