package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

enum class PowerType {
    elec, gas, no, noselect;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}

