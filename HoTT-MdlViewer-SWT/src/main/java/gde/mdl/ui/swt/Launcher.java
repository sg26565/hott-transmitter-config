package gde.mdl.ui.swt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Launcher extends gde.mdl.ui.swing.Launcher {
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

  public static void main(final String[] args) throws Exception {
    initSystemProperties();
    initLogging();
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
    // Call main method of SwtMdlBrower via reflection to avoid load time class
    // loading of SWT (which will fail as SWT is not yet on the class path).
    final Class<?> mainDialog = Class.forName("gde.mdl.ui.swt.SwtMdlBrowser");
    final Method main = mainDialog.getMethod("main", String[].class);
    main.invoke(null, new Object[] { new String[] {} });
  }
}