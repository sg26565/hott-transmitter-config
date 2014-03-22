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

import freemarker.ext.beans.JavaBeansIntrospector;
import gde.messages.Messages;
import gde.model.BaseModel;
import gde.report.html.HTMLReport;
import gde.report.pdf.PDFReport;
import gde.report.xml.XMLReport;
import gnu.io.RXTXCommDriver;

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
    private final Source      source;

    public LoadAction(final String name, final Source source) {
      super(name);
      this.source = source;
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      try {
        load(source);
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

  public enum Source {
    File, Memory, SdCard
  }

  @SuppressWarnings("unused")
  private Class<JavaBeansIntrospector> class1;

  @SuppressWarnings("unused")
  private static RXTXCommDriver        driver;

  static final String                  LAST_LOAD_DIR        = "lastLoadDir";                                                                                  //$NON-NLS-1$
  private static final String          LAST_SAVE_DIR        = "lastSaveDir"; //$NON-NLS-1$
  private static final Logger          LOG                  = Logger.getLogger(SimpleGUI.class.getName());
  static final Preferences             PREFS                = Preferences.userNodeForPackage(SimpleGUI.class);
  private static final long            serialVersionUID     = 8824399313635999416L;

  private final JFrame                 frame                = new JFrame(Messages.getString("SimpleGUI.Title", System.getProperty(Launcher.PROGRAM_VERSION))); //$NON-NLS-1$
  private final Action                 closeAction          = new CloseAction(Messages.getString("Close"));                                                   //$NON-NLS-1$
  private final Action                 loadAction       = new LoadAction(Messages.getString("Load"), Source.File);                                        //$NON-NLS-1$
  private BaseModel                    model                = null;
  private final Action                 refreshAction        = new RefreshAction(Messages.getString("Refresh"));                                               //$NON-NLS-1$
  private final Action                 saveHtmlAction       = new SaveAction(Messages.getString("SaveHtml"), FileType.HTML);                                  //$NON-NLS-1$
  private final Action                 savePdfAction        = new SaveAction(Messages.getString("SavePdf"), FileType.PDF);                                    //$NON-NLS-1$
  private final Action                 saveXmlAction        = new SaveAction(Messages.getString("SaveXml"), FileType.XML);                                    //$NON-NLS-1$
  private final XHTMLPanel             xhtmlPane            = new XHTMLPanel();
  private final JPopupMenu             popupMenu            = new JPopupMenu();

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

  public void load(final Source source) throws IOException, URISyntaxException {
    switch (source) {
    case File:
      loadFromFile();
      break;

    case Memory:
      loadFromMemory();
      break;

    case SdCard:
      loafFromSdCard();
      break;
    }
  }

  public void loadFromFile() throws IOException, URISyntaxException {
    final OpenDialog loader = new OpenDialog(frame);
    loader.setVisible(true);
    // final ModelLoader dialog = SelectFromFile.showDialog(frame);

    final BaseModel m = loader.getModel();
    if (m != null) {
      model = m;

      saveHtmlAction.setEnabled(true);
      savePdfAction.setEnabled(true);
      saveXmlAction.setEnabled(true);

      refresh();
    }
  }

  private void loadFromMemory() throws IOException {
    // final ModelLoader loader = new SelectFromMemory();
    // loader.showDialog(frame);
    //
    // final BaseModel m = loader.getModel();
    // if (m != null) {
    // model = m;
    //
    // saveHtmlAction.setEnabled(true);
    // savePdfAction.setEnabled(true);
    // saveXmlAction.setEnabled(true);
    //
    // refresh();
    // }
  }

  private void loafFromSdCard() throws IOException {
    // final ModelLoader dialog = new SelectFromSdCard();
    // dialog.showDialog(frame);
    //
    // final BaseModel m = dialog.getModel();
    // if (m != null) {
    // model = m;
    //
    // saveHtmlAction.setEnabled(true);
    // savePdfAction.setEnabled(true);
    // saveXmlAction.setEnabled(true);
    //
    // refresh();
    // }
  }

  public void refresh() throws IOException {
    if (model != null) {
      xhtmlPane.setDocumentFromString(HTMLReport.generateHTML(model), "", new XhtmlNamespaceHandler()); //$NON-NLS-1$
    }
  }

  public void save(final FileType fileType) throws IOException, DocumentException, JAXBException {
    if (model == null) {
      return;
    }

    final String extension = fileType.toString().toLowerCase();
    final String description = fileType.toString() + Messages.getString("SimpleGUI._Files"); //$NON-NLS-1$
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

      if (!file.getName().endsWith("." + fileType)) { //$NON-NLS-1$
        file = new File(file.getParentFile(), file.getName() + "." + fileType); //$NON-NLS-1$
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
    LOG.log(Level.SEVERE, Messages.getString("Error"), t); //$NON-NLS-1$
    JOptionPane
    .showMessageDialog(getTopLevelAncestor(), t.getClass().getName() + ": " + t.getMessage(), Messages.getString("Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public void showInFrame() {
    try {
      final JMenu fileMenu = new JMenu(Messages.getString("File")); //$NON-NLS-1$
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
