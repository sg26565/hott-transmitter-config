package de.treichels.hott.vdfeditor.ui

import gde.mdl.messages.Messages
import javazoom.spi.mpeg.sampled.convert.MpegFormatConversionProvider
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader
import javazoom.spi.vorbis.sampled.convert.VorbisFormatConversionProvider
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader
import tornadofx.*
import java.io.File
import java.util.jar.JarFile

fun main(args: Array<String>) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<Launcher>(args)
}

// static references to mp3 and ogg decoders to keep them in the final jar
private val IGNORE = arrayOf(MpegAudioFileReader::class.java, MpegFormatConversionProvider::class.java, VorbisAudioFileReader::class.java, VorbisFormatConversionProvider::class.java)

fun getTitle(): String {
    return if (java.lang.Boolean.getBoolean("offline"))
        String.format("VDF Editor - %s (offline version)", getVersion())
    else
        String.format("VDF Editor - %s", getVersion())
}

fun getVersion(): String {
    val source = File(Launcher::class.java.protectionDomain.codeSource.location.toURI())
    if (source.name.endsWith(".jar") || source.name.endsWith(".exe"))
        JarFile(source).use { jarFile ->
            val attributes = jarFile.manifest.mainAttributes
            return Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version"), attributes.getValue("Implementation-Build"))
        }

    return Messages.getString("Launcher.Unknown")
}

class Launcher : App() {
    override val primaryView= MainView::class
}