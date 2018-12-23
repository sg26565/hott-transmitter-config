dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-Decoder", configuration="obfuscated"))
    compile(project(":HoTT-Voice"))
    implementation(Libs.freemarker)
}
