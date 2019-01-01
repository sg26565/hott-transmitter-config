package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

enum class ESCType(override val productCode: Int, override val orderNo: String = "", override val category: String = ESCType.category) : Registered<ESCType> {
    bl_control_18(15, "33718"),
    bl_control_35(12, "33735"),
    bl_control_45(12, "33745"),
    bl_control_50(16, "S3046"),
    bl_control_60(12, "33760"),
    bl_control_70(12, "33770"),
    bl_control_100(12, "S3030"),
    bl_control_80_hv(13, "S3041"),
    bl_control_100_hv(13, "S3036"),
    bl_control_120_hv(13, "S3038"),
    bl_control_160_hv(13, "S3039"),
    bl_control_160_hv_cool(13, "S3064"),
    bl_control_60_opto(14, "S3031"),
    bl_control_80_opto(14, "S3042"),
    bl_control_100_opto(14, "S3037"),
    bl_control_120_opto(14, "S3032"),
    bl_control_160_opto(14, "S3033"),
    genius_60(24, "S3084"),
    genius_120(24, "S3051"),
    genius_160(24, "S3078"),
    genius_180(24, "S3085"),
    programmer_box(13017000, "S8272"),
    wlan_module(13020400, "S8505");

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name] + " ($orderNo)"

    companion object {
        fun forProductCode(productCode: Int): ModuleType? = ModuleType.values().firstOrNull { s -> s.productCode == productCode }
        fun forOrderNo(orderNo: String): ModuleType? = ModuleType.values().firstOrNull { s -> s.orderNo == orderNo }

        val category = "Speed_Controller"
    }
}
