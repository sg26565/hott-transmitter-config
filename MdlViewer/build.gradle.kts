import com.google.gradle.osdetector.OsDetector

plugins {
    application
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
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-UI"))
    implementation(project(":HoTT-Voice"))
    val libs = File(rootProject.projectDir, "libs").listFiles { file -> file.isFile && file.extension == "jar" }
    implementation(files(libs))
    implementation("commons-io:commons-io:_")
    implementation("org.apache.commons:commons-math3:_")
    implementation("org.openjfx:javafx-swing:_")
    implementation("org.openjfx:javafx-swing:_:$os_platform")
    runtimeOnly(project(":jSerialCommPort"))
    runtimeOnly("com.sun.xml.bind:jaxb-impl:_")
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.mdlviewer.javafx.MdlViewerKt"
}
