import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.mavenPublish
    get() = id("maven-publish")

val PluginDependenciesSpec.osDetector
    get() = id("com.google.osdetector") version Versions.com_google_osdetector_gradle_plugin

val PluginDependenciesSpec.syncLibs
    get() = id("jmfayard.github.io.gradle-kotlin-dsl-libs") version Versions.jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin

val PluginDependenciesSpec.shaddow
    get() = id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin