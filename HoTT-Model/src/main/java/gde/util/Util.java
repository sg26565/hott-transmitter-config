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
package gde.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class Util {
    private static final String PARAM_OFFLINE = "offline";
    private static final String PARAM_DEBUG = "debug";
    private static final String LATEST_VERSIONS_URL = "https://drive.google.com/uc?export=download&id=0B_uPguA0xiT4SUl1V1VKYXFjWHc";
    private static final Properties latestVersions = new Properties();
    public static final boolean OFFLINE = Boolean.getBoolean(PARAM_OFFLINE);
    public static final boolean DEBUG = Boolean.getBoolean(PARAM_DEBUG);

    public static String dumpData(final byte[] data) {
        return dumpData(data, 0);
    }

    public static String dumpData(final byte[] data, final int baseAddress) {
        final StringBuilder sb = new StringBuilder();

        if (data != null) {
            final int len = data.length;
            int addr = 0;

            while (addr < len) {
                sb.append(String.format("0x%04x: ", baseAddress + addr)); //$NON-NLS-1$

                for (int i = 0; i < 16; i++)
                    if (addr + i < len) {
                        switch (i) {
                        case 4:
                        case 12:
                            sb.append(':');
                            break;

                        case 0:
                        case 8:
                            sb.append('|');
                            break;

                        default:
                            sb.append(' ');
                            break;
                        }

                        sb.append(String.format("%02x", data[addr + i])); //$NON-NLS-1$
                    } else
                        sb.append("   "); //$NON-NLS-1$

                sb.append("| "); //$NON-NLS-1$

                for (int i = 0; i < 16; i++)
                    if (addr + i < len) {
                        final char c = (char) (data[addr + i] & 0xff);
                        if (c >= 0x20 && c <= 0x7e)
                            sb.append(c);
                        else
                            sb.append('.');
                    }

                sb.append('\n');
                addr += 16;
            }
        }

        return sb.toString();
    }

    public static String getLatestVersion(final String key) {
        if (latestVersions.isEmpty()) try (InputStream is = new URL(LATEST_VERSIONS_URL).openStream(); InputStreamReader reader = new InputStreamReader(is)) {
            latestVersions.load(reader);
            latestVersions.setProperty(PARAM_OFFLINE, Boolean.FALSE.toString());
        } catch (final IOException e) {
            latestVersions.setProperty(PARAM_OFFLINE, Boolean.TRUE.toString());
        }

        return latestVersions.getProperty(key, null);
    }
}
