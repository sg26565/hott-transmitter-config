package gde.mdl.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import freemarker.ext.beans.JavaBeansIntrospector;

public class Launcher {
  public static final String LOG_DIR         = "log.dir";
  public static final String MDL_DIR         = "mdl.dir";
  public static final String PROGRAM_DIR     = "program.dir";
  public static final String PROGRAM_VERSION = "program.version";

  public static void extractFont() throws IOException {
    final File file = new File(System.getProperty("java.io.tmpdir"), "Arial.ttf");
    InputStream is = null;
    OutputStream os = null;

    try {
      is = ClassLoader.getSystemResourceAsStream("Arial.ttf");
      os = new FileOutputStream(file);

      final byte[] buffer = new byte[1024];
      while (true) {
        final int len = is.read(buffer);
        if (len == -1) {
          break;
        }
        os.write(buffer, 0, len);
      }
    } finally {
      if (is != null) {
        is.close();
      }

      if (os != null) {
        os.close();
      }
    }
  }

  /**
   * Initialize logfile.
   * 
   * @throws SecurityException
   * @throws IOException
   */
  public static void initLogging() throws SecurityException, IOException {
    final Logger global = Logger.getLogger("");

    // remove console handler - we don't run from a command line
    for (final Handler handler : global.getHandlers()) {
      global.removeHandler(handler);
    }

    // Setup logfile
    final Handler handler = new FileHandler(System.getProperty(LOG_DIR) + "/HoTTGUI.log");
    handler.setLevel(Level.INFO);
    handler.setFormatter(new SimpleFormatter());

    global.addHandler(handler);
    global.setLevel(Level.INFO);

    final Logger logger = Logger.getLogger(Launcher.class.getName());
    logger.log(Level.INFO, "program.dir =  " + System.getProperty(PROGRAM_DIR));
    logger.log(Level.INFO, "mdl.dir =  " + System.getProperty(MDL_DIR));
    logger.log(Level.INFO, "log.dir =  " + System.getProperty(LOG_DIR));
  }

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
      Class.forName("org.eclipse.swt.widgets.Dialog");
    } catch (final ClassNotFoundException e) {
      // nope - we need to add it

      // determine correct swt jar
      String osName = System.getProperty("os.name").toLowerCase();
      if (osName.startsWith("linux")) {
        osName = "linux";
      } else if (osName.startsWith("windows")) {
        osName = "windows";
      } else if (osName.startsWith("mac")) {
        osName = "mac";
      }

      final String osArch = System.getProperty("os.arch").toLowerCase();
      final String swtJarName = String.format("swt-%s-%s.jar", osName, osArch);
      final File swtJar = new File(new File(System.getProperty(PROGRAM_DIR), "swt"), swtJarName);
      if (!swtJar.exists() || !swtJar.isFile()) {
        throw new ClassNotFoundException("SWT library is missing: " + swtJar.getAbsolutePath());
      }

      // add swt to classpath
      // Note: this is a hack. Not all platforms may use URLClassLoader as
      // standard. Make protected addURL Method available via reflection and
      // invoke it
      final URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
      final Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
      method.setAccessible(true);
      method.invoke(classLoader, new Object[] { swtJar.toURI().toURL() });
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

    if (source.getName().endsWith(".jar")) {
      // read program version from manifest
      JarFile jarFile = null;
      try {
        jarFile = new JarFile(source);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();
        final String version = attributes.getValue("Implementation-Version") + " Rev. " + attributes.getValue("Implementation-Build").substring(0, 7);
        System.setProperty(Launcher.PROGRAM_VERSION, version);
      } finally {
        if (jarFile != null) {
          jarFile.close();
        }
      }
    } else {
      if (!System.getProperties().containsKey(PROGRAM_VERSION)) {
        System.setProperty(Launcher.PROGRAM_VERSION, "unknown");
      }

      // application was packaged as individual class files, find the classes
      // directory
      while (!source.getName().equals("classes")) {
        source = source.getParentFile();
      }
    }

    // get the parent directory containing the jar file or the classes
    // directory
    File programDir = source.getParentFile();

    // if we are running inside Eclipse in the target directory, step up to
    // the project level
    if (programDir.getName().equals("target")) {
      programDir = programDir.getParentFile();
    }

    System.setProperty(PROGRAM_DIR, programDir.getAbsolutePath());

    if (!System.getProperties().containsKey(MDL_DIR)) {
      System.setProperty(MDL_DIR, System.getProperty(PROGRAM_DIR));
    }

    if (!System.getProperties().containsKey(LOG_DIR)) {
      System.setProperty(LOG_DIR, System.getProperty(PROGRAM_DIR));
    }
  }

  public static void main(final String[] args) throws Exception {
    initSystemProperties();
    initLogging();
    initSwt();
    extractFont();
    startSwtApplication();
  }

  /**
   * Start the application.
   * 
   * @throws ClassNotFoundException
   * @throws NoSuchMethodException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static void startSwingApplication() {
    new SimpleGUI().showInFrame();
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
    // Call main method of SwtMdlBrower via reflection to avoid load time class
    // loading of SWT (which will fail as SWT is not yet on the class path).
    final Class<?> mainDialog = Class.forName("gde.mdl.ui.SwtMdlBrowser");
    final Method main = mainDialog.getMethod("main", String[].class);
    main.invoke(null, new Object[] { new String[] {} });
  }

  @SuppressWarnings("unused")
  private Class<JavaBeansIntrospector> class1;
}