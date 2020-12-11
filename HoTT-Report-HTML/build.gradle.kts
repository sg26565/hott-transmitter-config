dependencies {
    implementation(project(":HoTT-Model"))
    val libs = File(rootProject.projectDir, "libs").listFiles { file -> file.isFile && file.extension == "jar" }
    implementation(files(libs))
    implementation(project(":HoTT-Voice"))
    implementation("org.freemarker:freemarker:_")
    implementation("commons-io:commons-io:_")
    implementation("no.tornado:tornadofx:_")
}
