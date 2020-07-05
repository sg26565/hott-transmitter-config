import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner
import edu.sc.seis.launch4j.Launch4jPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.gradle.osdetector.OsDetector

buildscript {
    dependencies.classpath("com.pascalwelsch.gitversioner:gitversioner:0.5.0")
}

plugins {
    java
    kotlin("jvm") apply false
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "5.1.0" apply false
    id("edu.sc.seis.launch4j") version "2.4.6" apply false
    id("com.google.osdetector") version "1.6.2"
}

apply(plugin = "com.pascalwelsch.gitversioner")

val gitVersioner = the<GitVersioner>().apply {
    yearFactor = 0
    addLocalChangesDetails = false
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
                        "Implementation-Version" to archiveVersion.orNull,
                        "Implementation-Build" to versionName
                )
            }

            // enable reproducible builds
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            archiveVersion.set(shortVersion)
        }

        // set jvmTarget for Kotlin (covers compileKotlin and compileTestKotlin tasks)
        withType(KotlinCompile::class) {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.ExperimentalUnsignedTypes")
        }
    }

    dependencies {
        // common dependencies
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:_")
        testImplementation("junit:junit:_")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:_")

        // dependency management
        @Suppress("UnstableApiUsage")
        constraints {
            add("implementation", "org.jetbrains.kotlin:kotlin-reflect:_")
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
                archiveVersion.set(shortVersion)
            }

            // configure createExe task
            configure<Launch4jPluginExtension> {
                jar = shadowJar.get().archiveFile.get().asFile.path
                version = shortVersion
                textVersion = longVersion
                outfile = "${project.name}-$shortVersion.exe"
                copyright = "GPLv3"
                bundledJrePath = if (OsDetector().os == "windows") "%JAVA_HOME%" else "\${JAVA_HOME}"

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
                    include("*.exe")
                }
                from(tasks["shadowJar"]) {
                    include("*.jar")
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
