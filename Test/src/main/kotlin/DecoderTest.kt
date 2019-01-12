import de.treichels.hott.decoder.HoTTDecoder
import de.treichels.hott.model.BaseModel
import de.treichels.hott.report.xml.XMLReport
import java.io.File

fun main(vararg arg:String) {
    val path = "V:/MDL-Viewer_Daten/Models/mz-24pro"
    val filename = "hTEST HELI"
    val file = File(path,"$filename.mdl")
    val model: BaseModel = HoTTDecoder.decodeFile(file)
    val xmlOutput = XMLReport.generateXML(model)
    // val hex = Util.dumpData(file.readBytes())
    // println(hex)
    println(xmlOutput)
/*
    val xmlFile = File(path,"$filename.xml")
    xmlFile.writeText(xmlOutput)
    val htmlFile = File(path,"$filename.html")
    val htmlOutput = HTMLReport.generateHTML(model)
    htmlFile.writeText(htmlOutput)
*/
}
