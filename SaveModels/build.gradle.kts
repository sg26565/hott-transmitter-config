dependencies {
    val libs = File(rootProject.projectDir, "libs").listFiles { file -> file.isFile && file.extension == "jar" }
    implementation(files(libs))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls","javafx.fxml","javafx.web")
}

dependencies {
    implementation("no.tornado:tornadofx:1.7.20")
}

application {
    mainClass.set("de.treichels.hott.SaveModels")
}
