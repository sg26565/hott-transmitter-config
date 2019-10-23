import com.google.gradle.osdetector.OsDetector

plugins {
    application
    osDetector
}

val os = OsDetector().os
val os_platform = when (os) {
    "osx" -> "mac"
    "windows" -> "win"
    else -> os
}

dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Serial"))
    implementation(Libs.commons_math3)
    implementation(Libs.javafx_swing)
    implementation(Libs.javafx_swing + ":" + os_platform)
    runtimeOnly(project(":jSerialCommPort"))
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.javafx.MdlViewerKt"
}
