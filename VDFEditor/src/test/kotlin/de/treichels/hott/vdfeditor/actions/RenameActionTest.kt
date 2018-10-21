package de.treichels.hott.vdfeditor.actions

import de.treichels.hott.model.voice.VoiceData
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertEquals

class RenameActionTest {
    @Test
    fun testRenameAction() {
        val list = mutableListOf(VoiceData("before", byteArrayOf(1, 2, 3)))
        val action = RenameAction(0, "after")

        assertEquals(1, list.size.toLong())
        assertEquals("before", list[0].name)
        assertArrayEquals(byteArrayOf(1, 2, 3), list[0].rawData)

        action.apply(list)
        assertEquals(1, list.size.toLong())
        assertEquals("after", list[0].name)
        assertArrayEquals(byteArrayOf(1, 2, 3), list[0].rawData)

        action.undo(list)
        assertEquals(1, list.size.toLong())
        assertEquals("before", list[0].name)
        assertArrayEquals(byteArrayOf(1, 2, 3), list[0].rawData)
    }
}
