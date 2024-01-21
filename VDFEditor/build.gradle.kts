dependencies {
    implementation("de.treichels.hott:hott-decoder:_")
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-TTS"))
    implementation(project(":HoTT-Voice"))
    implementation(project(":HoTT-Model"))
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
    mainClass.set("de.treichels.hott.vdfeditor.ui.VDFEditorKt")
}
