package de.treichels.hott.vdfeditor.actions

import org.junit.Before

open class AbstractActionTest {
    protected val list = mutableListOf<String>()

    @Before
    fun setup() {
        list.clear()
        list.addAll(mutableListOf("a", "b", "c", "d"))
    }
}