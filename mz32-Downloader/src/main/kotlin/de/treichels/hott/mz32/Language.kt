package de.treichels.hott.mz32

import tornadofx.*
import java.util.*

enum class Language {
    ch, cz, en, es, fr, ge, it, kr, nl;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}