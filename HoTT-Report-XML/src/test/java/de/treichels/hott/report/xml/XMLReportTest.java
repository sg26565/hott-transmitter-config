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
package de.treichels.hott.report.xml;

import de.treichels.hott.model.BaseModel;
import de.treichels.hott.model.winged.WingedModel;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class XMLReportTest {
    @Test
    public void testProcessXML() throws JAXBException {
        final BaseModel model = new WingedModel();
        model.setModelName("testModel1"); //$NON-NLS-1$

        final String data = XMLReport.generateXML(model);
        assertTrue(data.contains("<modelName>testModel1</modelName>")); //$NON-NLS-1$
    }
}
