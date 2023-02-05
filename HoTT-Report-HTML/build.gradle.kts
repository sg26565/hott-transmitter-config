dependencies {
    implementation(project(":HoTT-Model"))
    implementation(fileTree(File(rootProject.projectDir, "libs")) { include("*.jar") })
    implementation(project(":HoTT-Voice"))
    implementation("org.freemarker:freemarker:_")
    implementation("commons-io:commons-io:_")
    implementation("no.tornado:tornadofx:_")
}
