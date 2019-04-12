package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

enum class RudderType {
    normal, wingLET, Unknown;

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]
}
