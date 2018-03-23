package de.treichels.hott.mdlviewer.swt

import de.treichels.hott.messages.Messages
import de.treichels.hott.util.Util
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

private const val LOG_DIR = "log.dir"
private const val MDL_DIR = "mdl.dir"
private const val PROGRAM_DIR = "program.dir"
private const val PROGRAM_VERSION = "program.version"
private const val MAIN_CLASS_NAME = "de.treichels.hott.mdlviewer.swt.SwtMdlBrowser"

fun main(vararg args: String) {
    Launcher.instance.launch()
}

class Launcher {
    companion object {
        val instance = Launcher()
    }

    // get the location of this class
    private val codeLocation: URL = javaClass.protectionDomain.codeSource.location
    private var sourceFile = File(codeLocation.toURI())
    val programDir: File
    val mdlDir: File
    val logDir: File
    val version: String

    fun launch() {
        // check if swt is already in the classpath
        val classLoader = try {
            Class.forName("org.eclipse.swt.widgets.Composite")
        } catch (e: ClassNotFoundException) {
            // nope - we need to add it

            // determine correct swt jar
            val osName = System.getProperty("os.name").toLowerCase()
            val arch = System.getProperty("os.arch").toLowerCase()

            val name = when {
                osName.startsWith("linux") -> "linux"
                osName.startsWith("windows") -> "windows"
                osName.startsWith("mac") -> "mac"
                else -> osName
            }

            val swtJarName = "swt-$name-$arch.jar"
            val swtDir = File(programDir, "swt")
            val swtJarFile = File(swtDir, swtJarName)
            if (!(swtJarFile.exists() && swtJarFile.isFile && swtJarFile.canRead()))
                throw ClassNotFoundException(Messages.getString("Launcher.SWTNotFound", swtJarFile.absolutePath))

            val swtJarFileURL = swtJarFile.toURI().toURL()
            val systemClassLoader = ClassLoader.getSystemClassLoader()

            // add swt to classpath
            if (systemClassLoader is URLClassLoader) {
                val addMethod = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
                addMethod.isAccessible = true
                addMethod.invoke(systemClassLoader, swtJarFileURL)

                systemClassLoader
            } else
                throw UnsupportedOperationException("The system classloader is not a subtype of URLClassLoader. Therefore, we cannot add SWT jar dynamically to the classpath.")
        }

        // Call main method via reflection to avoid load time class loading of SWT (which will fail as SWT is not yet on the class path).
        val mainClass = Class.forName(MAIN_CLASS_NAME)
        val mainMethod = mainClass.getMethod("main", Array<String>::class.java)
        mainMethod.isAccessible = true
        mainMethod.invoke(null, arrayOf<String>())
    }

    /** Initialize system properties. */
    init {
        version = if (sourceFile.name.endsWith(".jar") || sourceFile.name.endsWith(".exe")) {
            // read program version from manifest
            JarFile(sourceFile).use { jarfile ->
                val attributes = jarfile.manifest.mainAttributes
                Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version"), attributes.getValue("Implementation-Build"))
            }
        } else {
            // application was packaged as individual class files, find the classes directory
            while (sourceFile.name != "classes")
                sourceFile = sourceFile.parentFile

            // version is unknown
            System.getProperty(PROGRAM_VERSION, Messages.getString("Launcher.Unknown"))
        }

        // get the parent directory containing the jar file or the classes directory
        val parentFile = sourceFile.parentFile

        // if we are running inside Eclipse in the target directory, step up to the project level
        programDir = if (parentFile.name == "target") parentFile.parentFile else parentFile
        mdlDir = File(System.getProperty(MDL_DIR, programDir.absolutePath))
        logDir = File(System.getProperty(LOG_DIR, programDir.absolutePath))

        System.setProperty(PROGRAM_VERSION, version)
        System.setProperty(PROGRAM_DIR, programDir.absolutePath)
        System.setProperty(MDL_DIR, mdlDir.absolutePath)
        System.setProperty(LOG_DIR, logDir.absolutePath)

        if (Util.DEBUG) {
            System.out.printf("program.dir = %s%n", System.getProperty(PROGRAM_DIR))
            System.out.printf("mdl.dir = %s%n", System.getProperty(MDL_DIR))
            System.out.printf("log.dir = %s%n", System.getProperty(LOG_DIR))
        }
    }
}