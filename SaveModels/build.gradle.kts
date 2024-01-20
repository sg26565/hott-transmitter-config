dependencies {
    val libs = File(rootProject.projectDir, "libs").listFiles { file -> file.isFile && file.extension == "jar" }
    implementation(files(libs))
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
    id("org.openjfx.javafxplugin")
}

javafx {
    modules = listOf("javafx.controls","javafx.fxml","javafx.web")
}

dependencies {
    implementation("no.tornado:tornadofx:_")
}

application {
    mainClass.set("de.treichels.hott.SaveModels")
}
