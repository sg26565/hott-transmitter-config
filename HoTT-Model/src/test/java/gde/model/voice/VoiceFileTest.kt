package gde.model.voice

import gde.model.enums.TransmitterType
import javafx.beans.Observable
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class VoiceFileTest {
    private val voiceFile = VoiceFile()

    private var changed = false
    private var vdfTypeChanged = false
    private var transmitterTypeChanged = false
    private var vdfVersionChanged = false
    private var countryChanged = false
    private var voiceListChanged = false
    private var systemVDFChanged = false
    private var sizeChanged = false
    private var dirtyChanged = false

    @Before
    fun setup() {
        voiceFile.addListener({ observable ->
            assertEquals(voiceFile, observable)
            changed = true
        })

        voiceFile.vdfTypeProperty.addListener({ observable, _, newValue ->
            assertEquals(voiceFile.vdfTypeProperty, observable)
            assertEquals(voiceFile.vdfType, newValue)
            vdfTypeChanged = true
        })

        voiceFile.transmitterTypeProperty.addListener({ observable, _, newValue ->
            assertEquals(voiceFile.transmitterTypeProperty, observable)
            assertEquals(voiceFile.transmitterType, newValue)
            transmitterTypeChanged = true
        })

        voiceFile.vdfVersionProperty.addListener({ observable, _, newValue ->
            assertEquals(voiceFile.vdfVersionProperty, observable)
            assertEquals(voiceFile.vdfVersion, newValue)
            vdfVersionChanged = true
        })

        voiceFile.countryProperty.addListener({ observable, _, newValue ->
            assertEquals(voiceFile.countryProperty, observable)
            assertEquals(voiceFile.country, newValue)
            countryChanged = true
        })

        voiceFile.voiceList.addListener( { observable: Observable ->
            assertEquals(voiceFile.voiceList, observable)
            voiceListChanged = true
        })

        voiceFile.systenVDFProperty.addListener({ observable, _, newValue ->
            assertEquals(voiceFile.systenVDFProperty, observable)
            assertEquals(voiceFile.isSystemVDF, newValue)
            systemVDFChanged = true
        })

        voiceFile.sizeProperty.addListener({ observable, _, newValue ->
            assertEquals(voiceFile.sizeProperty, observable)
            assertEquals(voiceFile.size, newValue)
            sizeChanged = true
        })

        voiceFile.dirtyBinding.addListener({ observable, _, newValue ->
            assertEquals(voiceFile.dirtyBinding, observable)
            assertEquals(voiceFile.isDirty, newValue)
            dirtyChanged = true
        })
    }

    private fun clean() {
        voiceFile.clean()

        changed = false
        vdfTypeChanged = false
        transmitterTypeChanged = false
        vdfVersionChanged = false
        countryChanged = false
        voiceListChanged = false
        systemVDFChanged = false
        sizeChanged = false
        dirtyChanged = false
    }
    
    @Test
    fun testDefault() {
        assertFalse(changed)

        assertFalse(vdfTypeChanged)
        assertEquals(VDFType.User, voiceFile.vdfType)

        assertFalse(transmitterTypeChanged)
        assertEquals(TransmitterType.mc28, voiceFile.transmitterType)

        assertFalse(vdfVersionChanged)
        assertEquals(3000, voiceFile.vdfVersion)

        assertFalse(countryChanged)
        assertEquals(CountryCode.GLOBAL, voiceFile.country)

        assertFalse(voiceListChanged)
        assertFalse(sizeChanged)
        assertEquals(0, voiceFile.size)
        assertEquals(0, voiceFile.rawDataSize)

        assertFalse(systemVDFChanged)
        assertFalse(voiceFile.isSystemVDF)

        assertFalse(dirtyChanged)
        assertFalse(voiceFile.isDirty)

        voiceFile.clean()

        assertFalse(voiceFile.isDirty)
    }

    @Test
    fun testChangeVDFType() {
        testDefault()

        voiceFile.vdfType = VDFType.System

        assertTrue(changed)

        assertTrue(vdfTypeChanged)
        assertEquals(VDFType.System, voiceFile.vdfType)

        assertFalse(transmitterTypeChanged)
        assertEquals(TransmitterType.mc28, voiceFile.transmitterType)

        assertFalse(vdfVersionChanged)
        assertEquals(3000, voiceFile.vdfVersion)

        assertFalse(countryChanged)
        assertEquals(CountryCode.GLOBAL, voiceFile.country)

        assertFalse(voiceListChanged)
        assertFalse(sizeChanged)
        assertEquals(0, voiceFile.size)
        assertEquals(0, voiceFile.rawDataSize)

        assertTrue(systemVDFChanged)
        assertTrue(voiceFile.isSystemVDF)

        assertTrue(dirtyChanged)
        assertTrue(voiceFile.isDirty)

        clean()

        assertFalse(voiceFile.isDirty)
    }

    @Test
    fun testChangeTransmitterType() {
        voiceFile.transmitterType = TransmitterType.mx12

        assertTrue(changed)

        assertFalse(vdfTypeChanged)
        assertEquals(VDFType.User, voiceFile.vdfType)

        assertTrue(transmitterTypeChanged)
        assertEquals(TransmitterType.mx12, voiceFile.transmitterType)

        assertFalse(vdfVersionChanged)
        assertEquals(3000, voiceFile.vdfVersion)

        assertFalse(countryChanged)
        assertEquals(CountryCode.GLOBAL, voiceFile.country)

        assertFalse(voiceListChanged)
        assertEquals(0, voiceFile.size)
        assertEquals(0, voiceFile.rawDataSize)

        assertFalse(systemVDFChanged)
        assertFalse(voiceFile.isSystemVDF)

        assertTrue(dirtyChanged)
        assertTrue(voiceFile.isDirty)

        clean()

        assertFalse(voiceFile.isDirty)
    }

    @Test
    fun testChangeVdfVersion() {
        testDefault()

        voiceFile.vdfVersion = 2000

        assertTrue(changed)

        assertFalse(vdfTypeChanged)
        assertEquals(VDFType.User, voiceFile.vdfType)

        assertFalse(transmitterTypeChanged)
        assertEquals(TransmitterType.mc28, voiceFile.transmitterType)

        assertTrue(vdfVersionChanged)
        assertEquals(2000, voiceFile.vdfVersion)

        assertFalse(countryChanged)
        assertEquals(CountryCode.GLOBAL, voiceFile.country)

        assertFalse(voiceListChanged)
        assertFalse(sizeChanged)
        assertEquals(0, voiceFile.size)
        assertEquals(0, voiceFile.rawDataSize)

        assertFalse(systemVDFChanged)
        assertFalse(voiceFile.isSystemVDF)

        assertTrue(dirtyChanged)
        assertTrue(voiceFile.isDirty)

        clean()

        assertFalse(voiceFile.isDirty)
    }

    @Test
    fun testChangeCountry() {
        testDefault()

        voiceFile.country = CountryCode.US

        assertTrue(changed)

        assertFalse(vdfTypeChanged)
        assertEquals(VDFType.User, voiceFile.vdfType)

        assertFalse(transmitterTypeChanged)
        assertEquals(TransmitterType.mc28, voiceFile.transmitterType)

        assertFalse(vdfVersionChanged)
        assertEquals(3000, voiceFile.vdfVersion)

        assertTrue(countryChanged)
        assertEquals(CountryCode.US, voiceFile.country)

        assertFalse(voiceListChanged)
        assertFalse(sizeChanged)
        assertEquals(0, voiceFile.size)
        assertEquals(0, voiceFile.rawDataSize)

        assertFalse(systemVDFChanged)
        assertFalse(voiceFile.isSystemVDF)

        assertTrue(dirtyChanged)
        assertTrue(voiceFile.isDirty)

        clean()

        assertFalse(voiceFile.isDirty)
    }

    @Test
    fun testVoiceListAdd() {
        testDefault()

        voiceFile.voiceList.add(VoiceData("test", byteArrayOf(1,2,3)))

        assertTrue(changed)

        assertFalse(vdfTypeChanged)
        assertEquals(VDFType.User, voiceFile.vdfType)

        assertFalse(transmitterTypeChanged)
        assertEquals(TransmitterType.mc28, voiceFile.transmitterType)

        assertFalse(vdfVersionChanged)
        assertEquals(3000, voiceFile.vdfVersion)

        assertFalse(countryChanged)
        assertEquals(CountryCode.GLOBAL, voiceFile.country)

        assertTrue(voiceListChanged)
        assertTrue(sizeChanged)
        assertEquals(1, voiceFile.size)
        assertEquals(3, voiceFile.rawDataSize)

        assertFalse(systemVDFChanged)
        assertFalse(voiceFile.isSystemVDF)

        assertTrue(dirtyChanged)
        assertTrue(voiceFile.isDirty)

        clean()

        assertFalse(voiceFile.isDirty)
    }
    
    @Test
    fun testVoiceListRename() {
        testVoiceListAdd()

        val element = voiceFile.voiceList[0]
        element.name = "renamed"
        voiceFile.voiceList[0] = element

        assertTrue(changed)

        assertFalse(vdfTypeChanged)
        assertEquals(VDFType.User, voiceFile.vdfType)

        assertFalse(transmitterTypeChanged)
        assertEquals(TransmitterType.mc28, voiceFile.transmitterType)

        assertFalse(vdfVersionChanged)
        assertEquals(3000, voiceFile.vdfVersion)

        assertFalse(countryChanged)
        assertEquals(CountryCode.GLOBAL, voiceFile.country)

        assertTrue(voiceListChanged)
        assertFalse(sizeChanged)
        assertEquals(1, voiceFile.size)
        assertEquals(3, voiceFile.rawDataSize)

        assertFalse(systemVDFChanged)
        assertFalse(voiceFile.isSystemVDF)

        assertTrue(dirtyChanged)
        assertTrue(voiceFile.isDirty)

        clean()

        assertFalse(voiceFile.isDirty)
    }

    @Test
    fun testVoiceListRemove() {
        testVoiceListAdd()

        voiceFile.voiceList.removeAt(0)

        assertTrue(changed)

        assertFalse(vdfTypeChanged)
        assertEquals(VDFType.User, voiceFile.vdfType)

        assertFalse(transmitterTypeChanged)
        assertEquals(TransmitterType.mc28, voiceFile.transmitterType)

        assertFalse(vdfVersionChanged)
        assertEquals(3000, voiceFile.vdfVersion)

        assertFalse(countryChanged)
        assertEquals(CountryCode.GLOBAL, voiceFile.country)

        assertTrue(voiceListChanged)
        assertTrue(sizeChanged)
        assertEquals(0, voiceFile.size)
        assertEquals(0, voiceFile.rawDataSize)

        assertFalse(systemVDFChanged)
        assertFalse(voiceFile.isSystemVDF)

        assertTrue(dirtyChanged)
        assertTrue(voiceFile.isDirty)

        clean()

        assertFalse(voiceFile.isDirty)
    }
}