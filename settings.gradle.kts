rootProject.name = "hott-transmitter-config"

val javaVersion: String = System.getProperty("java.version")
val pos = javaVersion.indexOf('.', javaVersion.indexOf('.') + 1)
val ver = javaVersion.substring(0, pos).toDouble()

include(":FirmwareUpgrader")
include(":HoTT-Decoder:HoTT-Decoder")
include(":HoTT-MdlViewer-JavaFX")
include(":HoTT-Model")
include(":HoTT-Report-HTML")
include(":HoTT-Report-PDF")
include(":HoTT-Report-XML")
include(":HoTT-TTS")
if (ver == 1.8) include(":Java8ComboBoxListViewSkin")
include("jSerialCommPort")
include("JSSCSerialPort")
include(":lzma-compress")
include(":lzma-sdk")
include(":MemoryDump")
include(":mz32-Downloader")
include(":Test")
include(":VDFEditor")


