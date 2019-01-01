import de.treichels.hott.update.DeviceFirmware
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

fun main(vararg args: String) {
    val fileName1= "C:/Users/olive/.java/cache/HoTT/firmware/gr12l/GR-12L_1a92.bin"
    val fileName2 = "C:/Users/olive/.java/cache/HoTT/firmware/gr16/FS_GR-16_7a06.bin"
    val fileName3 = "C:/Users/olive/.java/cache/HoTT/firmware/gr12s/gr_rx6_mini_3a57.bin"
    val fileName4 = "C:/Users/olive/.java/cache/HoTT/firmware/gr12sc/33565_33566_MicroRec_V105.bin"
    val fileName5 = "C:/Users/olive/.java/cache/HoTT/firmware/heim3d/S1029_Heli_Software_V202_210918.bin"
    println(DeviceFirmware.load(File(fileName5)).dump())
    exitProcess(0)

    File("C:/Users/olive/.java/cache/HoTT/firmware")
            .listFiles { file, name -> file.isDirectory && (name.startsWith("gr") || name.startsWith("falcon")) }
            .forEach { dir ->
                println(dir)
                dir.listFiles { _, name -> name.endsWith(".bin") }.forEach { file ->
                    print("\t$file: ")

                    try {
                        println(DeviceFirmware.load(file).dump())
                    } catch (e: IOException) {
                        println(e.message)
                    }
                }
            }
}



