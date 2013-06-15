package gde.mdl.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Launcher {
	private static final Logger	LOG							= Logger.getLogger(Launcher.class.getName());
	public static final String	LOG_DIR					= "log.dir";
	public static final String	MDL_DIR					= "mdl.dir";
	public static final String	PROGRAM_DIR			= "program.dir";
	public static final String	PROGRAM_VERSION	= "program.version";

	public static void main(final String[] args) throws Exception {
		initSystemProperties();
		initLogging();
		initSwt();
		startApplication();
	}

	/**
	 * Start the application.
	 * 
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void startApplication() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class<?> mainDialog = Class.forName("gde.mdl.ui.SwtMdlBrowser");
		Method main = mainDialog.getMethod("main", String[].class);
		main.invoke(null, new Object[] { new String[] {} });
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
	 */
	public static void initSwt() throws MalformedURLException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		try {
			// check if swt is already in the classpath
			Class.forName("org.eclipse.swt.widgets.Dialog");
		}
		catch (ClassNotFoundException e) {
			// nope - we need to add it

			//determine correct swt jar
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.startsWith("linux")) {
				osName = "linux";
			}
			else if (osName.startsWith("windows")) {
				osName = "windows";
			}
			else if (osName.startsWith("mac")) {
				osName = "mac";
			}

			final String osArch = System.getProperty("os.arch").toLowerCase();
			final String swtJarName = String.format("swt-%s-%s.jar", osName, osArch);
			final File swtJar = new File(new File(System.getProperty(PROGRAM_DIR), "swt"), swtJarName);
			if (!swtJar.exists() || !swtJar.isFile()) {
				throw new RuntimeException("SWT library is missing: " + swtJar.getAbsolutePath());
			}

			// add swt to classpath
			// Note: this is a hack. Not all platforms may use URLClassLoader as standard
			URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(classLoader, new Object[] { swtJar.toURI().toURL() });
		}
	}

	/**
	 * Initialize logfile.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public static void initLogging() throws SecurityException, IOException {
		// Setup logfile
		Handler handler = new FileHandler(System.getProperty(LOG_DIR) + "/HoTTGUI.log");
		handler.setLevel(Level.INFO);
		handler.setFormatter(new SimpleFormatter());
		LOG.addHandler(handler);
		LOG.setLevel(Level.INFO);

		LOG.log(Level.INFO, "program.dir =  " + System.getProperty(PROGRAM_DIR));
		LOG.log(Level.INFO, "mdl.dir =  " + System.getProperty(MDL_DIR));
		LOG.log(Level.INFO, "log.dir =  " + System.getProperty(LOG_DIR));
	}

	/**
	 * Initialize system properties.
	 * 
	 * @throws URISyntaxException
	 */
	public static void initSystemProperties() throws URISyntaxException {
		if (!System.getProperties().contains(PROGRAM_DIR)) {
			// get the location of this class
			File source = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			// if application was packaged as individual class files, find the classes directory
			if (!source.getName().endsWith(".jar")) {
				while (!source.getName().equals("classes")) {
					source = source.getParentFile();
				}
			}

			// get the parent directory containing the jar file or the classes directory
			File programDir = source.getParentFile();

			// if we are running inside Eclipse in the target directory, step up to the project level
			if (programDir.getName().equals("target")) {
				programDir = programDir.getParentFile();
			}

			System.setProperty(PROGRAM_DIR, programDir.getAbsolutePath());
		}

		if (!System.getProperties().containsKey(MDL_DIR)) {
			System.setProperty(MDL_DIR, System.getProperty(PROGRAM_DIR));
		}

		if (!System.getProperties().containsKey(LOG_DIR)) {
			System.setProperty(LOG_DIR, System.getProperty(PROGRAM_DIR));
		}
	}
}