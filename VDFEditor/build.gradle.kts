dependencies {
    implementation(project(":HoTT-Decoder"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Report-XML"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.vdfeditor.ui.VDFEditorKt"
}
