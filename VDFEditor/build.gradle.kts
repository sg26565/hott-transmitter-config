dependencies {
    implementation(project(":HoTT-Decoder", configuration="obfuscated"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Serial"))
    implementation("commons-io:commons-io:_")
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.vdfeditor.ui.VDFEditorKt"
}
