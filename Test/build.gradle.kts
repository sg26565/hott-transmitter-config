dependencies {
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Decoder"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Voice"))
    implementation(project(":HoTT-UI"))
    implementation(project(":FirmwareUpdater"))
    runtimeOnly(Libs.jserialcomm)
    implementation(Libs.commons_math3)
    runtimeOnly(Libs.freemarker)
}
