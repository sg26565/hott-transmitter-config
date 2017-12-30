package de.treichels.hott.mdlviewer.swing;

import de.treichels.hott.decoder.HoTTDecoder;
import de.treichels.hott.messages.Messages;
import de.treichels.hott.model.BaseModel;
import de.treichels.hott.util.ModelLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SelectFromFile extends JPanel implements ModelLoader {
    private static final long serialVersionUID = 1L;

    private final JFileChooser chooser = new JFileChooser();
    private File file = null;

    SelectFromFile() {
        chooser.setControlButtonsAreShown(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter(Messages.getString("MdlFileDescription"), "mdl")); //$NON-NLS-1$ //$NON-NLS-2$
        chooser.setCurrentDirectory(new File(SimpleGUI.PREFS.get(SimpleGUI.LAST_LOAD_DIR, System.getProperty(Launcher.MDL_DIR))));

        setLayout(new BorderLayout());
        add(chooser, BorderLayout.CENTER);
    }

    @Override
    public BaseModel getModel() throws IOException {
        if (file == null) return null;

        return HoTTDecoder.INSTANCE.decodeFile(file);
    }

    @Override
    public byte[] getModelData() throws IOException {
        if (file == null) return null;

        final ByteArrayOutputStream os = new ByteArrayOutputStream((int) file.length());
        FileInputStream is = null;

        try {
            is = new FileInputStream(file);
            while (is.available() > 0)
                os.write(is.read());
        } finally {
            if (is != null) is.close();
        }

        return os.toByteArray();
    }

    @Override
    public void onCancel() {
        file = null;
    }

    @Override
    public void onOpen() {
        file = chooser.getSelectedFile();
        if (file != null) SimpleGUI.PREFS.put(SimpleGUI.LAST_LOAD_DIR, file.getParentFile().getAbsolutePath());
    }

    @Override
    public void onReload() {
        // do nothing
    }
}