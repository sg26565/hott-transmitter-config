import de.treichels.hott.decoder.HoTTDecoder
import de.treichels.hott.model.BaseModel
import de.treichels.hott.report.html.HTMLReport
import de.treichels.hott.report.xml.XMLReport
import java.io.File

fun main(vararg arg:String) {
    val path = "V:/Dropbox/MDL-Viewer_Daten/Models/mz-24pro"
     val filename = "gAHI"
     // val filename = "hTEST HELI"
    // val path = "V:/MDL-Viewer_Daten/Models/mc-28"
    // val filename = "aB4 YUKI"
    val file = File(path,"$filename.mdl")
    val model: BaseModel = HoTTDecoder.decodeFile(file)
    val xmlOutput = XMLReport.generateXML(model)
    // val hex = Util.dumpData(file.readBytes())
    // println(hex)
    println(xmlOutput)

    // write xml-file
    val xmlFile = File(path,"$filename.xml")
    xmlFile.writeText(xmlOutput)

    // crate and write html-file
    // val htmlFile = File(path,"$filename.html")
    // val htmlOutput = HTMLReport.generateHTML(model)
    // htmlFile.writeText(htmlOutput)

}
