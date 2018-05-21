package de.treichels.hott.lzma

import SevenZip.Compression.LZMA.Encoder
import de.treichels.hott.mz32.MD5Sum
import de.treichels.hott.util.ExceptionDialog
import de.treichels.hott.util.hash
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.concurrent.Worker
import javafx.geometry.Orientation
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File
import java.util.zip.ZipFile

private class LzmaCompressTask(private val zipFile: ZipFile, private val target: File) : Task<Unit>() {
    private val md5Sum = MD5Sum(target)

    override fun call() {
        md5Sum.clear()
        target.deleteRecursively()

        val total = zipFile.stream().filter { !it.isDirectory }.count().toLong()
        var count = 0L
        zipFile.stream().parallel().filter { !it.isDirectory }.forEach { zipEntry ->
            if (!isCancelled) {
                val file = File(target, "${zipEntry.name}.lzma").apply { parentFile.mkdirs() }

                md5Sum["/${zipEntry.name}"] = zipFile.hash(zipEntry)

                zipFile.getInputStream(zipEntry).use { inputStream ->
                    file.outputStream().use { outputStream ->
                        Encoder().apply {
                            SetEndMarkerMode(true)
                            WriteCoderProperties(outputStream)
                            Code(inputStream, outputStream, -1, -1, null)
                        }
                    }
                }

                count++
                val uncompressedSize = zipEntry.size / 1024
                val compressedSize = file.length() / 1024
                val ratio = compressedSize * 100 / uncompressedSize
                updateMessage("${zipEntry.name} $uncompressedSize kb -> $compressedSize kb ($ratio%)")
                updateProgress(count, total)
            }
        }

        md5Sum.save()
    }
}

private class LzmaCompressService : Service<Unit>() {
    lateinit var zipFile: ZipFile
    lateinit var target: File

    override fun createTask(): Task<Unit> = LzmaCompressTask(zipFile, target)
}

fun main(vararg args: String) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<LzmaCompressApp>(*args)
}

class LzmaCompressApp : App() {
    override val primaryView = LzmaCompress::class
}

class LzmaCompress : View("Compress Zip File to Directory") {
    // Controls
    private var zipFile by singleAssign<TextField>()
    private var targetDirectory by singleAssign<TextField>()
    private var progress by singleAssign<TextArea>()

    // Background service
    private var service = LzmaCompressService()

    // UI
    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("mz-32 Resource Zip File") {
                zipFile = textfield { isEditable = false }
                button("Select") {
                    action {
                        val file = chooseFile("Choose mz-32 Resource File", arrayOf(FileChooser.ExtensionFilter("Zip File", "*.zip"))) {
                            preferences {
                                val lastDir = File(get("lastZipDir", System.getProperty("user.home")))

                                if (lastDir.isDirectory)
                                    this@chooseFile.initialDirectory = lastDir
                            }
                        }.firstOrNull()?.absoluteFile

                        if (file != null) {
                            zipFile.text = file.absolutePath
                            preferences { put("lastZipDir", file.parentFile.absolutePath) }
                        }
                    }
                }
            }

            field("Target Directory (WARNING: Existing content will be deleted!)") {
                targetDirectory = textfield { isEditable = false }
                button("Select") {
                    action {
                        val dir = chooseDirectory("Choose Target Directory") {
                            preferences {
                                val lastDir = File(get("lastTargetDir", System.getProperty("user.home")))

                                if (lastDir.isDirectory)
                                    this@chooseDirectory.initialDirectory = lastDir
                            }
                        }?.absoluteFile


                        if (dir != null) {
                            targetDirectory.text = dir.absolutePath
                            preferences { put("lastTargetDir", dir.absolutePath) }
                        }
                    }
                }
            }

            field("Progress", Orientation.VERTICAL) {
                progress = textarea {
                    isEditable = false
                    vgrow = Priority.ALWAYS
                }
                progressbar {
                    prefWidthProperty().bind(this@LzmaCompress.progress.widthProperty())
                    bind(service.progressProperty())
                    visibleWhen { service.stateProperty().isNotEqualTo(Worker.State.READY) }
                }
            }

            button("Start") {
                disableWhen {
                    zipFile.textProperty().isEmpty.or(targetDirectory.textProperty().isEmpty).or(service.stateProperty().isNotEqualTo(Worker.State.READY))
                }

                action {
                    progress.text = "Extracting ${zipFile.text} to ${targetDirectory.text}"

                    service.zipFile = ZipFile(zipFile.text)
                    service.target = File(targetDirectory.text)
                    service.messageProperty().addListener { _, _, newValue ->
                        progress.appendText("\n$newValue")
                    }
                    service.start()
                }
            }
        }
    }
}