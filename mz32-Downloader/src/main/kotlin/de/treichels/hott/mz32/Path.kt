package de.treichels.hott.mz32

internal class Path(path: String) {
    // prepend path with leading /
    val value = if (path.startsWith("/")) path else "/$path"

    // /Help
    val isHelp = value.startsWith(helpPath)

    // /Voice
    val isVoice = value.startsWith(voicePath)

    // Paths with language (i.e. /Help or /Voice)
    val isLang = isHelp || isVoice

    // System paths
    @Suppress("MemberVisibilityCanBePrivate")
    val isSystem = systemPaths.any { value.startsWith(it) }


    // split path into directories
    // [0]: always empty
    // [1]: 1st level -> Help, Voice, etc.
    // [2]: language for Help/Voice -> en, ge, fr, etc.
    // [3]: Voice category -> 01_Beep, 02_Func, ... , 10_User or Help section -> B01S1, B01S2, etc.
    private val parts = value.split("/")

    // e.g. /Voice/en/10_User or /Voice/ge/10_Benutzer
    val isLangUser = isVoice && parts.size > 3 && parts[3].startsWith("10_")

    // Any user path
    val isUser = userPaths.any { value.startsWith(it) } || isLangUser

    val isProtected = isSystem || isUser

    val isAutoDelete = autoDeletePaths.any { value.startsWith(it) }

    // convert String to enum
    val language by lazy { Language.valueOf(parts[2]) }

    override fun toString(): String = "Path {value=\"$value\", isHelp=$isHelp, isVoice=$isVoice, isLang=$isLang, isLangUser=$isLangUser, isProtected=$isProtected, isSystem=$isSystem, isUser=$isUser, parts=$parts"

    companion object {
        private const val helpPath = "/Help"
        private const val voicePath = "/Voice"
        private val userPaths = listOf("/Image", "/Log", "/Model", "/MP3", "/Screenshot")
        private val systemPaths = listOf("/GraupnerDisk.cfg", "/System", "/Update", "/md5sum.txt")
        private val autoDeletePaths = listOf("/Help", "/Manual", "/System", "/Util", "/Voice")
    }
}
