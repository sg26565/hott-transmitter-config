package de.treichels.hott.lzma

import de.treichels.hott.mz32.Hash
import de.treichels.hott.mz32.MD5Sum
import de.treichels.hott.ui.ExceptionDialog
import de.treichels.hott.util.Util
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
import java.nio.file.Files
import java.nio.file.attribute.DosFileAttributeView
import java.util.concurrent.atomic.AtomicLong
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.reflect.KProperty
import kotlin.streams.toList
import kotlinx.coroutines.*

private class LzmaCompressTask(private val source: File, private val target: File) : Task<Unit>() {
    private val md5Sum = MD5Sum(target)
    private val totalUncompressedSize = AtomicLong(0L)
    private val totalCompressedSize = AtomicLong(0L)
    private val fileCount = AtomicLong(0L)
    private var totalFileCount = 0L
    private var startTime = 0L

    private val isWindows = Files.getFileStore(target.toPath()).supportsFileAttributeView(DosFileAttributeView::class.java)
    private val File.isSystem: Boolean
        get() {
            if (isWindows) {
                // ignore system and hidden files on Windows
                Files.getFileAttributeView(toPath(), DosFileAttributeView::class.java).readAttributes().apply {
                    if (isSystem || isHidden) return true
                }
            }

            return false
        }

    private fun processFile(sourceFile: File) {
        if (isCancelled) return

        val name = sourceFile.relativeTo(source).path.replace('\\', '/')
        md5Sum["/$name"] = Hash(sourceFile)

        val targetFile = if (canCompress(sourceFile)) {
            File(target, "$name.lzma").apply {
                parentFile.mkdirs()
                compress(sourceFile, this)
            }
        } else {
            File(target, name).apply {
                parentFile.mkdirs()
                sourceFile.copyTo(this, true)
            }
        }

        updateStatistics(name, sourceFile.length(), targetFile.length())
    }

    private fun getFiles(dir: File): List<File> {
        if (isCancelled) return emptyList()

        val files = mutableListOf<File>()

        dir.listFiles { file -> !file.isSystem }?.forEach { file ->
            if (!isCancelled) {
                if (file.isDirectory)
                    files.addAll(getFiles(file))
                else {
                    files.add(file)
                }
            }
        }

        return files
    }

    override fun call() {
        try {
            md5Sum.clear()
            if (target.list()?.isNotEmpty() == true) {
                updateMessage("\n>>>> Target directory \"" + target.absolutePath + "\" not empty, please clean up <<<<\n")
                cancel()
                updateProgress(0, 0)
                return
            }
            target.mkdirs()

            startTime = System.currentTimeMillis()

            runBlocking {
                if (source.isDirectory) {
                    val files = getFiles(source)
                    totalFileCount = files.size.toLong()
                    files.forEach { file ->
                        launch(Dispatchers.Default) { processFile(file) }
                    }
                } else if (source.isFile && source.name.endsWith(".zip")) {
                    val zipFile = ZipFile(source, Charsets.US_ASCII)
                    val entries = zipFile.stream().filter { !it.isDirectory }.toList()
                    totalFileCount = entries.size.toLong()

                    entries.forEach { zipEntry ->
                        launch(Dispatchers.Default) { processZipEntry(zipEntry, zipFile) }
                    }
                }
            }
        } catch (e: Exception) {
            ExceptionDialog.show(e)
        }

        updateMessage("Generating md5sum.txt")
        md5Sum.save()

        if (md5Sum.size.toLong() != totalFileCount) //md5sum processed files
            updateMessage("\n>>>>>> check for skipped files!\n")
        else
            updateMessage("\ndone\n")
    }

    fun processZipEntry(zipEntry: ZipEntry, zipFile: ZipFile) {
        if (!isCancelled) {
            val size = zipEntry.size
            val file: File = if (canCompress(zipEntry.name)) {
                // compress all other files
                File(target, "${zipEntry.name}.lzma").apply {
                    parentFile.mkdirs()
                    val hash = zipFile.hash(zipEntry)
                    md5Sum["/${zipEntry.name}"] = Hash(size, hash)
                    compress(zipFile.getInputStream(zipEntry), outputStream())
                }
            } else {
                // extract files that cannot be compressed further
                File(target, zipEntry.name).apply {
                    parentFile.mkdirs()
                    val hash = zipFile.extract(zipEntry, this)
                    md5Sum["/${zipEntry.name}"] = Hash(size, hash)
                }
            }

            updateStatistics(zipEntry.name, zipEntry.size, file.length())
        }
    }

