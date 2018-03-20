package de.treichels.hott.mdlviewer.swt;

import de.treichels.hott.messages.Messages;
import de.treichels.hott.util.Util;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Launcher {
    @NonNls
    private static final String LOG_DIR = "log.dir";
    @NonNls
    public static final String MDL_DIR = "mdl.dir";
    @NonNls
    private static final String PROGRAM_DIR = "program.dir";
    @NonNls
    public static final String PROGRAM_VERSION = "program.version";

    /**
     * Initialize SWT. Make sure that the correct SWT library is in the classpath.
     */
    private static void initSwt() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            MalformedURLException, ClassNotFoundException {
        try {
            // check if swt is already in the classpath
            Class.forName("org.eclipse.swt.widgets.Dialog");
        } catch (final ClassNotFoundException e) {
            // nope - we need to add it

            // determine correct swt jar
            @NonNls String osName = System.getProperty("os.name").toLowerCase();
            if (osName.startsWith("linux"))
                osName = "linux";
            else if (osName.startsWith("windows"))
                osName = "windows";
            else if (osName.startsWith("mac")) osName = "mac";

            final String osArch = System.getProperty("os.arch").toLowerCase();
            final String swtJarName = String.format("swt-%s-%s.jar", osName, osArch);
            final File swtJar = new File(new File(System.getProperty(PROGRAM_DIR), "swt"), swtJarName);
            if (!swtJar.exists() || !swtJar.isFile()) throw new ClassNotFoundException(Messages.getString("Launcher.SWTNotFound", swtJar.getAbsolutePath()));

            // add swt to classpath
            // Note: this is a hack. Not all platforms may use URLClassLoader as
            // standard. Make protected addURL Method available via reflection
            // and
            // invoke it
            final URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
            final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, swtJar.toURI().toURL());
        }
    }

    /**
     * Initialize system properties.
     */
    public static void initSystemProperties() throws URISyntaxException, IOException {
        // get the location of this class
        final URL url = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
        File source = new File(url.toURI());

        if (source.getName().endsWith(".jar") || source.getName().endsWith(".exe")) {
            // read program version from manifest
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(source);
                final Manifest manifest = jarFile.getManifest();
                @NonNls final Attributes attributes = manifest.getMainAttributes();
                final String version = Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version"),
                        attributes.getValue("Implementation-Build"));
                System.setProperty(Launcher.PROGRAM_VERSION, version);
            } finally {
                if (jarFile != null) jarFile.close();
            }
        } else {
            if (!System.getProperties().containsKey(PROGRAM_VERSION)) System.setProperty(Launcher.PROGRAM_VERSION, Messages.getString("Launcher.Unknown"));

            // application was packaged as individual class files, find the
            // classes
            // directory
            while (!source.getName().equals("classes"))
                source = source.getParentFile();
        }

        // get the parent directory containing the jar file or the classes
        // directory
        File programDir = source.getParentFile();

        // if we are running inside Eclipse in the target directory, step up to
        // the project level
        if (programDir.getName().equals("target")) programDir = programDir.getParentFile();

        System.setProperty(PROGRAM_DIR, programDir.getAbsolutePath());

        if (!System.getProperties().containsKey(MDL_DIR)) System.setProperty(MDL_DIR, System.getProperty(PROGRAM_DIR));

        if (!System.getProperties().containsKey(LOG_DIR)) System.setProperty(LOG_DIR, System.getProperty(PROGRAM_DIR));

        if (Util.INSTANCE.getDEBUG()) {
            System.out.printf("program.dir = %s%n", System.getProperty(PROGRAM_DIR));
            System.out.printf("mdl.dir = %s%n", System.getProperty(MDL_DIR));
            System.out.printf("log.dir = %s%n", System.getProperty(LOG_DIR));
        }
    }

    public static void main(final String[] args) throws Exception {
        initSystemProperties();
        initSwt();
        startSwtApplication();
    }

    /**
     * Start the application.
     */
    private static void startSwtApplication() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException,
            NoSuchMethodException, SecurityException {
        // Call main method of SwtMdlBrower via reflection to avoid load time
        // class
        // loading of SWT (which will fail as SWT is not yet on the class path).
        final Class<?> mainDialog = Class.forName("de.treichels.hott.mdlviewer.swt.SwtMdlBrowser");
        final Method main = mainDialog.getMethod("main", String[].class);
        main.invoke(null, new Object[] { new String[] {} });
    }
}