/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.mdlviewer.swing;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.SwingUtilities;

import de.treichels.hott.messages.Messages;
import de.treichels.hott.util.Util;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class Launcher {
    public static final String LOG_DIR = "log.dir"; //$NON-NLS-1$
    public static final String MDL_DIR = "mdl.dir"; //$NON-NLS-1$
    public static final String PROGRAM_DIR = "program.dir"; //$NON-NLS-1$
    public static final String PROGRAM_VERSION = "program.version"; //$NON-NLS-1$

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
                final String version = Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version"), //$NON-NLS-1$ //$NON-NLS-2$
                        attributes.getValue("Implementation-Build"));
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

        if (Util.INSTANCE.getDEBUG()) {
            System.out.printf("program.dir = %s%n", System.getProperty(PROGRAM_DIR)); //$NON-NLS-1$
            System.out.printf("mdl.dir = %s%n", System.getProperty(MDL_DIR)); //$NON-NLS-1$
            System.out.printf("log.dir = %s%n", System.getProperty(LOG_DIR)); //$NON-NLS-1$
        }
    }

    public static void main(final String[] args) throws Exception {
        initSystemProperties();
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
        SwingUtilities.invokeLater(() -> new SimpleGUI());
    }
}