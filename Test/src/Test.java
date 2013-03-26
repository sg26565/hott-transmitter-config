import gde.model.BaseModel;
import gde.model.helicopter.HeliCopterMixer;
import gde.model.helicopter.HelicopterModel;
import gde.model.helicopter.HelicopterTrim;
import gde.model.winged.WingedMixer;
import gde.model.winged.WingedModel;
import gde.model.winged.WingedTrim;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.treichels.hott.HoTTDecoder;

public class Test {
	public static void main(final String[] args) throws IOException, URISyntaxException, JAXBException {
		// create a JAXB marshaller to convert the data model into XML
		final JAXBContext ctx = JAXBContext.newInstance(WingedModel.class, WingedMixer.class, WingedTrim.class, HelicopterModel.class, HeliCopterMixer.class,
				HelicopterTrim.class);
		final Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		m.marshal(getModel("aMERLIN.mdl"), System.out);
		m.marshal(getModel("hSPACER-4X.mdl"), System.out);
	}

	private static BaseModel getModel(final String fileName) throws IOException, URISyntaxException {
		// lookup the binary model file from the class path
		final URL url = ClassLoader.getSystemResource(fileName);

		// decode the model file into the data model
		return HoTTDecoder.decode(new File(url.toURI()));
	}
}
