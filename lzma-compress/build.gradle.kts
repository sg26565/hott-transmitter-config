dependencies {
    compile(project(":mz32-Downloader"))
}

plugins {
    application
    shaddow
}

application {
    mainClassName = "de.treichels.hott.lzma.LzmaCompressKt"
}
