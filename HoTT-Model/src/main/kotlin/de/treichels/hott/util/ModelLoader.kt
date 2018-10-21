package de.treichels.hott.util

import de.treichels.hott.model.BaseModel
import java.io.IOException

interface ModelLoader {

    val model: BaseModel?
    @Throws(IOException::class) get


    val modelData: ByteArray?
    @Throws(IOException::class) get

    fun onCancel()
    fun onOpen()
    fun onReload()
}