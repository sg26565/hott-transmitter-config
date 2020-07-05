package de.treichels.hott.vdfeditor.ui

import java.util.*

enum class Language {
    DE, EN, FR, IT, ES, NL, CZ, RU, CN, KR, JP;

    /** @return the locale-dependent languages
     */
    override fun toString(): String = ResourceBundle.getBundle(javaClass.name).getString(name)
}
