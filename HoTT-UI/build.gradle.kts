import com.google.gradle.osdetector.OsDetector

plugins {
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
    api("org.openjfx:javafx-base:_")
    api("org.openjfx:javafx-base:_:$os_platform")
    api("org.openjfx:javafx-graphics:_")
    api("org.openjfx:javafx-graphics:_:$os_platform")
    api("org.openjfx:javafx-controls:_")
    api("org.openjfx:javafx-controls:_:$os_platform")
    api("org.openjfx:javafx-web:_")
    api("org.openjfx:javafx-web:_:$os_platform")
    api("org.openjfx:javafx-media:_")
    api("org.openjfx:javafx-media:_:$os_platform")
    api("no.tornado:tornadofx:_")
    api(project(":HoTT-Util"))
}
