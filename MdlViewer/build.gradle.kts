import com.google.gradle.osdetector.OsDetector

plugins {
    application
    id("com.google.osdetector") version "1.6.2"
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
    implementation("org.apache.commons:commons-math3:_")
    implementation("org.openjfx:javafx-swing:_")
    implementation("org.openjfx:javafx-swing:_:" + os_platform)
    runtimeOnly(project(":jSerialCommPort"))
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.javafx.MdlViewerKt"
}
