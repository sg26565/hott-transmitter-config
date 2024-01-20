dependencies {
    implementation(project(":HoTT-Report-HTML"))
    implementation(project(":HoTT-Report-XML"))
    implementation(project(":HoTT-Report-PDF"))
    implementation(project(":HoTT-Serial"))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Util"))
    implementation(fileTree(File(rootProject.projectDir, "libs")) { include("*.jar") })
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
    mainClass.set("de.treichels.hott.mdlviewer.swt.LauncherKt")
}

eclipseMavenCentral {
    silenceEquoIDE()
    release("4.30.0") {
        implementation("org.eclipse.swt")
        useNativesForRunningPlatform()
    }
}
