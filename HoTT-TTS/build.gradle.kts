dependencies {
    implementation(project(":HoTT-Model"))
    implementation(project(":HoTT-Voice"))
    implementation(project(":HoTT-UI"))
    implementation("com.amazonaws:aws-java-sdk-polly:_")
    implementation("com.fasterxml.jackson.core:jackson-databind:_")
    runtimeOnly("com.googlecode.soundlibs:mp3spi:_")
    runtimeOnly("com.googlecode.soundlibs:vorbisspi:_")
}

plugins {
    application
}

application {
    mainClass.set("de.treichels.hott.tts.SpeechDialogKt")
}
