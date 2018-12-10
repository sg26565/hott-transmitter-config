import com.google.gradle.osdetector.OsDetector
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.org_jetbrains_kotlin apply false
    mavenPublish
    osDetector
    syncLibs
}

val os = OsDetector().os as String
val platform = when (os) {
    "osx" -> "mac"
    "windows" -> "win"
    else -> os
}

subprojects {
    group = "de.treichels.hott"
    version = "0.9.4"

    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    // set jvmTarget for Kotlin
    tasks.withType(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
    }

    // enable reproducible builds
    tasks.withType(AbstractArchiveTask::class) {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    dependencies {
        // common dependencies
        "compile"(Libs.kotlin_stdlib_jdk8)
        "testCompile"(Libs.junit)
        "testCompile"(Libs.kotlin_test_junit)

        // dependency management
        @Suppress("UnstableApiUsage")
        constraints {
            add("compile", "org.jetbrains.kotlin:kotlin-reflect:1.3.10")
        }
    }

    // add platform dependencies for JavaFX
    configurations.all {
        dependencies.all {
            if (group == "org.openjfx") {
                dependencies.add(project.dependencies.create(group = group!!, name = name, version = version, classifier = platform))
            }
        }
    }
}
