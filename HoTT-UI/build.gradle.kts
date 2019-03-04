import com.google.gradle.osdetector.OsDetector

plugins {
    osDetector
}

val os = OsDetector().os
val os_platform = when (os) {
    "osx" -> "mac"
    "windows" -> "win"
    else -> os
}

dependencies {
    compile(Libs.javafx_base)
    compile(Libs.javafx_base + ":" + os_platform)
    compile(Libs.javafx_graphics)
    compile(Libs.javafx_graphics + ":" + os_platform)
    compile(Libs.javafx_controls)
    compile(Libs.javafx_controls + ":" + os_platform)
    compile(Libs.javafx_web)
    compile(Libs.javafx_web + ":" + os_platform)
    compile(Libs.javafx_media)
    compile(Libs.javafx_media + ":" + os_platform)
    compile(Libs.tornadofx)
    compile(project(":HoTT-Util"))
}
