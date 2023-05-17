package de.treichels.hott.mz32

import tornadofx.*
import java.util.*

enum class Language {
    ch, cz, en, es, fr, ge, it, kr, nl, pt, dv; //add language placeholder "dv" for diverse user defined language

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]

    companion object { //remove dv from values() to hide display in selection list
        fun getValues(): Array<Language> = enumValues<Language>().copyOfRange(0, 10)
    }
}
