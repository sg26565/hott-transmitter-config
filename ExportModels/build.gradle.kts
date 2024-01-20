dependencies {
    implementation(fileTree(File(rootProject.projectDir, "libs")) { include("*.jar") })
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Serial"))
    runtimeOnly(project(":jSerialCommPort"))
}

plugins {
    application
}

application {
    mainClass.set("de.treichels.hott.ExportModelsKt")
}
