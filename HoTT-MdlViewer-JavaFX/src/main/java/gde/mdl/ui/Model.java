package gde.mdl.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import com.itextpdf.text.DocumentException;

import de.treichels.hott.HoTTDecoder;
import gde.model.BaseModel;
import gde.model.HoTTException;
import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;
import gde.report.ReportException;
import gde.report.html.HTMLReport;
import gde.report.pdf.PDFReport;
import gde.report.xml.XMLReport;
import gde.util.Util;

public class Model {
    public static Model load(final File file) throws IOException {
        // check if file exits
        if (file == null || !file.exists()) throw new HoTTException("FileNotFound", file); //$NON-NLS-1$

        // check that file has a .mdl extension
        final String fileName = file.getName();
        if (!fileName.endsWith(".mdl")) throw new HoTTException("InvalidFileName", fileName); //$NON-NLS-1$

        // check model type
        final ModelType modelType = ModelType.forChar(fileName.charAt(0));
        final String modelName = fileName.substring(1, fileName.length() - 4);
        final ModelInfo info = new ModelInfo(0, modelName, modelType, null, null);
        final byte[] data = new byte[(int) file.length()];
        try (InputStream is = new FileInputStream(file)) {
            is.read(data);
        }

        return new Model(info, data);
    }

    private BaseModel model = null;

    private final byte[] data;

    private final ModelInfo info;

    public Model(final ModelInfo info, final byte[] data) {
        this.info = info;
        this.data = data;
    }

    public BaseModel decode() throws IOException {
        if (model == null) model = HoTTDecoder.decodeStream(info.getModelType(), info.getModelName(), new ByteArrayInputStream(data));

        return model;
    }

    public byte[] getData() {
        return data;
    }

    public String getFileName() {
        return (info.getModelType() == ModelType.Helicopter ? "h" : "a") + info.getModelName();
    }

    public String getFileName(final String extension) {
        return getFileName() + "." + extension;
    }

    public String getHtml() throws IOException {
        return HTMLReport.generateHTML(decode());
    }

    public ModelInfo getInfo() {
        return info;
    }

    public String getXml() throws IOException, JAXBException {
        return XMLReport.generateXML(decode());
    }

    public void saveHtml(final File file) throws IOException {
        HTMLReport.save(file, getHtml());
    }

    public void saveMdl(final File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }

    public void savePdf(final File file) throws IOException, ReportException, DocumentException {
        PDFReport.save(file, getHtml());
    }

    public void saveTxt(final File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(Util.dumpData(data).getBytes());
        }
    }

    public void saveXml(final File file) throws IOException, JAXBException {
        HTMLReport.save(file, getXml());
    }
}
