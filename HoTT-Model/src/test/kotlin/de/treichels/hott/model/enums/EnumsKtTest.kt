package de.treichels.hott.model.enums

import com.sun.xml.internal.ws.developer.UsesJAXBContext
import org.junit.Assert.*
import kotlin.test.Test

class EnumsKtTest {
    enum class X { ONE, TWO, THREE, LAST }

    @Test
    fun last() {
        assertEquals(X.LAST, last<X>())
    }

    @Test
    fun testLastIndex() {
        assertEquals(3, lastIndex<X>())
    }

    @Test
    fun testCode() {
        assertEquals(0, X.ONE.code)
        assertEquals(1, X.TWO.code)
        assertEquals(2, X.THREE.code)
        assertEquals(-1, X.LAST.code)
    }

    @Test
    fun testFromCode() {
        assertEquals(X.ONE, fromCode<X>(0))
        assertEquals(X.TWO, fromCode<X>(1))
        assertEquals(X.THREE, fromCode<X>(2))
        assertEquals(X.LAST, fromCode<X>(3))
        assertEquals(X.LAST, fromCode<X>(-1))
    }
}
