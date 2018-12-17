package de.treichels.hott.mdlviewer.swt;

import de.treichels.hott.decoder.HoTTDecoder;
import de.treichels.hott.mdlviewer.swt.dialogs.SelectFromMemoryDialog;
import de.treichels.hott.mdlviewer.swt.dialogs.SelectFromSdCardDialog;
import de.treichels.hott.mdlviewer.swt.dialogs.SelectFromTransmitterDialog;
import de.treichels.hott.messages.Messages;
import de.treichels.hott.model.BaseModel;
import de.treichels.hott.report.html.HTMLReport;
import de.treichels.hott.report.pdf.PDFReport;
import de.treichels.hott.report.xml.XMLReport;
import de.treichels.hott.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.FileWriter;
import java.util.prefs.Preferences;

class MdlTabItemComposite extends Composite {
    @NonNls
    private static final String MDL = ".mdl";
    @NonNls
    private static final String PDF = ".pdf";
    @NonNls
    private static final String HTML = ".html";
    @NonNls
    private static final String XML = ".xml";
    @NonNls
    private static final String LAST_SAVE_DIR = "lastSaveDir";
    @NonNls
    private static final String LAST_LOAD_DIR = "lastLoadDir";
    private static final Preferences PREFS = Preferences.userNodeForPackage(MdlTabItemComposite.class);

    private BaseModel model = null;
    private final Browser browser;
    private final Button saveMdlButton;
    private final MenuItem saveMdlMenuItem;
    private final MenuItem reloadMenuItem;

