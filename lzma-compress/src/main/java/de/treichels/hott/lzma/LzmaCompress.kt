package de.treichels.hott.lzma

import de.treichels.hott.mz32.Hash
import de.treichels.hott.mz32.MD5Sum
import de.treichels.hott.util.ExceptionDialog
import de.treichels.hott.util.extract
import de.treichels.hott.util.hash
import de.treichels.lzma.canCompress
import de.treichels.lzma.compress
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
import java.util.concurrent.atomic.AtomicLong
import java.util.zip.ZipFile
import kotlin.streams.toList

private class LzmaCompressTask(private val zipFile: ZipFile, private val target: File) : Task<Unit>() {
    private val md5Sum = MD5Sum(target)

    override fun call() {
        val start = System.currentTimeMillis()

        try {
        md5Sum.clear()
        updateMessage("Deleting target directory ...")
        target.deleteRecursively()

        val entries = zipFile.stream().filter { !it.isDirectory }.toList()
        val total = entries.size.toLong()
        val totalUncompressedSize = AtomicLong(0L)
        val totalCompressedSize = AtomicLong(0L)
        val count = AtomicLong(0L)

            entries.stream().parallel().forEach { zipEntry ->
                if (!isCancelled) {
                    val size = zipEntry.size
                    val file: File = if (canCompress(zipEntry.name)) {
                        // extract files that cannot be compressed further
                        File(target, zipEntry.name).apply {
                            parentFile.mkdirs()
                            val hash = zipFile.extract(zipEntry, this)
                            md5Sum["/${zipEntry.name}"] = Hash(size, hash)
                        }

                    } else {
                    // compress all other files
                        File(target, "${zipEntry.name}.lzma").apply {
                            parentFile.mkdirs()
                            val hash = zipFile.hash(zipEntry)
                            md5Sum["/${zipEntry.name}"] = Hash(size, hash)
                            compress(zipFile.getInputStream(zipEntry), outputStream())
                        }
                    }

                    // Statistics
                    val now = System.currentTimeMillis()
                    val done = count.incrementAndGet()
                    val uncompressedSize = zipEntry.size
                    val compressedSize = file.length()
                    val rate = compressedSize * 100.0 / uncompressedSize

                    val totalUncompressed = totalUncompressedSize.addAndGet(uncompressedSize)
                    val totalCompressed = totalCompressedSize.addAndGet(compressedSize)
                    val totalRate = totalCompressed * 100.0 / totalUncompressed
                    val elapsed = now - start
                    val throughput = totalCompressed * 1000 / elapsed

                    updateMessage("${zipEntry.name} - ${uncompressedSize.kb()} KB -> ${compressedSize.kb()} KB (${rate.format()}%)")
                    updateTitle("$done of $total Files - total ${totalUncompressed.mb()} MB -> ${totalCompressed.mb()} MB (${totalRate.format()}%) @ ${throughput.mb()} MB/s - elapsed time ${elapsed.toTime()}")
                    updateProgress(done, total)
                }
            }
        } catch (e: Exception) {
            ExceptionDialog.show(e)
        }

        updateMessage("Generating md5sum.txt")
        md5Sum.save()

        updateMessage("done")
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

    // helper
    private var lastZipFile: String
        get() {
            lateinit var result: String
            preferences {
                result = get("lastZipFile", System.getProperty("user.home"))
            }

            return result
        }
        set(value) {
            preferences { put("lastZipFile", value) }
        }
    private var lastTargetDir: String
        get() {
            lateinit var result: String
            preferences {
                result = get("lastTargetDir", System.getProperty("user.home"))
            }

            return result
        }
        set(value) {
            preferences { put("lastTargetDir", value) }
        }

    // UI
    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("mz-32 Resource Zip File") {
                zipFile = textfield {
                    isEditable = false
                    File(lastZipFile).apply {
                        if (exists() && isFile && canRead()) text = absolutePath
                    }
                }

                button("Select") {
                    action {
                        chooseFile("Choose mz-32 Resource File", arrayOf(FileChooser.ExtensionFilter("Zip File", "*.zip"))) {
                            val lastDir = File(lastZipFile).parentFile
                            if (lastDir.isDirectory) initialDirectory = lastDir
                        }.firstOrNull()?.apply {
                            if (exists() && isFile && canRead()) {
                                zipFile.text = absolutePath
                                lastZipFile = absolutePath
                            }
                        }
                    }
                }
            }

            field("Target Directory (WARNING: Existing content will be deleted!)") {
                targetDirectory = textfield {
                    isEditable = false
                    File(lastTargetDir).apply {
                        if (exists() && isDirectory && canWrite()) text = absolutePath
                    }
                }

                button("Select") {
                    action {
                        chooseDirectory("Choose Target Directory") {
                            val lastDir = File(lastTargetDir)
                            if (lastDir.isDirectory) initialDirectory = lastDir
                        }?.apply {
                            if (exists() && isDirectory && canWrite()) {
                                targetDirectory.text = absolutePath
                                lastTargetDir = absolutePath
                            }
                        }
                    }
                }
            }

            field("Progress", Orientation.VERTICAL) {
                progress = textarea {
                    isEditable = false
                    vgrow = Priority.ALWAYS
                    prefWidth = 800.0
                    prefWidth = 600.0
                }
                progressbar {
                    prefWidthProperty().bind(this@LzmaCompress.progress.widthProperty())
                    bind(service.progressProperty())
                    visibleWhen { service.stateProperty().isNotEqualTo(Worker.State.READY) }
                }
                label {
                    textProperty().bind(service.titleProperty())
                }
            }

            button("Start") {
                disableWhen {
                    zipFile.textProperty().isEmpty.or(targetDirectory.textProperty().isEmpty).or(service.runningProperty())
                }

                action {
                    progress.text = "Extracting ${zipFile.text} to ${targetDirectory.text}"

                    service.zipFile = ZipFile(zipFile.text)
                    service.target = File(targetDirectory.text)
                    service.messageProperty().addListener { _, _, newValue ->
                        progress.appendText("\n$newValue")
                    }
                    service.restart()
                }
            }
        }
    }

    init {
        setStageIcon(resources.image("icon.png"))
    }
}

private fun Double.format(digits: Int = 1) = String.format("%.${digits}f", this)
private fun Long.kb() = (this / 1024.0).format()
private fun Long.mb() = (this / 1024.0 / 1024).format()
private fun Long.toTime(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return String.format("%2d:%02d:%02d", hours, minutes % 60, seconds % 60)
}

