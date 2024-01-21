import com.google.gradle.osdetector.OsDetector

plugins {
    application
    id("com.google.osdetector")
}

val os = osdetector.os
val os_platform = when (os) {
    "osx" -> "mac"
    "windows" -> "win"
    "linux" -> "linux"
    else -> throw UnsupportedOperationException("os $os is not supported")
}

dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Voice"))
    implementation("de.treichels.hott:hott-decoder:_")
    implementation("commons-io:commons-io:_")
    implementation("org.apache.commons:commons-math3:_")
    implementation("org.openjfx:javafx-swing:_")
    implementation("org.openjfx:javafx-swing:_:$os_platform")
    runtimeOnly(project(":jSerialCommPort"))
    runtimeOnly("com.sun.xml.bind:jaxb-impl:_")
}

application {
    mainClass.set("de.treichels.hott.mdlviewer.javafx.MdlViewerKt")
}
