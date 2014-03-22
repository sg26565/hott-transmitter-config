package gde.mdl.ui;

import gde.messages.Messages;
import gde.model.BaseModel;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.treichels.hott.HoTTDecoder;

public class SelectFromFile extends JPanel implements ModelLoader {
  private static final long serialVersionUID = 1L;

  private final JFileChooser chooser          = new JFileChooser();
  private File               file             = null;

  public SelectFromFile() {
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
    if (file == null) {
      return null;
    }

    return HoTTDecoder.decodeFile(file);
  }

  @Override
  public void onCancel() {
    file = null;
  }

  @Override
  public void onOpen() {
    file = chooser.getSelectedFile();
    if (file != null) {
      SimpleGUI.PREFS.put(SimpleGUI.LAST_LOAD_DIR, file.getParentFile().getAbsolutePath());
    }
  }

  @Override
  public void onReload() {
    // do nothing
  }
}
