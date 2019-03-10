package de.treichels.hott.util

interface Callback {
    fun updateMessage(message: String)
    fun updateProgress(workDone: Long, max: Long)
    fun updateProgress(workDone: Int, max: Int) = updateProgress(workDone.toLong(), max.toLong())
    fun isCancelled(): Boolean
    fun cancel(): Boolean
}

class SimpleCallback : Callback {
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
