import gde.report.Report;

import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(final String[] args) throws IOException {
		Report.generateXsd(new File("HoTTGUI.xsd"));
	}
}
