dependencies {
    compile(project(":mz32-Downloader"))
}

plugins {
    application
    shadow
}

application {
    mainClassName = "de.treichels.hott.lzma.LzmaCompressKt"
}
