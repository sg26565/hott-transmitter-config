package de.treichels.hott.model.serial

import java.util.concurrent.Future

interface UpdateHandler<T>: Future<T> {
    fun update(step: Int, count: Int)
}
