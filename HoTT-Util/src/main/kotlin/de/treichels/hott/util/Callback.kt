package de.treichels.hott.util

interface Callback {
    fun updateMessage(message: String)
    fun updateProgress(workDone: Long, max: Long)
    fun updateProgress(workDone: Int, max: Int) = updateProgress(workDone.toLong(), max.toLong())
    fun isCancelled(): Boolean
    fun cancel(): Boolean
}
