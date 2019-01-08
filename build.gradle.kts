import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.gradle.osdetector.OsDetector
import com.pascalwelsch.gitversioner.GitVersioner
import edu.sc.seis.launch4j.Launch4jPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath(Libs.gitversioner)
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

val versionCode by extra(gitVersioner.versionCode)
val versionName by extra(gitVersioner.versionName)
val baseVersion = "0.9.4"
val shortVersion = "$baseVersion.$versionCode"
val longVersion = "$baseVersion.$versionName"

subprojects {
    group = "de.treichels.hott"
    version = baseVersion

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
                        "Implementation-Build" to versionName
                )
            }

            // enable reproducible builds
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            version = shortVersion
        }

        // set jvmTarget for Kotlin (covers compileKotlin and compileTestKotlin tasks)
        withType(KotlinCompile::class) {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.ExperimentalUnsignedTypes")
        }
    }

    dependencies {
        // common dependencies
        implementation(Libs.kotlin_stdlib_jdk8)
        testImplementation(Libs.junit)
        testImplementation(Libs.kotlin_test_junit)

        // dependency management
        @Suppress("UnstableApiUsage")
        constraints {
            add("compile", "org.jetbrains.kotlin:kotlin-reflect:${Versions.org_jetbrains_kotlin}")
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

    // add shadow and launch4j to any application project
    afterEvaluate {
        if (this.plugins.hasPlugin("application")) {
            // apply the shadow and launch4j plugins
            apply(plugin = "com.github.johnrengelman.shadow")
            apply(plugin = "edu.sc.seis.launch4j")

            // configure shadowJar Task
            val shadowJar by tasks.existing(ShadowJar::class) {
                version = shortVersion
            }

            // configure createExe task
            configure<Launch4jPluginExtension> {
                jar = "${shadowJar.get().archivePath}"
                version = shortVersion
                textVersion = longVersion
                outfile = "${project.name}-$shortVersion.exe"
                copyright = "GPLv3"

                // add icon - if it exists
                file("icon.ico").apply {
                    if (exists()) icon = path
                }

                // add splashfile - if it exists
                file("splash.bmp").apply {
                    if (exists()) splashFileName = path
                }
            }

            // copy executable to release dir
            task<Copy>("release") {
                group = "build"
                from(tasks["createExe"]) {
                    exclude("lib/")
                }
                into("$rootDir/../release")
            }

            // add createExe to default build tasks
            tasks.named("build") {
                dependsOn("createExe")
            }
        }
    }
}
