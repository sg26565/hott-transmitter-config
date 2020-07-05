dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Decoder", configuration="obfuscated"))
    implementation(project(":HoTT-Voice"))
    implementation("org.freemarker:freemarker:_")
    implementation("commons-io:commons-io:_")
}
