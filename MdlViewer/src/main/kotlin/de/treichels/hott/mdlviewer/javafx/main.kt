package de.treichels.hott.mdlviewer.javafx

import de.treichels.hott.ui.ExceptionDialog
import tornadofx.*

fun main(args: Array<String>) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<MdlViewer>(args)
}