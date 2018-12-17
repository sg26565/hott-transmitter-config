package de.treichels.hott.model.enums

import org.junit.Assert.*
import org.junit.Test

class SensorTypeTest {
    @Test
    fun testMap() {
        assertEquals(0b0000000, SensorType.None.map)
        assertEquals(0b0000001, SensorType.Receiver.map)
        assertEquals(0b0000010, SensorType.GeneralModule.map)
        assertEquals(0b0000100, SensorType.ElectricAirModule.map)
        assertEquals(0b0001000, SensorType.Vario.map)
        assertEquals(0b0010000, SensorType.GPS.map)
        assertEquals(0b0100000, SensorType.ESC.map)
    }

    @Test
    fun testForMap() {
        assertTrue(SensorType.forMap(0).isEmpty())

        var list = SensorType.forMap(1)
        assertFalse(list.isEmpty())
        assertEquals(1, list.size)
        assertTrue(list.contains(SensorType.Receiver))
        assertFalse(list.contains(SensorType.GeneralModule))
        assertFalse(list.contains(SensorType.ElectricAirModule))
        assertFalse(list.contains(SensorType.Vario))
        assertFalse(list.contains(SensorType.GPS))
        assertFalse(list.contains(SensorType.ESC))

        list = SensorType.forMap(11)
        assertFalse(list.isEmpty())
        assertEquals(3, list.size)
        assertTrue(list.contains(SensorType.Receiver))
        assertTrue(list.contains(SensorType.GeneralModule))
        assertFalse(list.contains(SensorType.ElectricAirModule))
        assertTrue(list.contains(SensorType.Vario))
        assertFalse(list.contains(SensorType.GPS))
        assertFalse(list.contains(SensorType.ESC))

        list = SensorType.forMap(63)
        assertFalse(list.isEmpty())
        assertEquals(6, list.size)
        assertTrue(list.contains(SensorType.Receiver))
        assertTrue(list.contains(SensorType.GeneralModule))
        assertTrue(list.contains(SensorType.ElectricAirModule))
        assertTrue(list.contains(SensorType.Vario))
        assertTrue(list.contains(SensorType.GPS))
        assertTrue(list.contains(SensorType.ESC))
    }

    @Test
    fun testGetMap() {
        val list = mutableListOf<SensorType>()

        assertEquals(0, SensorType.getMap(list))

        list.add(SensorType.Receiver)
        assertEquals(1, SensorType.getMap(list))

        list.add(SensorType.Vario)
        assertEquals(9, SensorType.getMap(list))

        list.add(SensorType.GeneralModule)
        assertEquals(11, SensorType.getMap(list))

        list.add(SensorType.ElectricAirModule)
        assertEquals(15, SensorType.getMap(list))

        list.add(SensorType.ESC)
        assertEquals(47, SensorType.getMap(list))

        list.add(SensorType.GPS)
        assertEquals(63, SensorType.getMap(list))
    }
}
