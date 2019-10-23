dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Serial"))
    implementation(Libs.commons_math3)
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    eclipse
    application
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.swt.LauncherKt"
}

eclipseMavenCentral {
    release("4.13.0") {
        implementation("org.eclipse.swt")
        useNativesForRunningPlatform()
    }
}
