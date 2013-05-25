import java.io.FileOutputStream;
import java.io.StringReader;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

public class Test {
	private static final String	SVG	= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"100\" height=\"100\"><rect x=\"5\" y=\"5\" width=\"90\" height=\"90\" fill=\"green\" stroke=\"black\" stroke-width=\"3\"/></svg>";

	public static void main(final String[] args) throws Exception {
		StringReader reader = new StringReader(SVG);
		final FileOutputStream os = new FileOutputStream("test.jpg");
		final TranscoderInput input = new TranscoderInput(reader);
		final TranscoderOutput output = new TranscoderOutput(os);
		final Transcoder t = new JPEGTranscoder();

		t.transcode(input, output);

		os.flush();
		os.close();
	}
}
