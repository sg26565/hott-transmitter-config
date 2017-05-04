import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;

import de.treichels.hott.HoTTDecoder;
import gde.model.BaseModel;
import gde.report.html.HTMLReport;
import gde.report.pdf.PDFReport;
import gde.util.Util;

public class MC28Test {
    public static void main(final String[] args) throws IOException, DocumentException {
        final File[] dirs = new File[] { new File("C:/Users/olive/Downloads"), new File("C:/Users/olive/Graupner/mc-32/Models-2.015"),
                new File("C:/Users/olive/Graupner/mc-32/Models-1.017"), new File("C:/Users/olive/Graupner/mx-16/Models-1.807"),
                new File("C:/Users/olive/Graupner/mx-16/Models-2.015"),
                new File("C:/Users/olive/git/mdlviewer/hott-transmitter-config/HoTT-Report-HTML/src/test/resources/gde/report/html/models/mc20"),
                new File("C:/Users/olive/git/mdlviewer/hott-transmitter-config/HoTT-Report-HTML/src/test/resources/gde/report/html/models/mc28"),
                new File("C:/Users/olive/git/mdlviewer/hott-transmitter-config/HoTT-Report-HTML/src/test/resources/gde/report/html/models/mc32"),
                new File("C:/Users/olive/git/mdlviewer/hott-transmitter-config/HoTT-Report-HTML/src/test/resources/gde/report/html/models/mx12"),
                new File("C:/Users/olive/git/mdlviewer/hott-transmitter-config/HoTT-Report-HTML/src/test/resources/gde/report/html/models/mx16"),
                new File("C:/Users/olive/git/mdlviewer/hott-transmitter-config/HoTT-Report-HTML/src/test/resources/gde/report/html/models/mx20"), };

        for (final File dir : dirs)
            for (final File file : dir.listFiles(f -> f.getName().endsWith(".mdl"))) {
                final String baseName = FilenameUtils.getBaseName(file.getName());
                System.out.println(file.getAbsolutePath());

                final byte[] data = Files.readAllBytes(file.toPath());
                final String dump = Util.dumpData(data);
                final File dumpFile = new File(dir, baseName + ".txt");

                try (FileOutputStream os = new FileOutputStream(dumpFile)) {
                    os.write(dump.getBytes());
                }

                final BaseModel model = HoTTDecoder.decodeFile(file);
                final String html = HTMLReport.generateHTML(model);
                final File htmlFile = new File(dir, baseName + ".html");
                final File pdfFile = new File(dir, baseName + ".pdf");

                HTMLReport.save(htmlFile, html);
                PDFReport.save(pdfFile, html);
            }
    }
}