    fun updateStatistics(name: String, uncompressedSize: Long, compressedSize: Long) {
        // Statistics
        val now = System.currentTimeMillis()
        val filesDone = fileCount.incrementAndGet()
        val compressionRate = compressedSize * 100.0 / uncompressedSize

        val totalUncompressed = totalUncompressedSize.addAndGet(uncompressedSize)
        val totalCompressed = totalCompressedSize.addAndGet(compressedSize)
        val totalCompressionRate = totalCompressed * 100.0 / totalUncompressed
        val elapsedTime = now - startTime
        val throughput = totalCompressed * 1000 / elapsedTime
        val totalTime = totalFileCount * elapsedTime / filesDone
        val remainingTime = totalTime - elapsedTime

        updateMessage("$name - ${uncompressedSize.kb()} KB -> ${compressedSize.kb()} KB (${compressionRate.format()}%)")
        updateTitle("$filesDone of $totalFileCount Files - ${totalUncompressed.mb()} MB -> ${totalCompressed.mb()} MB (${totalCompressionRate.format()}%) @ ${throughput.kb()} KB/s - elapsed ${elapsedTime.toTime()} - remaining ${remainingTime.toTime()}")
        updateProgress(filesDone, totalFileCount)
    }
}

private class LzmaCompressService : Service<Unit>() {
    lateinit var source: File
    lateinit var target: File

    override fun createTask(): Task<Unit> = LzmaCompressTask(source, target)
}

fun main(vararg args: String) {
    Thread.setDefaultUncaughtExceptionHandler { _, e -> ExceptionDialog.show(e) }
    launch<LzmaCompressApp>(*args)
}

class LzmaCompressApp : App() {
    override val primaryView = LzmaCompress::class
}

class Preference(private val defaultValue: String = "") {
    operator fun getValue(thisRef: Component, property: KProperty<*>): String {
        lateinit var result: String
        thisRef.preferences { result = get(property.name, defaultValue) }
        return result
    }

    operator fun setValue(thisRef: Component, property: KProperty<*>, value: String) {
        thisRef.preferences { put(property.name, value) }
    }
}

class LzmaCompress : View("Compress Zip File to Directory (" + Util.sourceVersion(LzmaCompress::class) + ")") {
    // Controls
    private var source by singleAssign<TextField>()
    private var target by singleAssign<TextField>()
    private var progress by singleAssign<TextArea>()

    // Background service
    private var service = LzmaCompressService()

    // Preferences
    private val homeDir = System.getProperty("user.home")
    private var lastZipFile by Preference()
    private var lastSourceDir by Preference(homeDir)
    private var lastSource by Preference(homeDir)
    private var lastTargetDir by Preference(homeDir)

    // UI
    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("mz-32 Resource Zip File or Directory") {
                source = textfield {
                    isEditable = false
                    File(lastSource).apply {
                        if (exists() && canRead()) text = absolutePath
                    }
                }

                button("Select Zip") {
                    action {
                        chooseFile("Choose mz-32 Resource File", arrayOf(FileChooser.ExtensionFilter("Zip File", "*.zip"))) {
                            File(lastZipFile).apply {
                                if (exists() && isFile && canRead()) {
                                    initialFileName = this.name
                                    initialDirectory = this.parentFile
                                }
                            }
                        }.firstOrNull()?.apply {
                            if (exists() && isFile && canRead()) {
                                source.text = absolutePath
                                lastZipFile = absolutePath
                                lastSource = absolutePath
                            }
                        }
                    }
                }

                button("Select Dir") {
                    action {
                        chooseDirectory {
                            File(lastSourceDir).apply { if (exists() && isDirectory && canRead()) initialDirectory = this }
                        }?.apply {
                            if (exists() && isDirectory && canRead()) {
                                source.text = absolutePath
                                lastSourceDir = absolutePath
                                lastSource = absolutePath
                            }
                        }
                    }
                }
            }

            field("Target Directory (check for empty directory)") {
                target = textfield {
                    isEditable = false
                    File(lastTargetDir).apply {
                        if (exists() && isDirectory && canWrite()) text = absolutePath
                    }
                }

                button("Select Dir") {
                    action {
                        chooseDirectory("Choose Target Directory") {
                            File(lastTargetDir).apply {
                                if (exists() && isDirectory && canWrite()) initialDirectory = this
                            }
                        }?.apply {
                            if (exists() && isDirectory && canWrite()) {
                                target.text = absolutePath
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
                    source.textProperty().isEmpty.or(target.textProperty().isEmpty)
                }

                action {
                    if (service.isRunning) {
                        service.cancel()
                        text = "Start"
                    } else {
                        progress.text = "Compressing ${source.text} to ${target.text}"
                        service.source = File(source.text)
                        service.target = File(target.text)
                        service.restart()
                        text = if (File(target.text).list()?.isNotEmpty() == false) {
                            "Cancel"
                        } else {
                            "Start"
                        }
                    }
                }
            }
        }
    }

    init {
        setStageIcon(resources.image("icon.png"))
        service.messageProperty().addListener { _, _, newValue ->
            progress.appendText("\n$newValue")
        }
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


