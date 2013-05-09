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
package gde.gui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import com.itextpdf.text.DocumentException;

import freemarker.template.TemplateException;
import gde.model.BaseModel;
import gde.report.Report;

/**
 * @author oli
 */
public class GenerateDemoFiles {
	public static void main(final String[] args) throws IOException, URISyntaxException, JAXBException, TemplateException, DocumentException {
		final File mainJar = new File(GenerateDemoFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		final File programDir = mainJar.getParentFile().getParentFile();
		System.setProperty("program.dir", programDir.getAbsolutePath());

		final File dir = new File("demo");

		Report.setSuppressExceptions(false);
		Report.generateXsd(new File(dir, "HoTTGUI.xsd"));

		for (final File file : dir.listFiles()) {
			if (file.getName().endsWith(".mdl")) {
				final String baseName = file.getName().substring(0, file.getName().length() - 4);

				final BaseModel model = Report.getModel(file);

				String data = Report.generateXML(model);
				Report.save(new File(dir, baseName + ".xml"), data);

				data = Report.generateHTML(model);
				Report.save(new File(dir, baseName + ".html"), data);
				Report.savePDF(new File(dir, baseName + ".pdf"), data);
			}
		}
	}
}
