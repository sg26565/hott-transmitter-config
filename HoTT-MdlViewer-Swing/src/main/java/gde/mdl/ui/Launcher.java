/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gde.mdl.ui;

import gde.mdl.messages.Messages;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.SwingUtilities;

/**
 * @author oli@treichels.de
 */
public class Launcher {

  public static final String LOG_DIR         = "log.dir";        //$NON-NLS-1$
  public static final String MDL_DIR         = "mdl.dir";        //$NON-NLS-1$
  public static final String PROGRAM_DIR     = "program.dir";    //$NON-NLS-1$
  public static final String PROGRAM_VERSION = "program.version"; //$NON-NLS-1$

  /**
   * Initialize logfile.
   * 
   * @throws SecurityException
   * @throws IOException
   */
  public static void initLogging() throws SecurityException, IOException {
    final Logger global = Logger.getLogger(""); //$NON-NLS-1$

    // remove console handler - we don't run from a command line
    for (final Handler handler : global.getHandlers()) {
      global.removeHandler(handler);
    }

    // Setup logfile
    final Handler handler = new FileHandler(System.getProperty(LOG_DIR) + "/MdlViewer.log"); //$NON-NLS-1$
    handler.setLevel(Level.INFO);
    handler.setFormatter(new SimpleFormatter());

    global.addHandler(handler);
    global.setLevel(Level.INFO);

    final Logger logger = Logger.getLogger(Launcher.class.getName());
    logger.log(Level.INFO, "program.dir =  " + System.getProperty(PROGRAM_DIR)); //$NON-NLS-1$
    logger.log(Level.INFO, "mdl.dir =  " + System.getProperty(MDL_DIR)); //$NON-NLS-1$
    logger.log(Level.INFO, "log.dir =  " + System.getProperty(LOG_DIR)); //$NON-NLS-1$
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

    if (source.getName().endsWith(".jar")) { //$NON-NLS-1$
      // read program version from manifest
      JarFile jarFile = null;
      try {
        jarFile = new JarFile(source);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();
        final String version = Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version")); //$NON-NLS-1$ //$NON-NLS-2$
        System.setProperty(Launcher.PROGRAM_VERSION, version);
      } finally {
        if (jarFile != null) {
          jarFile.close();
        }
      }
    } else {
      if (!System.getProperties().containsKey(PROGRAM_VERSION)) {
        System.setProperty(Launcher.PROGRAM_VERSION, Messages.getString("Launcher.Unknown")); //$NON-NLS-1$
      }

      // application was packaged as individual class files, find the classes
      // directory
      while (!source.getName().equals("classes")) { //$NON-NLS-1$
        source = source.getParentFile();
      }
    }

    // get the parent directory containing the jar file or the classes
    // directory
    File programDir = source.getParentFile();

    // if we are running inside Eclipse in the target directory, step up to
    // the project level
    if (programDir.getName().equals("target")) { //$NON-NLS-1$
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
    startSwingApplication();
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
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new SimpleGUI();
      }
    });
  }
}