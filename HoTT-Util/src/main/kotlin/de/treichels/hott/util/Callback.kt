package de.treichels.hott.util

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.CancellationException

interface Callback {
    fun updateMessage(message: String)
    fun updateProgress(workDone: Long, max: Long)
    fun updateProgress(workDone: Int, max: Int) = updateProgress(workDone.toLong(), max.toLong())
    fun isCancelled(): Boolean
    fun cancel(): Boolean
}

class SimpleCallback: Callback {
    private var cancelled = false

    override fun updateMessage(message: String) {
        println(message)
    }

    override fun updateProgress(workDone: Long, max: Long) {
        val percent = workDone * 100 / max
        println("progress: $workDone of $max ($percent %)")
    }

    override fun isCancelled(): Boolean = cancelled

    override fun cancel(): Boolean {
        cancelled = true
        return true
    }
}

private fun update(callback: Callback?, counter: Long, total: Long?): Long {
    callback?.apply {
        if (isCancelled()) {
            throw CancellationException()
        }

        if (total != null) {
            // report progress every 1KiB
            if (counter % 1024 == 0L) {
                updateProgress(counter, total)
            }
        }
    }

    return counter + 1
}

class CallbackInputStream(private val callback: Callback?, private val parent: InputStream, private val total: Long?) : InputStream() {
    private var counter = 0L

    override fun read(): Int {
        counter = update(callback, counter, total)
        return parent.read()
    }
}

class CallbackOutputStream(private val callback: Callback?, private val parent: OutputStream, private val total: Long?) : OutputStream() {
    private var counter = 0L

    override fun write(b: Int) {
        counter = update(callback, counter, total)
        parent.write(b)
    }
}
