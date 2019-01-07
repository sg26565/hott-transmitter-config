package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

enum class SwitchMode {
    single, dual, Unknown;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
