dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Util"))
    val libs = File(rootProject.projectDir, "libs").listFiles { file -> file.isFile && file.extension == "jar" }
    implementation(files(libs))
    implementation("org.apache.commons:commons-math3:_")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:_")
    runtimeOnly("com.sun.xml.bind:jaxb-impl:_")
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    id("com.diffplug.eclipse.mavencentral")
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
