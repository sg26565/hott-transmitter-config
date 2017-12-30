package de.treichels.hott.model.serial

interface UpdateHandler {
    val cancelled: Boolean
    fun update(step: Int, count: Int)
}
