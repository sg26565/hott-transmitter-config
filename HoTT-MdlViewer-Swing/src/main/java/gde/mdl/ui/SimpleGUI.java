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

package gde.mdl.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;

import com.itextpdf.text.DocumentException;

import de.treichels.hott.HoTTDecoder;
import freemarker.ext.beans.JavaBeansIntrospector;
import gde.model.BaseModel;
import gde.report.html.HTMLReport;
import gde.report.pdf.PDFReport;
import gde.report.xml.XMLReport;

public class SimpleGUI extends FSScrollPane {
  private final class CloseAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public CloseAction(final String name) {
      super(name);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      System.exit(0);
    }
  }

  public enum FileType {
    HTML, PDF, XML
  }

  private final class LoadAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public LoadAction(final String name) {
      super(name);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      try {
        load();
      } catch (final Throwable t) {
        showError(t);
      }
    }
  }

  private final class RefreshAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public RefreshAction(final String name) {
      super(name);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      try {
        refresh();
      } catch (final Exception e) {
        showError(e);
      }
    }
  }

  private final class SaveAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private final FileType    fileType;

    public SaveAction(final String name, final FileType fileType) {
      super(name);
      this.fileType = fileType;
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      try {
        save(fileType);
      } catch (final Throwable t) {
        showError(t);
      }
    }
  }

  @SuppressWarnings("unused")
  private Class<JavaBeansIntrospector> class1;

  private static final String          LAST_LOAD_DIR    = "lastLoadDir";
  private static final String          LAST_SAVE_DIR    = "lastSaveDir";
  private static final Logger          LOG              = Logger.getLogger(SimpleGUI.class.getName());
  private static final Preferences     PREFS            = Preferences.userNodeForPackage(SimpleGUI.class);
  private static final long            serialVersionUID = 8824399313635999416L;

  private final Action                 closeAction      = new CloseAction("Close");
  private final Action                 loadAction       = new LoadAction("Load MDL");
  private BaseModel                    model            = null;
  private final Action                 refreshAction    = new RefreshAction("Refresh");
  private final Action                 saveHtmlAction   = new SaveAction("Save HTML", FileType.HTML);
  private final Action                 savePdfAction    = new SaveAction("Save PDF", FileType.PDF);
  private final Action                 saveXmlAction    = new SaveAction("Save XML", FileType.XML);
  private final XHTMLPanel             xhtmlPane        = new XHTMLPanel();

  private final JPopupMenu             popupMenu        = new JPopupMenu();

  public SimpleGUI() {
    final SharedContext ctx = xhtmlPane.getSharedContext();
    ctx.getTextRenderer().setSmoothingThreshold(10);
    ctx.setReplacedElementFactory(new InlineImageReplacedElementFactory());
    setViewportView(xhtmlPane);
    HTMLReport.setSuppressExceptions(true);

    saveHtmlAction.setEnabled(false);
    savePdfAction.setEnabled(false);
    saveXmlAction.setEnabled(false);
  }

  private String getFileName(final BaseModel model) {
    // setup filename
    final StringBuilder fileName = new StringBuilder();
    switch (model.getModelType()) {
    case Helicopter:
      fileName.append('h');
      break;

    case Winged:
      fileName.append('a');
      break;

    default:
      break;
    }

    fileName.append(model.getModelName());

    return fileName.toString();
  }

  public void load() throws IOException, URISyntaxException {
    final JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setMultiSelectionEnabled(false);
    fc.setAcceptAllFileFilterUsed(false);
    fc.setFileFilter(new FileNameExtensionFilter("HoTT Transmitter Model Files", "mdl"));
    fc.setCurrentDirectory(new File(PREFS.get(LAST_LOAD_DIR, System.getProperty(Launcher.MDL_DIR))));

    final int result = fc.showOpenDialog(getTopLevelAncestor());

    if (result == JFileChooser.APPROVE_OPTION) {
      final File file = fc.getSelectedFile();
      PREFS.put(LAST_LOAD_DIR, file.getParentFile().getAbsolutePath());

      model = HoTTDecoder.decodeFile(file);
      saveHtmlAction.setEnabled(true);
      savePdfAction.setEnabled(true);
      saveXmlAction.setEnabled(true);

      refresh();
    }
  }

  public void refresh() throws IOException {
    if (model != null) {
      xhtmlPane.setDocumentFromString(HTMLReport.generateHTML(model), "", new XhtmlNamespaceHandler());
    }
  }

  public void save(final FileType fileType) throws IOException, DocumentException, JAXBException {
    if (model == null) {
      return;
    }

    final String extension = fileType.toString().toLowerCase();
    final String description = fileType.toString() + " Files";
    final JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setMultiSelectionEnabled(false);
    fc.setAcceptAllFileFilterUsed(false);
    fc.setCurrentDirectory(new File(PREFS.get(LAST_SAVE_DIR, PREFS.get(LAST_LOAD_DIR, System.getProperty(Launcher.MDL_DIR)))));
    fc.setFileFilter(new FileNameExtensionFilter(description, extension));
    fc.setSelectedFile(new File(getFileName(model)));

    final int result = fc.showSaveDialog(getTopLevelAncestor());

    if (result == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      PREFS.put(LAST_SAVE_DIR, file.getParentFile().getAbsolutePath());

      if (!file.getName().endsWith("." + fileType)) {
        file = new File(file.getParentFile(), file.getName() + "." + fileType);
      }

      switch (fileType) {
      case XML:
        HTMLReport.save(file, XMLReport.generateXML(model));
        break;

      case HTML:
        HTMLReport.save(file, HTMLReport.generateHTML(model));
        break;

      case PDF:
        PDFReport.save(file, HTMLReport.generateHTML(model));
        break;
      }
    }
  }

  private void showError(final Throwable t) {
    LOG.log(Level.SEVERE, "Error", t);
    JOptionPane.showMessageDialog(getTopLevelAncestor(), t.getClass().getName() + ": " + t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
  }

  public void showInFrame() {
    try {
      final JMenu fileMenu = new JMenu("File");
      fileMenu.add(new JMenuItem(loadAction));
      fileMenu.add(new JMenuItem(saveHtmlAction));
      fileMenu.add(new JMenuItem(savePdfAction));
      fileMenu.add(new JMenuItem(saveXmlAction));
      fileMenu.addSeparator();
      fileMenu.add(new JMenuItem(refreshAction));
      fileMenu.addSeparator();
      fileMenu.add(new JMenuItem(closeAction));

      final JMenuBar menubar = new JMenuBar();
      menubar.add(fileMenu);

      final JPanel buttonPanel = new JPanel();
      buttonPanel.add(new JButton(closeAction));
      buttonPanel.add(new JButton(refreshAction));
      buttonPanel.add(new JButton(loadAction));
      buttonPanel.add(new JButton(saveHtmlAction));
      buttonPanel.add(new JButton(savePdfAction));
      buttonPanel.add(new JButton(saveXmlAction));

      popupMenu.add(new JMenuItem(loadAction));
      popupMenu.add(new JMenuItem(saveHtmlAction));
      popupMenu.add(new JMenuItem(savePdfAction));
      popupMenu.add(new JMenuItem(saveXmlAction));
      popupMenu.addSeparator();
      popupMenu.add(new JMenuItem(refreshAction));
      popupMenu.addSeparator();
      popupMenu.add(new JMenuItem(closeAction));

      xhtmlPane.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(final MouseEvent e) {
          if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
          }
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
          if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
          }
        }
      });

      final JFrame frame = new JFrame("Hott Transmitter Config - " + System.getProperty(Launcher.PROGRAM_VERSION));
      frame.setJMenuBar(menubar);
      frame.setLayout(new BorderLayout());
      frame.add(buttonPanel, BorderLayout.SOUTH);
      frame.add(this, BorderLayout.CENTER);
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      frame.setSize(800, 600);
    } catch (final Throwable t) {
      showError(t);
    }
  }
}
