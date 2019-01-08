package de.treichels.hott.ui

import de.treichels.hott.util.Callback
import javafx.concurrent.Task
import tornadofx.*

abstract class CallbackTask<T> : Task<T>(), Callback {
    override fun updateMessage(message: String) = runLater { super.updateMessage(message) }
    override fun updateProgress(workDone: Long, max: Long) = runLater { super<Task>.updateProgress(workDone, max) }
}

class CallbackAdapter<T>(val task: FXTask<T>) : Callback {
    override fun updateMessage(message: String) = runLater { task.updateMessage(message) }
    override fun updateProgress(workDone: Long, max: Long) = runLater { task.updateProgress(workDone, max) }
    override fun isCancelled(): Boolean = task.isCancelled
    override fun cancel(): Boolean = task.cancel()
}
