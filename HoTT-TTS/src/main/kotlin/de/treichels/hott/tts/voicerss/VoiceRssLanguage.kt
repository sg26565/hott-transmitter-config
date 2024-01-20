package de.treichels.hott.tts.voicerss

import tornadofx.*
import java.util.*

enum class VoiceRssLanguage {
    ca_es, zh_cn, zh_hk, zh_tw, da_dk, nl_nl, en_au, en_ca, en_gb, en_in, en_us, fi_fi, fr_ca, fr_fr, de_de, it_it, ja_jp, ko_kr, nb_no, pl_pl, pt_br, pt_pt,
    ru_ru, es_mx, es_es, sv_se;

    val key: String
        get() = name.lowercase().replace("_", "-")

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name]

    companion object {
        fun forString(language: String): VoiceRssLanguage {
            return VoiceRssLanguage.valueOf(language.lowercase().replace("-", "_"))
        }

        fun forString(language: String, variant: String): VoiceRssLanguage {
            return VoiceRssLanguage.valueOf("${language}_$variant".lowercase())
        }

        val all: List<String>
            get() = values().map { it.toString() }.toList()
    }
}
