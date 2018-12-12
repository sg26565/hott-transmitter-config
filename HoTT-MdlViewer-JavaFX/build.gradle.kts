import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(Libs.commons_math3)
    implementation(Libs.javafx_swing)
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.javafx.MainKt"
}

tasks.withType(ShadowJar::class) {
    minimize()
}