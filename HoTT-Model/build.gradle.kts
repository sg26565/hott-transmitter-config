dependencies {
    implementation(group = "org.openjfx", name = "javafx-base", classifier = "win")
    implementation(group = "org.openjfx", name = "javafx-graphics", classifier = "win")
    implementation(group = "org.openjfx", name = "javafx-controls", classifier = "win")
    implementation(group = "org.openjfx", name = "javafx-media", classifier = "win")
    implementation(group = "org.openjfx", name = "javafx-web", classifier = "win")
    implementation(group = "no.tornado", name = "tornadofx")

    implementation("javax.xml.bind:jaxb-api")
    implementation("org.apache.httpcomponents:fluent-hc")
    implementation("commons-io:commons-io")

    testImplementation("junit:junit")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}