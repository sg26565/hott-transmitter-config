import de.treichels.hott.decoder.internal.decoder.BaseDecoder
import de.treichels.hott.report.html.HTMLReport
import de.treichels.hott.report.xml.XMLReport
import de.treichels.hott.util.Util
import java.io.File

fun main(vararg arg:String) {
    val path = "V:/MDL-Viewer_Daten/Models/mz-24pro"
    val filename = "hTEST HELI"
    val file = File(path,"$filename.mdl")
    val model = BaseDecoder.decode(file)
    val xmlOutput = XMLReport.generateXML(model)
    // val hex = Util.dumpData(file.readBytes())
    // println(hex)
    println(xmlOutput)
    val xmlFile = File(path,"$filename.xml")
    xmlFile.writeText(xmlOutput)
    val htmlFile = File(path,"$filename.html")
    val htmlOutput = HTMLReport.generateHTML(model)
    htmlFile.writeText(htmlOutput)
}
