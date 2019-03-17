dependencies {
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Decoder"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Voice"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Util"))
    implementation(project(":FirmwareUpdater"))
    runtimeOnly(Libs.jserialcomm)
    implementation(Libs.commons_math3)
    runtimeOnly(Libs.freemarker)
}
