package de.treichels.hott.ui

import de.treichels.hott.util.AbstractCallback
import tornadofx.*

class CallbackAdapter<T>(val task: FXTask<T>) : AbstractCallback() {
    override fun updateMessage(message: String) = runLater { task.updateMessage(message) }
    override fun isCancelled(): Boolean = task.isCancelled
    override fun cancel(): Boolean = task.cancel()

    override fun updateProgress(workDone: Long, totalWork: Long) {
        super.updateProgress(workDone, totalWork)
        runLater { task.updateProgress(workDone, totalWork) }
    }

    override fun updateSubProgress(subWorkDone: Long, subTotalWork: Long) {
        super.updateSubProgress(subWorkDone, subTotalWork)
        runLater { task.updateProgress(subWorkDone, subTotalWork) }
    }
}
