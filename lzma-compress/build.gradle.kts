dependencies {
    compile(project(":mz32-Downloader"))
}

plugins {
    application
}

application {
    mainClassName = "de.treichels.hott.lzma.LzmaCompressKt"
}
