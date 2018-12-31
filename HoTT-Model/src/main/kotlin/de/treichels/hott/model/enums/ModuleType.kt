package de.treichels.hott.model.enums

import de.treichels.hott.util.get
import java.util.*

enum class ModuleType(override val productCode: Int, override val orderNo: String = "", override val category: String = ModuleType.category) : Registered<ModuleType> {
    smartbox(16400, "33700"),
    mxs8(16004900, "33200"),
    x4s(16005900, "33400"),
    mz8(16006700, "S1030.Mx"),
    mz4(16007400, "S1031"),
    mz8p(16007500, "S1032.Mx"),
    mx16s(3000, "4755"),
    mx12s(3010, "4754"),
    mg1(3800, "33300"),
    mg2(3900, "33302"),
    mx10(4200, "33110"),
    mz10(5000, "S1042");

    override fun toString(): String = ResourceBundle.getBundle(javaClass.name)[name] + " ($orderNo)"

    companion object {
        fun forProductCode(productCode: Int): ModuleType? = ModuleType.values().firstOrNull { s -> s.productCode == productCode }
        fun forOrderNo(orderNo: String): ModuleType? = ModuleType.values().firstOrNull { s -> s.orderNo == orderNo }

        const val category = "HoTT_Module"
    }
}
