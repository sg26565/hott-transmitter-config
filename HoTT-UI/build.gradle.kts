import com.google.gradle.osdetector.OsDetector

plugins {
    id("com.google.osdetector") version "1.6.2"
}

val os = OsDetector().os
val os_platform = when (os) {
    "osx" -> "mac"
    "windows" -> "win"
    else -> os
}

dependencies {
    compile("org.openjfx:javafx-base:_")
    compile("org.openjfx:javafx-base:_:" + os_platform)
    compile("org.openjfx:javafx-graphics:_")
    compile("org.openjfx:javafx-graphics:_:" + os_platform)
    compile("org.openjfx:javafx-controls:_")
    compile("org.openjfx:javafx-controls:_:" + os_platform)
    compile("org.openjfx:javafx-web:_")
    compile("org.openjfx:javafx-web:_:" + os_platform)
    compile("org.openjfx:javafx-media:_")
    compile("org.openjfx:javafx-media:_:" + os_platform)
    compile("no.tornado:tornadofx:_")
    compile(project(":HoTT-Util"))
}
