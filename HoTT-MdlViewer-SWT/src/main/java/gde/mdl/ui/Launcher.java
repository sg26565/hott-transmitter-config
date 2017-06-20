package gde.mdl.ui;

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

import gde.mdl.messages.Messages;
import gde.util.Util;

public class Launcher {
    public static final String LOG_DIR = "log.dir"; //$NON-NLS-1$
    public static final String MDL_DIR = "mdl.dir"; //$NON-NLS-1$
    public static final String PROGRAM_DIR = "program.dir"; //$NON-NLS-1$
    public static final String PROGRAM_VERSION = "program.version"; //$NON-NLS-1$

    /**
     * Initialize SWT. Make sure that the correct SWT library is in the classpath.
     *
     * @throws MalformedURLException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     */
    public static void initSwt() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            MalformedURLException, ClassNotFoundException {
        try {
            // check if swt is already in the classpath
            Class.forName("org.eclipse.swt.widgets.Dialog"); //$NON-NLS-1$
        } catch (final ClassNotFoundException e) {
            // nope - we need to add it

            // determine correct swt jar
            String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
            if (osName.startsWith("linux"))
                osName = "linux"; //$NON-NLS-1$
            else if (osName.startsWith("windows"))
                osName = "windows"; //$NON-NLS-1$
            else if (osName.startsWith("mac")) osName = "mac"; //$NON-NLS-1$

            final String osArch = System.getProperty("os.arch").toLowerCase(); //$NON-NLS-1$
            final String swtJarName = String.format("swt-%s-%s.jar", osName, osArch); //$NON-NLS-1$
            final File swtJar = new File(new File(System.getProperty(PROGRAM_DIR), "swt"), swtJarName); //$NON-NLS-1$
            if (!swtJar.exists() || !swtJar.isFile()) throw new ClassNotFoundException(Messages.getString("Launcher.SWTNotFound", swtJar.getAbsolutePath())); //$NON-NLS-1$

            // add swt to classpath
            // Note: this is a hack. Not all platforms may use URLClassLoader as
            // standard. Make protected addURL Method available via reflection
            // and
            // invoke it
            final URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
            final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class); //$NON-NLS-1$
            method.setAccessible(true);
            method.invoke(classLoader, swtJar.toURI().toURL());
        }
    }

    /**
     * Initialize system properties.
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void initSystemProperties() throws URISyntaxException, IOException {
        // get the location of this class
        final URL url = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
        File source = new File(url.toURI());

        if (source.getName().endsWith(".jar") || source.getName().endsWith(".exe")) { //$NON-NLS-1$
            // read program version from manifest
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(source);
                final Manifest manifest = jarFile.getManifest();
                final Attributes attributes = manifest.getMainAttributes();
                final String version = Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version")); //$NON-NLS-1$ //$NON-NLS-2$
                System.setProperty(Launcher.PROGRAM_VERSION, version);
            } finally {
                if (jarFile != null) jarFile.close();
            }
        } else {
            if (!System.getProperties().containsKey(PROGRAM_VERSION)) System.setProperty(Launcher.PROGRAM_VERSION, Messages.getString("Launcher.Unknown")); //$NON-NLS-1$

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

        if (Util.DEBUG) {
            System.out.printf("program.dir = %s%n", System.getProperty(PROGRAM_DIR)); //$NON-NLS-1$
            System.out.printf("mdl.dir = %s%n", System.getProperty(MDL_DIR)); //$NON-NLS-1$
            System.out.printf("log.dir = %s%n", System.getProperty(LOG_DIR)); //$NON-NLS-1$
        }
    }

    public static void main(final String[] args) throws Exception {
        initSystemProperties();
        initSwt();
        startSwtApplication();
    }

    /**
     * Start the application.
     *
     * @throws IllegalArgumentException
     *
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SecurityException
     */
    public static void startSwtApplication() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException,
            NoSuchMethodException, SecurityException {
        // Call main method of SwtMdlBrower via reflection to avoid load time
        // class
        // loading of SWT (which will fail as SWT is not yet on the class path).
        final Class<?> mainDialog = Class.forName("gde.mdl.ui.SwtMdlBrowser"); //$NON-NLS-1$
        final Method main = mainDialog.getMethod("main", String[].class); //$NON-NLS-1$
        main.invoke(null, new Object[] { new String[] {} });
    }
}