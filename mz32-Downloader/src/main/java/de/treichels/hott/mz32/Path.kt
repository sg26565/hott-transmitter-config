package de.treichels.hott.mz32

internal class Path(path: String) {
    // prepend path with leading /
    val value = if (path.startsWith("/")) path else "/$path"

    // /Help
    val isHelp = value.startsWith(helpPath)

    // /Voice
    val isVoice = value.startsWith(voicePath)

    // Paths with langage (i.e. /Help or /Voice)
    val isLang = isHelp || isVoice

    // Any user path
    val isUser = userPaths.any { value.startsWith(it) }

    // split path into directories
    // [0]: always empty
    // [1]: 1st level -> Help, Voice, etc.
    // [2]: language for Help/Voice -> en, ge, fr, etc.
    // [3]: Voice category -> 01_Beep, 02_Func, ... , 10_User or Help section -> B01S1, B01S2, etc.
    private val parts by lazy { value.split("/") }

    // e.g. /Voice/en/10_User or /Voice/ge/10_Benutzer
    val isLangUser by lazy { isVoice && parts[3].startsWith("10_") }

    // convert String to enum
    val language by lazy { Language.valueOf(parts[2]) }

    companion object {
        private const val helpPath = "/Help"
        private const val voicePath = "/Voice"
        private val userPaths = listOf("/GraupnerDisk.cfg", "/Image", "/Log", "/Model", "/MP3", "/Screenshot", "/System Volume Information", "/Update/ftplist")
    }
}
