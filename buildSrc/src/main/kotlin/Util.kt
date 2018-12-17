import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.mavenPublish
    get() = id("maven-publish")

val PluginDependenciesSpec.osDetector
    get() = id("com.google.osdetector") version Versions.com_google_osdetector_gradle_plugin

val PluginDependenciesSpec.syncLibs
    get() = id("jmfayard.github.io.gradle-kotlin-dsl-libs") version Versions.jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin

val PluginDependenciesSpec.shadow
    get() = id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin

val PluginDependenciesSpec.eclipse
    get() = id("com.diffplug.gradle.eclipse.mavencentral") version Versions.com_diffplug_gradle_eclipse_mavencentral_gradle_plugin

val PluginDependenciesSpec.launch4j
    get() = id("edu.sc.seis.launch4j") version Versions.edu_sc_seis_launch4j_gradle_plugin

val javaVersion by lazy {
    val version = System.getProperty("java.version")
    val pos = version.indexOf('.', version.indexOf('.') + 1)
    version.substring(0, pos).toDouble()
}

val isJava8 = javaVersion == 1.8
