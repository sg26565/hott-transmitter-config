dependencies {
    compile(project(":HoTT-Decoder:HoTT-Decoder"))
    compile(project(":HoTT-TTS"))
    compile(project(":HoTT-Report-HTML"))
    compile(project(":HoTT-Report-PDF"))
    compile(project(":HoTT-Report-XML"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.vdfeditor.ui.VDFEditorKt"
}
