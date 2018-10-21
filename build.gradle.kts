import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.treichels.hott"
version = "0.9.4"

val platform="win"

plugins {
    kotlin("jvm") version "1.2.71"
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "java")
    apply(plugin = "kotlin")

    dependencies {
        implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib")
        implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8")

        constraints {
            implementation(group = "org.openjfx", name = "javafx-base", version = "11", classifier = platform)
            implementation(group = "org.openjfx", name = "javafx-graphics", version = "11", classifier = platform)
            implementation(group = "org.openjfx", name = "javafx-controls", version = "11", classifier = platform)
            implementation(group = "org.openjfx", name = "javafx-media", version = "11", classifier = platform)
            implementation(group = "org.openjfx", name = "javafx-web", version = "11", classifier = platform)

            implementation(group = "no.tornado", name = "tornadofx", version = "1.7.17")

            implementation(group = "javax.xml.bind", name = "jaxb-api", version = "2.3.0")
            implementation(group = "org.apache.httpcomponents", name = "fluent-hc", version = "4.5.5")
            implementation(group = "commons-io", name = "commons-io", version = "2.5")

            testImplementation(group = "junit", name = "junit", version = "4.12")
        }
    }

    val compileKotlin: KotlinCompile by tasks
    compileKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }

    val compileTestKotlin: KotlinCompile by tasks
    compileTestKotlin.kotlinOptions {
        jvmTarget = "1.8"
    }
}