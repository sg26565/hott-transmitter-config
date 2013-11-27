package gde.mdl.ui;

import gde.model.BaseModel;
import gde.report.Report;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

public class MdlTabItemComposite extends Composite {
	private static final Preferences	PREFS	= Preferences.userNodeForPackage(MdlTabItemComposite.class);

	private Browser										browser;
	private final Font											font;
	private Button										loadMdlButton;
	private MenuItem									loadMdlMenuItem;
	private Label											mdlVersionLabel;
	private BaseModel									model	= null;
	private MenuItem									reloadMenuItem;
	private Button										saveMdlButton;
	private MenuItem									saveMdlMenuItem;

	public MdlTabItemComposite(final Composite parent) {
		super(parent, SWT.NONE);
		final GridLayout tabItemCompositeLayout = new GridLayout();
		tabItemCompositeLayout.numColumns = 3;
		setLayout(tabItemCompositeLayout);
		setBackground(new Color(Display.getDefault(), 250, 249, 211));
		final FontData fontData = parent.getFont().getFontData()[0];
		fontData.setHeight(fontData.getHeight() - 1);
		font = SWTResourceManager.getFont(SwtMdlBrowser.WIDGET_FONT_NAME, SwtMdlBrowser.WIDGET_FONT_SIZE, SWT.NORMAL);
		Report.setSuppressExceptions(false);
		{
			loadMdlButton = new Button(this, SWT.PUSH | SWT.CENTER);
			final GridData loadMdlButtonLData = new GridData();
			loadMdlButtonLData.widthHint = 180;
			loadMdlButtonLData.heightHint = 26;
			loadMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			loadMdlButtonLData.grabExcessHorizontalSpace = true;
			loadMdlButtonLData.horizontalAlignment = GridData.CENTER;
			loadMdlButton.setLayoutData(loadMdlButtonLData);
			loadMdlButton.setFont(font);
			loadMdlButton.setText("Load MDL"); //$NON-NLS-1$
			loadMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "loadMdlButton.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.load();
				}
			});
		}
		{
			mdlVersionLabel = new Label(this, SWT.CENTER);
			final GridData mdlVersionLabelLData = new GridData();
			mdlVersionLabelLData.horizontalAlignment = GridData.CENTER;
			//mdlVersionLabelLData.widthHint = 180; // no line wrap
			mdlVersionLabelLData.heightHint = 26;
			mdlVersionLabelLData.verticalAlignment = GridData.BEGINNING;
			mdlVersionLabelLData.grabExcessHorizontalSpace = true;
			mdlVersionLabel.setLayoutData(mdlVersionLabelLData);
			mdlVersionLabel.setBackground(new Color(Display.getDefault(), 250, 249, 211));
			mdlVersionLabel.setFont(font);
			String version = "?"; //$NON-NLS-1$
			try {
				@SuppressWarnings("rawtypes")
				final
				Class clazz = MdlTabItem.class;
				final String className = clazz.getSimpleName() + ".class"; //$NON-NLS-1$
				final String classPath = clazz.getResource(className).toString();
				if (classPath.startsWith("jar")) { //$NON-NLS-1$
					final String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF"; //$NON-NLS-1$ //$NON-NLS-2$
					final Manifest manifest = new Manifest(new URL(manifestPath).openStream());
					version = manifest.getMainAttributes().getValue("Implementation-Version") + "." + manifest.getMainAttributes().getValue("Implementation-Build");
				}
				System.setProperty(Launcher.PROGRAM_VERSION, version);
				mdlVersionLabel.setText("Implementation-Version: " + version); //$NON-NLS-1$
			}
			catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		{
			saveMdlButton = new Button(this, SWT.PUSH | SWT.CENTER);
			final GridData saveMdlButtonLData = new GridData();
			saveMdlButtonLData.horizontalAlignment = GridData.CENTER;
			saveMdlButtonLData.widthHint = 180;
			saveMdlButtonLData.heightHint = 26;
			saveMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			saveMdlButtonLData.grabExcessHorizontalSpace = true;
			saveMdlButton.setLayoutData(saveMdlButtonLData);
			saveMdlButton.setFont(font);
			saveMdlButton.setText("Save PDF/HTML/XML"); //$NON-NLS-1$
			saveMdlButton.setEnabled(false);
			saveMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "saveMdlButton.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.save();
				}
			});
		}
		{
			browser = new Browser(this, SWT.BORDER);
			final GridData viewTextLData = new GridData();
			viewTextLData.grabExcessHorizontalSpace = true;
			viewTextLData.verticalAlignment = GridData.FILL;
			viewTextLData.grabExcessVerticalSpace = true;
			viewTextLData.horizontalSpan = 4;
			viewTextLData.horizontalAlignment = GridData.FILL;
			browser.setLayoutData(viewTextLData);

			final Menu contextMenu = new Menu(browser);
			loadMdlMenuItem = new MenuItem(contextMenu, SWT.NONE);
			loadMdlMenuItem.setText("Load");
			loadMdlMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "loadMdlMenuItem.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.load();
				}
			});

			saveMdlMenuItem = new MenuItem(contextMenu, SWT.NONE);
			saveMdlMenuItem.setText("Save");
			saveMdlMenuItem.setEnabled(false);
			saveMdlMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "saveMdlMenuItem.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.save();
				}
			});

			new MenuItem(contextMenu, SWT.SEPARATOR);

			reloadMenuItem = new MenuItem(contextMenu, SWT.NONE);
			reloadMenuItem.setText("Reload");
			reloadMenuItem.setEnabled(false);
			reloadMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "reloadMenuItem.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.updateView(false);
				}
			});

			browser.setMenu(contextMenu);

			browser.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(final KeyEvent evt) {
					switch (evt.keyCode) {
					case SWT.F5:
						MdlTabItemComposite.this.updateView(false);
						break;
					}
				}

				@Override
				public void keyReleased(final KeyEvent evt) {
				}
			});
		}
	}

	private void load() {
		final FileDialog fd = new FileDialog(getParent().getShell(), SWT.SINGLE);
		fd.setFilterExtensions(new String[] { "*.mdl" }); //$NON-NLS-1$
		fd.setFilterNames(new String[] { "HoTT Transmitter Model Files (*.mdl)" }); //$NON-NLS-1$
		fd.setFilterPath(PREFS.get("lastLoadDir", System.getProperty(Launcher.MDL_DIR)));

		fd.open();
		if (fd.getFileName().length() > 4) {
			final File file = new File(fd.getFilterPath() + "/" + fd.getFileName()); //$NON-NLS-1$
			if (file.exists() && file.canRead()) {
				PREFS.put("lastLoadDir", file.getParentFile().getAbsolutePath());
				try {
					model = Report.getModel(file);
					saveMdlButton.setEnabled(true);
					saveMdlMenuItem.setEnabled(true);
					reloadMenuItem.setEnabled(true);
					updateView(false);
				}
				catch (final Throwable t) {
					final MessageBox mb = new MessageBox(getParent().getShell(), SWT.NONE);
					mb.setText(t.getClass().getSimpleName());
					mb.setMessage(t.getMessage() == null ? t.getClass().getSimpleName() : t.getMessage());
					mb.open();
					MdlTabItem.log.log(Level.SEVERE, t.getMessage(), t);
				}
			}
		}
	}

	private void save() {
		if (model != null) {
			final FileDialog fileSaveDialog = new FileDialog(getParent().getShell(), SWT.PRIMARY_MODAL | SWT.SAVE);
			fileSaveDialog.setText("save PDF"); //$NON-NLS-1$
			fileSaveDialog.setFilterExtensions(new String[] { "*.pdf", "*.html", "*.xml" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			fileSaveDialog.setFilterNames(new String[] { "Portable Document Format (*.pdf)", "Hypertext Markup Language (*.html)", "Extensible Markup Language (*.xml)" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			fileSaveDialog.setFilterPath(PREFS.get("lastSaveDir", PREFS.get("lastLoadDir", System.getProperty(Launcher.MDL_DIR))));
			fileSaveDialog.setFileName(model.getModelName() + ".pdf");
			fileSaveDialog.open();
			final File file = new File(fileSaveDialog.getFilterPath() + "/" + fileSaveDialog.getFileName()); //$NON-NLS-1$
			PREFS.put("lastSaveDir", file.getParentFile().getAbsolutePath());

			try {
				String data;
				if (file.getName().endsWith(".xml")) { //$NON-NLS-1$
					data = Report.generateXML(model);
				}
				else {
					data = Report.generateHTML(model);
				}

				if (file.getName().endsWith(".pdf")) { //$NON-NLS-1$
					Report.savePDF(file, data);
				}
				else {
					Report.save(file, data);
				}
			}
			catch (final Throwable e) {
				MdlTabItem.log.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

	private void updateView(final boolean isXML) {
		if (model != null) {
			String data;
			try {
				if (isXML) {
					data = Report.generateXML(model);
				}
				else {
					data = Report.generateHTML(model);
				}
			}
			catch (final Exception e) {
				data = e.getMessage();
			}

			browser.setText(data);
		}

		browser.setFocus();
	}
}