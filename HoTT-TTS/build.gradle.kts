dependencies {
    compile(project(":HoTT-Model"))
    compile(Libs.aws_java_sdk_polly)
    implementation(Libs.mp3spi)
    implementation(Libs.vorbisspi)
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.tts.SpeechDialogKt"
}