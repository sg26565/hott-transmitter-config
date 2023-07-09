package de.treichels.hott.mz32

internal class Path(path: String) {
    // prepend path with leading /
    val value = if (path.startsWith("/")) path else "/$path"

    // /Help
    val isHelp = value.startsWith(helpPath, ignoreCase = true)

    // /Voice
    val isVoice = value.startsWith(voicePath, ignoreCase = true)

    // /Manual
    val isManual = value.startsWith(manualPath, ignoreCase = true)

    // /Widgets
    val isWidgets = value.startsWith(widgetsPath, ignoreCase = true)

    // /Image
    val isImage = value.startsWith(imagePath, ignoreCase = true)

    // /Util
    val isUtils = value.startsWith(utilsPath, ignoreCase = true)

    // Paths with language (i.e. /Help or /Voice)
    val isLang = isHelp || isVoice || isManual

    // System paths
    @Suppress("MemberVisibilityCanBePrivate")
    val isSystem = systemPaths.any { value.startsWith(it, ignoreCase = true) }


    // split path into directories
    // [0]: always empty
    // [1]: 1st level -> Help, Voice, etc.
    // [2]: language for Help/Voice -> en, ge, fr, etc.
    // [3]: Voice category -> 01_Beep, 02_Func, ... , 10_User or Help section -> B01S1, B01S2, etc.
    private val parts = value.split("/")

    // e.g. /Voice/en/10_User or /Voice/ge/10_Benutzer
    // extend exclude list with Voice/dv, 1*_* language user extensions
    val isLangUser = isVoice && parts.size > 3 && parts[2].startsWith("dv")
            || isVoice && parts.size > 3 && (parts[3].startsWith("1") && parts[3].contains("_"))

    // Any user path
    val isUser = userPaths.any { value.startsWith(it, ignoreCase = true) } || isLangUser

    val isProtected = isSystem || isUser

    val isAutoDelete = autoDeletePaths.any { value.startsWith(it, ignoreCase = true) }

    // convert String to enum
    val language by lazy { Language.valueOf(parts[2].toLowerCase()) }

    override fun toString(): String = "Path {value=\"$value\", isHelp=$isHelp, isVoice=$isVoice, isLang=$isLang, isLangUser=$isLangUser, isProtected=$isProtected, isSystem=$isSystem, isUser=$isUser, parts=$parts"

    companion object {
        private const val helpPath = "/Help"
        private const val voicePath = "/Voice"
        private const val manualPath = "/Manual"
        private const val widgetsPath = "/Widgets"
        private const val imagePath = "/Image"
        private const val utilsPath = "/Util"
        private val userPaths = listOf("/Maps", "/Widgets", "/Image", "/Log", "/Model", "/MP3", "/Screenshot")
        private val systemPaths = listOf("/GraupnerDisk.cfg", "/System", "/Update", "/md5sum.txt")
        private val autoDeletePaths = listOf("/Help", "/Manual", "/Voice")
    }
}
