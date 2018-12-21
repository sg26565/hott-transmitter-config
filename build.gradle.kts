import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.gradle.osdetector.OsDetector
import com.pascalwelsch.gitversioner.GitVersioner
import edu.sc.seis.launch4j.Launch4jPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("com.pascalwelsch.gitversioner:gitversioner:0.4.1")
    }
}

plugins {
    java
    kotlin("jvm") version Versions.org_jetbrains_kotlin apply false
    mavenPublish
    osDetector
    syncLibs
    shadow apply false
    launch4j apply false
}

apply(plugin = "com.pascalwelsch.gitversioner")

val gitVersioner = the<GitVersioner>().apply {
    yearFactor = 0
    addLocalChangesDetails = false
}

val os = OsDetector().os
val platform = when (os) {
    "osx" -> "mac"
    "windows" -> "win"
    else -> os
}

subprojects {
    group = "de.treichels.hott"
    version = "0.9.4"

    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
    }

    tasks {
        jar {
            // store version numbers in jar
            manifest {
                attributes(
                        "Implementation-Version" to version,
                        "Implementation-Build" to gitVersioner.versionName
                )
            }

            // enable reproducible builds
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            version = "${project.version}.${gitVersioner.versionCode}"
        }

        // set jvmTarget for Kotlin
        withType(KotlinCompile::class) {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    dependencies {
        // common dependencies
        "implementation"(Libs.kotlin_stdlib_jdk8)
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

    // publish to Bintray
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }

        repositories {
            maven {
                name = "Bintray"
                url = uri("https://api.bintray.com/maven/sg26565/maven/mdlviewer/;publish=1")
                credentials {
                    username = properties["bintray.user.name"] as String?
                    password = properties["bintray.user.key"] as String?
                }
            }
        }
    }

    afterEvaluate {
        if (this.plugins.hasPlugin("application")) {
            // apply the shadow and launch4j plugins
            apply(plugin = "com.github.johnrengelman.shadow")
            apply(plugin = "edu.sc.seis.launch4j")

            val gitVersioner = rootProject.the<GitVersioner>()
            val shadowJar = tasks.named<ShadowJar>("shadowJar").get()

            shadowJar.version = "${project.version}.${gitVersioner.versionCode}"

            configure<Launch4jPluginExtension> {
                copyConfigurable = shadowJar.outputs.files
                jar = "lib/${shadowJar.archiveName}"
                icon = "$projectDir/icon.ico"
                version = "${project.version}.${gitVersioner.versionCode}"
                textVersion = "${project.version}.${gitVersioner.versionName}"
                outfile = "${project.name}-$version.exe"
                copyright = "GPLv3"

                val splashFile = File(projectDir, "splash.bmp")
                if (splashFile.exists() && splashFile.canRead()) {
                    splashFileName = splashFile.path
                }
            }
        }
    }
}
