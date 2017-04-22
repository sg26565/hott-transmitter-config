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
        final File dir = new File("C:/Users/olive/Google Drive/Graupner/models/mc28");

        for (final File file : dir.listFiles(f -> f.getName().endsWith(".mdl"))) {
            final String baseName = FilenameUtils.getBaseName(file.getName());
            System.out.println(baseName);

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
