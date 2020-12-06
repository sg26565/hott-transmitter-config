dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Util"))
    implementation(project(":HoTT-Decoder", configuration="obfuscated"))
    implementation("org.apache.commons:commons-math3:_")
    implementation("javax.xml.bind:jaxb-api:_")
    runtimeOnly("org.glassfish.jaxb:jaxb-runtime:_")
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    id("com.diffplug.gradle.eclipse.mavencentral") version "3.18.1"
    application
}

application {
    @Suppress("DEPRECATION")
    mainClassName = "de.treichels.hott.mdlviewer.swt.LauncherKt"
}

eclipseMavenCentral {
    release("4.13.0") {
        implementation("org.eclipse.swt")
        useNativesForRunningPlatform()
    }
}
