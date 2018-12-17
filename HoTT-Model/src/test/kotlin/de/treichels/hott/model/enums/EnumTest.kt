package de.treichels.hott.model.enums

import org.junit.Assert.*
import org.junit.Test
import java.io.File

class EnumTest {
    companion object {
        val packageNames = listOf("de.treichels.hott.model.enums", "de.treichels.hott.serial", "de.treichels.hott.model.voice")
    }

    @Test
    fun test() {
        packageNames.forEach { packageName ->
            Thread.currentThread().contextClassLoader
                    .getResources(packageName.replace(".", "/")).asSequence() // sequence of URLs
                    .map { File(it.toURI()) }.filter { it.isDirectory } // sequence of directories
                    .flatMap { it.listFiles().asSequence() }.filter { it.name.endsWith(".class") } // sequence of .class files
                    .map { "$packageName.${it.nameWithoutExtension}" }.map { Class.forName(it) }.filter { it.isEnum } // sequence of enums
                    .filter { it.declaredMethods.find { it.name == "toString" } != null } // with declared toString method
                    .forEach { clazz ->
                        val toStringMethod = clazz.getMethod("toString")
                        val nameMethod = clazz.getMethod("name")

                        clazz.declaredFields.filter { it.isEnumConstant }.forEach { field ->
                            val enumConstant = field.get(null)
                            val name = nameMethod.invoke(enumConstant) as String
                            val string = toStringMethod.invoke(enumConstant) as String

                            assertEquals(field.name, name)
                            assertNotNull(string)
                            assertNotEquals("[$name]", string)
                        }
                    }
        }
    }
}
