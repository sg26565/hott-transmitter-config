import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-Voice"))
    implementation(Libs.aws_java_sdk_polly)
    implementation(Libs.jackson_databind)
    runtimeOnly(Libs.mp3spi)
    runtimeOnly(Libs.vorbisspi)
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.tts.SpeechDialogKt"
}
