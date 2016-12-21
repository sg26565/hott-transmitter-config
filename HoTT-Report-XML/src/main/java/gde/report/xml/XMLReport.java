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

package gde.report.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import gde.model.BaseModel;
import gde.model.CurveMixer;
import gde.model.LinearMixer;
import gde.model.helicopter.HelicopterModel;
import gde.model.helicopter.HelicopterPhase;
import gde.model.winged.WingedModel;
import gde.model.winged.WingedPhase;

/**
 * @author oli@treichels.de
 */
public class XMLReport {
	private static final JAXBContext CTX;
	private static final Marshaller MARSHALLER;

	static {
		// setup JAXB
		try {
			CTX = JAXBContext.newInstance(WingedModel.class, WingedPhase.class, HelicopterModel.class, HelicopterPhase.class, LinearMixer.class,
					CurveMixer.class);
			MARSHALLER = CTX.createMarshaller();
			MARSHALLER.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (final JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public static String generateXML(final BaseModel model) throws JAXBException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MARSHALLER.marshal(model, baos);
		return baos.toString();
	}

	public static void generateXsd(final File file) throws IOException {
		CTX.generateSchema(new SchemaOutputResolver() {
			@Override
			public Result createOutput(final String namespaceUri, final String suggestedFileName) throws IOException {
				final StreamResult result = new StreamResult(file);
				return result;
			}
		});
	}
}
