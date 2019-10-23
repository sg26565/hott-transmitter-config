import kotlin.String

/**
 * Find which updates are available by running
 *     `$ ./gradlew syncLibs`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val aws_java_sdk_polly: String = "1.11.656" //available: "1.11.657" 

    const val com_diffplug_gradle_eclipse_mavencentral_gradle_plugin: String = "3.18.1" 

    const val jackson_databind: String = "2.10.0" 

    const val jserialcomm: String = "2.5.2" 

    const val com_github_johnrengelman_shadow_gradle_plugin: String = "5.1.0" 

    const val com_google_osdetector_gradle_plugin: String = "1.6.2" 

    const val mp3spi: String = "1.9.5.4" 

    const val vorbisspi: String = "1.0.3.3" 

    const val gitversioner: String = "0.5.0" 

    const val commons_io: String = "2.6" 

    const val edu_sc_seis_launch4j_gradle_plugin: String = "2.4.6" 

    const val jaxb_api: String = "2.4.0-b180830.0359" 

    const val jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin: String = "0.2.6" 

    const val junit: String = "4.12" 

    const val launch4j: String = "3.12" 

    const val proguard_gradle: String = "6.2.0" 

    const val tornadofx: String = "1.7.19" 

    const val commons_lang3: String = "3.9" 

    const val commons_math3: String = "3.6.1" 

    const val fluent_hc: String = "4.5.10" 

    const val org_eclipse_swt: String = "3.112.0" 

    const val freemarker: String = "2.3.29" 

    const val jaxb_runtime: String = "2.4.0-b180830.0438" 

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String = "1.3.50" 

    const val org_jetbrains_kotlin: String = "1.3.50" 

    const val org_openjfx: String = "11.0.2" //available: "14-ea+1" 

    const val flying_saucer_pdf_itext5: String = "9.1.18" 

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.6.2"

        const val currentVersion: String = "5.6.3"

        const val nightlyVersion: String = "6.1-20191021220025+0000"

        const val releaseCandidate: String = "6.0-rc-1"
    }
}
