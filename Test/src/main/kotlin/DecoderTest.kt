import de.treichels.hott.decoder.internal.decoder.BaseDecoder
import de.treichels.hott.report.xml.XMLReport
import java.io.File

fun main(vararg arg:String) {
    val model = BaseDecoder.decode(File("V:\\MDL-Viewer_Daten\\Models\\mc-28\\aB4 YUKI.mdl"))
    val xmlOutput = XMLReport.generateXML(model)
    println(xmlOutput)
}
