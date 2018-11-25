dependencies {
    compile(project(":HoTT-Report-HTML"))
    compile(project(":HoTT-Report-XML"))
    compile(project(":HoTT-Report-PDF"))
    compile(Libs.commons_math3)
    compile(Libs.javafx_swing)
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.javafx.MainKt"
}