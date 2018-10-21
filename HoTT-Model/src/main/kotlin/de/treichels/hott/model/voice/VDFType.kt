package de.treichels.hott.model.voice

import tornadofx.*
import java.util.*

enum class VDFType {
    System, User;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}