import de.treichels.hott.decoder.internal.decoder.BaseDecoder
import de.treichels.hott.report.xml.XMLReport
import de.treichels.hott.util.Util
import java.io.File

fun main(vararg arg:String) {
    val file = File("V:\\MDL-Viewer_Daten\\Models\\mz-24pro\\aClik R2.mdl")
    val model = BaseDecoder.decode(file)
    val xmlOutput = XMLReport.generateXML(model)
    // val hex = Util.dumpData(file.readBytes())
    // println(hex)
    println(xmlOutput)
}
