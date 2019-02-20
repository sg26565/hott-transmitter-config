package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

enum class SwitchActive {
    Inh, On, Off, Unknown;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
