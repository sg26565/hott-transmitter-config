dependencies {
    compile(project(":HoTT-Model"))
    compile(Libs.aws_java_sdk_polly)
    compile(Libs.jackson_databind)
    runtimeOnly(Libs.mp3spi)
    runtimeOnly(Libs.vorbisspi)
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.tts.SpeechDialogKt"
}