    public MdlTabItemComposite(final Composite parent) {
        super(parent, SWT.NONE);

        final GridLayout tabItemCompositeLayout = new GridLayout();
        tabItemCompositeLayout.numColumns = 3;
        setLayout(tabItemCompositeLayout);
        setBackground(new Color(Display.getDefault(), 250, 249, 211));

        final Font font = SWTResourceManager.getFont(SwtMdlBrowser.WIDGET_FONT_SIZE);

        HTMLReport.INSTANCE.setSuppressExceptions(false);

        // Load button
        final Button loadMdlButton = new Button(this, SWT.PUSH | SWT.CENTER);
        final GridData loadMdlButtonLData = new GridData();
        loadMdlButtonLData.widthHint = 180;
        loadMdlButtonLData.heightHint = 26;
        loadMdlButtonLData.verticalAlignment = GridData.BEGINNING;
        loadMdlButtonLData.grabExcessHorizontalSpace = true;
        loadMdlButtonLData.horizontalAlignment = GridData.CENTER;
        loadMdlButton.setLayoutData(loadMdlButtonLData);
        loadMdlButton.setFont(font);
        loadMdlButton.setText(Messages.getString("Load"));
        loadMdlButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                if (Util.INSTANCE.getDEBUG()) System.out.printf("loadMdlButton.widgetSelected, event=%s%n", evt);
                MdlTabItemComposite.this.loadFromFile();
            }
        });

        // Version label
        final Label mdlVersionLabel = new Label(this, SWT.CENTER);
        final GridData mdlVersionLabelLData = new GridData();
        mdlVersionLabelLData.horizontalAlignment = GridData.CENTER;
        // mdlVersionLabelLData.widthHint = 180; // no line wrap
        mdlVersionLabelLData.heightHint = 26;
        mdlVersionLabelLData.verticalAlignment = GridData.BEGINNING;
        mdlVersionLabelLData.grabExcessHorizontalSpace = true;
        mdlVersionLabel.setLayoutData(mdlVersionLabelLData);
        mdlVersionLabel.setBackground(new Color(Display.getDefault(), 250, 249, 211));
        mdlVersionLabel.setFont(font);
        mdlVersionLabel.setText("Version: " + Launcher.Companion.getInstance().getVersion());

        saveMdlButton = new Button(this, SWT.PUSH | SWT.CENTER);
        final GridData saveMdlButtonLData = new GridData();
        saveMdlButtonLData.horizontalAlignment = GridData.CENTER;
        saveMdlButtonLData.widthHint = 180;
        saveMdlButtonLData.heightHint = 26;
        saveMdlButtonLData.verticalAlignment = GridData.BEGINNING;
        saveMdlButtonLData.grabExcessHorizontalSpace = true;
        saveMdlButton.setLayoutData(saveMdlButtonLData);
        saveMdlButton.setFont(font);
        saveMdlButton.setText(Messages.getString("Save"));
        saveMdlButton.setEnabled(false);
        saveMdlButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                if (Util.INSTANCE.getDEBUG()) System.out.printf("saveMdlButton.widgetSelected, event=%s%n", evt);
                MdlTabItemComposite.this.save();
            }
        });

        // Browser
        browser = new Browser(this, SWT.BORDER);
        final GridData viewTextLData = new GridData();
        viewTextLData.grabExcessHorizontalSpace = true;
        viewTextLData.verticalAlignment = GridData.FILL;
        viewTextLData.grabExcessVerticalSpace = true;
        viewTextLData.horizontalSpan = 4;
        viewTextLData.horizontalAlignment = GridData.FILL;
        browser.setLayoutData(viewTextLData);
        final Menu contextMenu = new Menu(browser);
        browser.setMenu(contextMenu);
        browser.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent evt) {
                switch (evt.keyCode) {
                case SWT.F5:
                    MdlTabItemComposite.this.updateView();
                    break;
                }
            }

            @Override
            public void keyReleased(final KeyEvent evt) {}
        });

        // Load from file menu item
        final MenuItem loadMdlFromFileMenuItemMenuItem = new MenuItem(contextMenu, SWT.NONE);
        loadMdlFromFileMenuItemMenuItem.setText(Messages.getString("LoadFromFile"));
        loadMdlFromFileMenuItemMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                if (Util.INSTANCE.getDEBUG()) System.out.printf("loadMdlMenuItem.widgetSelected, event=%s%n", evt);
                MdlTabItemComposite.this.loadFromFile();
            }
        });

        // Load from memory menu item
        final MenuItem loadMdlFromMemoryMenuItemMenuItem = new MenuItem(contextMenu, SWT.NONE);
        loadMdlFromMemoryMenuItemMenuItem.setText(Messages.getString("LoadFromMemory"));
        loadMdlFromMemoryMenuItemMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                if (Util.INSTANCE.getDEBUG()) System.out.printf("loadMdlMenuItem.widgetSelected, event=%s%n", evt);
                MdlTabItemComposite.this.loadFromMemory();
            }
        });

        // Load from sd card menu item
        final MenuItem loadMdlFromSdCardMenuItemMenuItem = new MenuItem(contextMenu, SWT.NONE);
        loadMdlFromSdCardMenuItemMenuItem.setText(Messages.getString("LoadFromSdCard"));
        loadMdlFromSdCardMenuItemMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                if (Util.INSTANCE.getDEBUG()) System.out.printf("loadMdlMenuItem.widgetSelected, event=%s%n", evt);
                MdlTabItemComposite.this.loadFromSdCard();
            }
        });

        saveMdlMenuItem = new MenuItem(contextMenu, SWT.NONE);
        saveMdlMenuItem.setText(Messages.getString("Save"));
        saveMdlMenuItem.setEnabled(false);
        saveMdlMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                if (Util.INSTANCE.getDEBUG()) System.out.printf("saveMdlMenuItem.widgetSelected, event=%s%n", evt);
                MdlTabItemComposite.this.save();
            }
        });

        new MenuItem(contextMenu, SWT.SEPARATOR);

        reloadMenuItem = new MenuItem(contextMenu, SWT.NONE);
        reloadMenuItem.setText(Messages.getString("Reload"));
        reloadMenuItem.setEnabled(false);
        reloadMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
                if (Util.INSTANCE.getDEBUG()) System.out.printf("reloadMenuItem.widgetSelected, event=%s%n", evt);
                MdlTabItemComposite.this.updateView();
            }
        });
    }

    private void loadFromFile() {
        final FileDialog fd = new FileDialog(getParent().getShell(), SWT.APPLICATION_MODAL | SWT.OPEN | SWT.SINGLE);
        fd.setText(Messages.getString("Load"));
        fd.setFilterExtensions(new String[] { "*.mdl" });
        fd.setFilterNames(new String[] { Messages.getString("MdlFileDescription") });
        fd.setFilterPath(PREFS.get(LAST_LOAD_DIR, System.getProperty(Launcher.Companion.getInstance().getMdlDir().getAbsolutePath())));

        final String fileName = fd.open();
        if (fileName != null && fileName.endsWith(MDL)) {
            final File file = new File(fileName);
            if (file.exists() && file.canRead()) {
                PREFS.put(LAST_LOAD_DIR, file.getParentFile().getAbsolutePath());

                BusyIndicator.showWhile(getDisplay(), () -> {
                    try {
                        model = HoTTDecoder.INSTANCE.decodeFile(file);
                    } catch (final Throwable t) {
                        showError(t);
                    }
                });

                saveMdlButton.setEnabled(true);
                saveMdlMenuItem.setEnabled(true);
                reloadMenuItem.setEnabled(true);

                updateView();
            }
        }
    }

    private void loadFromMemory() {
        load(new SelectFromMemoryDialog(getParent().getShell()));
    }

    private void loadFromSdCard() {
        load(new SelectFromSdCardDialog(getParent().getShell()));
    }

    private void load(SelectFromTransmitterDialog dialog) {
        if (dialog.open() != null) {
            model = dialog.getModel();

            saveMdlButton.setEnabled(true);
            saveMdlMenuItem.setEnabled(true);
            reloadMenuItem.setEnabled(true);

            updateView();
        }
    }

    private void save() {
        if (model != null) {
            final FileDialog fd = new FileDialog(getParent().getShell(), SWT.APPLICATION_MODAL | SWT.SAVE | SWT.SINGLE);
            fd.setText(Messages.getString("Save"));
            fd.setFilterExtensions(new String[] { "*.pdf","*.html","*.xml" });
            fd.setFilterNames(new String[] { "Portable Document Format (*.pdf)", "Hypertext Markup Language (*.html)", "Extensible Markup Language (*.xml)" });
            fd.setFilterPath(PREFS.get(LAST_SAVE_DIR, PREFS.get(LAST_LOAD_DIR, System.getProperty(Launcher.Companion.getInstance().getMdlDir().getAbsolutePath()))));
            fd.setFileName(model.getModelName() + PDF);
            fd.setOverwrite(true);

            final String fileName = fd.open();
            if (fileName != null && fileName.length() > 4) {
                final File file = new File(fileName);
                PREFS.put(LAST_SAVE_DIR, file.getParentFile().getAbsolutePath());

                BusyIndicator.showWhile(getDisplay(), () -> {
                    try {
                        if (fileName.endsWith(XML)) {
                            final String data1 = XMLReport.INSTANCE.generateXML(model);
                            final FileWriter fw1 = new FileWriter(file);
                            fw1.write(data1);
                            fw1.close();
                        } else if (fileName.endsWith(HTML)) {
                            final String data2 = HTMLReport.INSTANCE.generateHTML(model);
                            final FileWriter fw2 = new FileWriter(file);
                            fw2.write(data2);
                            fw2.close();
                        } else if (fileName.endsWith(PDF)) {
                            final String data3 = HTMLReport.INSTANCE.generateHTML(model);
                            PDFReport.INSTANCE.save(file, data3);
                        }
                    } catch (final Throwable t) {
                        showError(t);
                    }
                });
            }
        }
    }

    private void showError(final Throwable t) {
        final MessageBox mb = new MessageBox(getParent().getShell(), SWT.NONE);
        mb.setText(t.getClass().getSimpleName());
        mb.setMessage(t.getMessage() == null ? t.getClass().getSimpleName() : t.getMessage());
        mb.open();
        if (Util.INSTANCE.getDEBUG()) t.printStackTrace();
    }

    private void updateView() {
        if (model != null) BusyIndicator.showWhile(getDisplay(), () -> {
            String data;
            try {
                data = HTMLReport.INSTANCE.generateHTML(model);
            } catch (final Exception e) {
                data = e.getMessage();
            }

            browser.setText(data);
            browser.setFocus();
        });
    }
}
