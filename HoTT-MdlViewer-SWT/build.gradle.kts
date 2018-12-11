import com.diffplug.gradle.eclipse.MavenCentralExtension
import com.diffplug.gradle.pde.EclipseRelease
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compile(project(":HoTT-Report-HTML"))
    compile(project(":HoTT-Report-XML"))
    compile(project(":HoTT-Report-PDF"))
    compile(Libs.commons_math3)
}

plugins {
    eclipse
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.mdlviewer.swt.LauncherKt"
}

eclipseMavenCentral {
    release("4.9.0") {
        implementation("org.eclipse.swt")
        useNativesForRunningPlatform()
    }
}

tasks.withType(ShadowJar::class) {
    minimize()
}
