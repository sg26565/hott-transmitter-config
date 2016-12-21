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

import gde.mdl.messages.Messages;
import gde.model.BaseModel;
import gde.report.html.HTMLReport;
import gde.report.pdf.PDFReport;
import gde.report.xml.XMLReport;
import gnu.io.RXTXCommDriver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;

public class SimpleGUI {
  private final class ExitAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public ExitAction(final String name) {
      super(name);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      System.exit(0);
    }
  }

  private final class LoadAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public LoadAction(final String name) {
      super(name);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      load();
    }
  }

  private final class LoadWorker extends SwingWorker<Void, Void> {
    @Override
    protected Void doInBackground() throws Exception {
      try {
        final OpenDialog loader = new OpenDialog(frame);
        loader.setVisible(true);

        layerUI.start();
        modelData = loader.getModelData();
        final BaseModel m = loader.getModel();
        if (m != null) {
          model = m;
        }
      } catch (final Exception e) {
        LOG.log(Level.SEVERE, "error during load", e);
        JOptionPane.showMessageDialog(frame, e, Messages.getString("Error"), JOptionPane.ERROR_MESSAGE);
      }
      return null;
    }

    @Override
    protected void done() {
      refresh();
    }
  }

  private final class RefreshAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public RefreshAction(final String name) {
      super(name);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      refresh();
    }
  }

  private final class RefreshWorker extends SwingWorker<Void, String> {
    @Override
    protected Void doInBackground() throws Exception {
      try {
        layerUI.start();

        if (model != null) {
          final String html = HTMLReport.generateHTML(model);
          publish(html);
        }
      } catch (final Exception e) {
        LOG.log(Level.SEVERE, "error during refresh", e);
        JOptionPane.showMessageDialog(frame, e, Messages.getString("Error"), JOptionPane.ERROR_MESSAGE);
      }

      return null;
    }

    @Override
    protected void done() {
      if (model != null) {
        saveAction.setEnabled(true);
      }

      layerUI.stop();
    }

    @Override
    protected void process(final List<String> chunks) {
      for (final String html : chunks) {
        xhtmlPane.setDocumentFromString(html, "", new XhtmlNamespaceHandler()); //$NON-NLS-1$
      }
    }
  }

  private final class RightClickListener extends MouseAdapter {
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
  }

  private final class SaveAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public SaveAction(final String name) {
      super(name);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
      save();
    }
  }

  private class SaveWorker extends SwingWorker<Void, Void> {
    @Override
    protected Void doInBackground() throws Exception {
      try {
        if (model != null) {
          final JFileChooser fc = new JFileChooser();
          fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fc.setMultiSelectionEnabled(false);
          fc.setAcceptAllFileFilterUsed(false);
          fc.setCurrentDirectory(new File(PREFS.get(LAST_SAVE_DIR, PREFS.get(LAST_LOAD_DIR, System.getProperty(Launcher.MDL_DIR)))));
          fc.addChoosableFileFilter(new FileNameExtensionFilter(Messages.getString("SimpleGUI.PDF"), "pdf"));
          fc.addChoosableFileFilter(new FileNameExtensionFilter(Messages.getString("SimpleGUI.XML"), "xml"));
          fc.addChoosableFileFilter(new FileNameExtensionFilter(Messages.getString("SimpleGUI.HTML"), "html"));
          fc.addChoosableFileFilter(new FileNameExtensionFilter(Messages.getString("MdlFileDescription"), "mdl"));

          fc.setSelectedFile(new File(getFileName(model)));

          final int result = fc.showSaveDialog(frame);

          if (result == JFileChooser.APPROVE_OPTION) {
            layerUI.start();
            File file = fc.getSelectedFile();
            final String extension = ((FileNameExtensionFilter) fc.getFileFilter()).getExtensions()[0];

            if (!file.getName().endsWith(extension)) {
              file = new File(file.getParent(), file.getName() + "." + extension);
            }

            PREFS.put(LAST_SAVE_DIR, file.getParentFile().getAbsolutePath());

            if ("xml".equals(extension)) {
              HTMLReport.save(file, XMLReport.generateXML(model));
            } else if ("html".equals(extension)) {
              HTMLReport.save(file, HTMLReport.generateHTML(model));
            } else if ("pdf".equals(extension)) {
              PDFReport.save(file, HTMLReport.generateHTML(model));
            } else if ("mdl".equals(extension)) {
              FileOutputStream fos = null;
              try {
                fos = new FileOutputStream(file);
                fos.write(modelData);
              } finally {
                if (fos != null) {
                  fos.close();
                }
              }
            }
          }
        }
      } catch (final Exception e) {
        LOG.log(Level.SEVERE, "error during save", e);
        JOptionPane.showMessageDialog(frame, e, Messages.getString("Error"), JOptionPane.ERROR_MESSAGE);
      }

      return null;
    }

    @Override
    protected void done() {
      layerUI.stop();
    }
  }

  @SuppressWarnings("unused")

  @SuppressWarnings("unused")
  private static RXTXCommDriver          driver;

  static final String                    LAST_LOAD_DIR = "lastLoadDir";                                                                                  //$NON-NLS-1$

  private static final String            LAST_SAVE_DIR = "lastSaveDir";                                                                                  //$NON-NLS-1$

  private static final Logger            LOG           = Logger.getLogger(SimpleGUI.class.getName());

  static final Preferences               PREFS         = Preferences.userNodeForPackage(SimpleGUI.class);
  private final JFrame                   frame         = new JFrame(Messages.getString("SimpleGUI.Title", System.getProperty(Launcher.PROGRAM_VERSION))); //$NON-NLS-1$
  private final Action                   exitAction    = new ExitAction(Messages.getString("Exit"));                                                     //$NON-NLS-1$
  private final Action                   loadAction    = new LoadAction(Messages.getString("Load"));                                                     //$NON-NLS-1$
  private BaseModel                      model         = null;
  private byte[]                         modelData     = null;
  private final Action                   refreshAction = new RefreshAction(Messages.getString("Refresh"));                                               //$NON-NLS-1$
  private final Action                   saveAction    = new SaveAction(Messages.getString("Save"));                                                     //$NON-NLS-1$
  private final XHTMLPanel               xhtmlPane     = new XHTMLPanel();
  private final JMenuBar                 menubar       = new JMenuBar();
  private final JMenu                    fileMenu      = new JMenu(Messages.getString("File"));                                                          //$NON-NLS-1$
  private final JPopupMenu               popupMenu     = new JPopupMenu();
  private final JScrollPane              scrollPane    = new JScrollPane();
  private final JPanel                   buttonPanel   = new JPanel();
  private final JPanel                   content       = new JPanel();
	private final WaitLayerUI<JScrollPane> layerUI = new WaitLayerUI<>();
	private final JLayer<JScrollPane> layer = new JLayer<>(scrollPane, layerUI);

  public SimpleGUI() {
    final SharedContext ctx = xhtmlPane.getSharedContext();
    ctx.getTextRenderer().setSmoothingThreshold(10);
    ctx.setReplacedElementFactory(new InlineImageReplacedElementFactory());
    HTMLReport.setSuppressExceptions(false);

    saveAction.setEnabled(false);

    fileMenu.add(new JMenuItem(loadAction));
    fileMenu.add(new JMenuItem(saveAction));
    fileMenu.addSeparator();
    fileMenu.add(new JMenuItem(refreshAction));
    fileMenu.addSeparator();
    fileMenu.add(new JMenuItem(exitAction));

    menubar.add(fileMenu);

    buttonPanel.add(new JButton(exitAction));
    // buttonPanel.add(new JButton(refreshAction));
    buttonPanel.add(new JButton(loadAction));
    buttonPanel.add(new JButton(saveAction));

    popupMenu.add(new JMenuItem(loadAction));
    popupMenu.add(new JMenuItem(saveAction));
    popupMenu.addSeparator();
    popupMenu.add(new JMenuItem(refreshAction));
    popupMenu.addSeparator();
    popupMenu.add(new JMenuItem(exitAction));

    xhtmlPane.addMouseListener(new RightClickListener());
    scrollPane.setViewportView(xhtmlPane);

    content.setLayout(new BorderLayout());
    content.add(buttonPanel, BorderLayout.SOUTH);
    content.add(layer, BorderLayout.CENTER);

    frame.setJMenuBar(menubar);
    frame.setLayout(new BorderLayout());
    frame.getContentPane().add(content);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.setSize(800, 600);
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

  private void load() {
    new LoadWorker().execute();
  }

  private void refresh() {
    new RefreshWorker().execute();
  }

  private void save() {
    new SaveWorker().execute();
  }
}
