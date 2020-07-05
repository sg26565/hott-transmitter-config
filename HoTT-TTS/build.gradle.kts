dependencies {
    compile(project(":HoTT-Model"))
    compile(project(":HoTT-Voice"))
    implementation("com.amazonaws:aws-java-sdk-polly:_")
    implementation("com.fasterxml.jackson.core:jackson-databind:_")
    runtimeOnly("com.googlecode.soundlibs:mp3spi:_")
    runtimeOnly("com.googlecode.soundlibs:vorbisspi:_")
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.tts.SpeechDialogKt"
}
