
import com.sun.jna.Memory
import jtermios.JTermios.JTermiosLogging
import jtermios.JTermios.JTermiosLogging.lineno
import jtermios.windows.WinAPI.*
import purejavacomm.CommPortIdentifier

fun main(args: Array<String>) {
    JTermiosLogging.setLogLevel(10)

    val portIdentifier = CommPortIdentifier.getPortIdentifier("COM5")
    val port = portIdentifier.open("SerialTest", 1000)

    port.close()
}

fun fail(): Boolean {
    val err = GetLastError()
    val buffer = Memory(2048)
    FormatMessageW(FORMAT_MESSAGE_FROM_SYSTEM or FORMAT_MESSAGE_IGNORE_INSERTS, null, err, MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), buffer, buffer.size().toInt(), null)

    JTermiosLogging.log(1, "fail() %s, Windows GetLastError()= %d, %s\n", lineno(1), err, buffer.getWideString(0))

    return false
}


