import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compile(project(":HoTT-Decoder:HoTT-Decoder"))
    compile(project(":HoTT-UI"))
    compile(project(":HoTT-TTS"))
    compile(project(":HoTT-Report-HTML"))
    compile(project(":HoTT-Report-PDF"))
    compile(project(":HoTT-Report-XML"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.vdfeditor.ui.VDFEditorKt"
}

tasks.withType(ShadowJar::class) {
    minimize()
}