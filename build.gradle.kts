import com.google.gradle.osdetector.OsDetector
import org.jetbrains.kotlin.backend.common.bridges.findInterfaceImplementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        "classpath"(Libs.osdetector_gradle_plugin)
        "classpath"(Libs.jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin)
    }
}

plugins {
    kotlin("jvm") version Versions.org_jetbrains_kotlin_jvm_gradle_plugin apply false
}

apply(plugin = "com.google.osdetector")
apply(plugin = "jmfayard.github.io.gradle-kotlin-dsl-libs")

val os: String = (extensions["osdetector"] as OsDetector).os
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

    val compileKotlin: KotlinCompile by tasks
    compileKotlin.kotlinOptions.jvmTarget = "1.8"

    val compileTestKotlin: KotlinCompile by tasks
    compileTestKotlin.kotlinOptions.jvmTarget = "1.8"

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
