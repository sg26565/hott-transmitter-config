package de.treichels.hott.vdfeditor.ui

import de.treichels.hott.messages.Messages
import de.treichels.hott.ui.ExceptionDialog
import de.treichels.hott.util.Util
import tornadofx.*
import java.io.File
import java.util.jar.JarFile
import java.util.logging.LogManager

fun main(args: Array<String>) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<VDFEditor>(args)
}

class VDFEditor : App() {
    companion object {
        val title by lazy {
            if (java.lang.Boolean.getBoolean("offline"))
                String.format("VDF Editor - %s (offline version)", version)
            else
                String.format("VDF Editor - %s", version)
        }

        val version: String by lazy {
            var result = Messages.getString("Launcher.Unknown")

            val source = File(VDFEditor::class.java.protectionDomain.codeSource.location.toURI())
            if (source.name.endsWith(".jar") || source.name.endsWith(".exe"))
                JarFile(source).use { jarFile ->
                    val attributes = jarFile.manifest.mainAttributes
                    result = Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version"), attributes.getValue("Implementation-Build"))
                }

            result
        }
    }

    init {
        // setup logging
        if (Util.DEBUG) LogManager.getLogManager().readConfiguration(ClassLoader.getSystemResourceAsStream("logging.properties"))
    }

    override val primaryView = MainView::class
}