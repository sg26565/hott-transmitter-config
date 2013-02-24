import gde.model.WingedMixer;
import gde.model.WingedModel;
import gde.model.WingedTrim;

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
		// lookup the binary model file from the class path - for testing
		// purposes
		final URL url = ClassLoader.getSystemResource("aMERLIN.mdl");

		// decode the model file into the data model
		final WingedModel model = (WingedModel) HoTTDecoder.decode(new File(url.toURI()));

		// create a JAXB marshaller to convert the data model into XML
		final JAXBContext ctx = JAXBContext.newInstance(WingedModel.class, WingedMixer.class, WingedTrim.class);
		final Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		// write the XML to stdout
		m.marshal(model, System.out);
	}
}
