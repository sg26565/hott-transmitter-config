import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.pascalwelsch.gitversioner.GitVersioner
import edu.sc.seis.launch4j.tasks.Launch4jLibraryTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.file.DuplicatesStrategy

buildscript {
    dependencies.classpath("com.pascalwelsch.gitversioner:gitversioner:0.5.0") // cannot be loaded from plugin store
}

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("edu.sc.seis.launch4j")
    id("com.google.osdetector")
}

apply(plugin = "com.pascalwelsch.gitversioner")

val gitVersioner = the<GitVersioner>().apply {
    yearFactor = 0
    addLocalChangesDetails = false
}

val versionCode by extra(gitVersioner.versionCode)
val versionName by extra(gitVersioner.versionName)
val baseVersion = "1.0"
val shortVersion = "$baseVersion.$versionCode"
val longVersion = "$baseVersion.$versionName"

subprojects {
    group = "de.treichels.hott"
    version = shortVersion

    apply(plugin = "kotlin")

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
            kotlinOptions.freeCompilerArgs = listOf("-opt-in=kotlin.ExperimentalUnsignedTypes")
        }
    }

    dependencies {
        // common dependencies
        implementation("org.jetbrains.kotlin:kotlin-reflect:_")
        testImplementation("junit:junit:_")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:_")
    }

    // add shadow and launch4j to any application project
    afterEvaluate {
        if (this.plugins.hasPlugin("application")) {
            // apply the shadow and launch4j plugins
            apply(plugin = "com.github.johnrengelman.shadow")
            apply(plugin = "edu.sc.seis.launch4j")

            tasks {
                named<Launch4jLibraryTask>("createExe") {
                    version = shortVersion
                    textVersion = longVersion
                    outfile = "${project.name}-${shortVersion}.exe"
                    copyright = "GPLv3"
                    jarTask = project.tasks.shadowJar
                    jreMinVersion = "21.0.1"

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
                create<Copy>("release") {
                    dependsOn(createExe)

                    group = "build"
                    from(createExe) {
                        include("*.exe")
                    }
                    from(shadowJar) {
                        include("*.jar")
                    }
                    into("$rootDir/../release")
                    duplicatesStrategy = DuplicatesStrategy.INCLUDE
                }

                // add createExe to default build tasks
                named("build") {
                    dependsOn(createExe)
                }
            }
        }
    }
}
