dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    implementation("de.treichels.hott:hott-decoder:_")
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
