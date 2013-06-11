package gde.mdl.ui;

import gde.model.BaseModel;
import gde.report.Report;
import gde.report.SWTSVGReplacedElementFactory;

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
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.simple.SWTXHTMLRenderer;
import org.xhtmlrenderer.simple.xhtml.XhtmlNamespaceHandler;

public class MdlTabItemComposite extends Composite {
	private static final Preferences	PREFS	= Preferences.userNodeForPackage(MdlTabItemComposite.class);

	private Composite									browser;
	private Button										saveMdlButton;
	private MenuItem									saveMdlMenuItem;
	private Label											mdlVersionLabel;
	private Button										loadMdlButton;
	private MenuItem									loadMdlMenuItem;
	private MenuItem									reloadMenuItem;
	private BaseModel									model	= null;
	private Font											font;

	public MdlTabItemComposite(final Composite parent) {
		super(parent, SWT.NONE);
		GridLayout tabItemCompositeLayout = new GridLayout();
		tabItemCompositeLayout.numColumns = 3;
		this.setLayout(tabItemCompositeLayout);
		this.setBackground(new Color(Display.getDefault(), 250, 249, 211));
		FontData fontData = parent.getFont().getFontData()[0];
		fontData.setHeight(fontData.getHeight() - 1);
		this.font = SWTResourceManager.getFont(SwtMdlBrowser.WIDGET_FONT_NAME, SwtMdlBrowser.WIDGET_FONT_SIZE, SWT.NORMAL);
		Report.setSuppressExceptions(true);
		{
			this.loadMdlButton = new Button(this, SWT.PUSH | SWT.CENTER);
			GridData loadMdlButtonLData = new GridData();
			loadMdlButtonLData.widthHint = 180;
			loadMdlButtonLData.heightHint = 26;
			loadMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			loadMdlButtonLData.grabExcessHorizontalSpace = true;
			loadMdlButtonLData.horizontalAlignment = GridData.CENTER;
			this.loadMdlButton.setLayoutData(loadMdlButtonLData);
			this.loadMdlButton.setFont(this.font);
			this.loadMdlButton.setText("Load MDL"); //$NON-NLS-1$
			this.loadMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "loadMdlButton.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.load();
				}
			});
		}
		{
			this.mdlVersionLabel = new Label(this, SWT.CENTER);
			GridData mdlVersionLabelLData = new GridData();
			mdlVersionLabelLData.horizontalAlignment = GridData.CENTER;
			//mdlVersionLabelLData.widthHint = 180; // no line wrap
			mdlVersionLabelLData.heightHint = 26;
			mdlVersionLabelLData.verticalAlignment = GridData.BEGINNING;
			mdlVersionLabelLData.grabExcessHorizontalSpace = true;
			this.mdlVersionLabel.setLayoutData(mdlVersionLabelLData);
			this.mdlVersionLabel.setBackground(new Color(Display.getDefault(), 250, 249, 211));
			this.mdlVersionLabel.setFont(this.font);
			String version = "?"; //$NON-NLS-1$
			try {
				@SuppressWarnings("rawtypes")
				Class clazz = MdlTabItem.class;
				String className = clazz.getSimpleName() + ".class"; //$NON-NLS-1$
				String classPath = clazz.getResource(className).toString();
				if (classPath.startsWith("jar")) { //$NON-NLS-1$
					String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF"; //$NON-NLS-1$ //$NON-NLS-2$
					Manifest manifest = new Manifest(new URL(manifestPath).openStream());
					version = manifest.getMainAttributes().getValue("Implementation-Version") + "." + manifest.getMainAttributes().getValue("Implementation-Build");
				}
				System.setProperty(Launcher.PROGRAM_VERSION, version);
				this.mdlVersionLabel.setText("Implementation-Version: " + version); //$NON-NLS-1$
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		{
			this.saveMdlButton = new Button(this, SWT.PUSH | SWT.CENTER);
			GridData saveMdlButtonLData = new GridData();
			saveMdlButtonLData.horizontalAlignment = GridData.CENTER;
			saveMdlButtonLData.widthHint = 180;
			saveMdlButtonLData.heightHint = 26;
			saveMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			saveMdlButtonLData.grabExcessHorizontalSpace = true;
			this.saveMdlButton.setLayoutData(saveMdlButtonLData);
			this.saveMdlButton.setFont(this.font);
			this.saveMdlButton.setText("Save PDF/HTML/XML"); //$NON-NLS-1$
			this.saveMdlButton.setEnabled(false);
			this.saveMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "saveMdlButton.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.save();
				}
			});
		}
		{
			if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				// org.eclipse.swt.browser.Browser does not support SVG on Windows
				SWTXHTMLRenderer renderer = new SWTXHTMLRenderer(this, SWT.BORDER);
				SharedContext ctx = renderer.getSharedContext();
				ctx.getTextRenderer().setSmoothingThreshold(10);
				ctx.setReplacedElementFactory(new SWTSVGReplacedElementFactory(ctx.getReplacedElementFactory()));
				this.browser = renderer;
			}
			else {
				this.browser = new Browser(this, SWT.BORDER);
			}
			GridData viewTextLData = new GridData();
			viewTextLData.grabExcessHorizontalSpace = true;
			viewTextLData.verticalAlignment = GridData.FILL;
			viewTextLData.grabExcessVerticalSpace = true;
			viewTextLData.horizontalSpan = 4;
			viewTextLData.horizontalAlignment = GridData.FILL;
			this.browser.setLayoutData(viewTextLData);

			Menu contextMenu = new Menu(browser);
			this.loadMdlMenuItem = new MenuItem(contextMenu, SWT.NONE);
			this.loadMdlMenuItem.setText("Load");
			this.loadMdlMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "loadMdlMenuItem.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.load();
				}
			});

			this.saveMdlMenuItem = new MenuItem(contextMenu, SWT.NONE);
			this.saveMdlMenuItem.setText("Save");
			this.saveMdlMenuItem.setEnabled(false);
			this.saveMdlMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "saveMdlMenuItem.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.save();
				}
			});

			new MenuItem(contextMenu, SWT.SEPARATOR);

			this.reloadMenuItem = new MenuItem(contextMenu, SWT.NONE);
			this.reloadMenuItem.setText("Reload");
			this.reloadMenuItem.setEnabled(false);
			this.reloadMenuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					MdlTabItem.log.log(Level.FINEST, "reloadMenuItem.widgetSelected, event=" + evt); //$NON-NLS-1$
					MdlTabItemComposite.this.updateView(false);
				}
			});

			this.browser.setMenu(contextMenu);

			this.browser.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent evt) {
				}

				@Override
				public void keyPressed(KeyEvent evt) {
					switch (evt.keyCode) {
					case SWT.F5:
						MdlTabItemComposite.this.updateView(false);
						break;
					}
				}
			});
		}
	}

	private void updateView(boolean isXML) {
		if (this.model != null) {
			String data;
			try {
				if (isXML) {
					data = Report.generateXML(this.model);
				}
				else {
					data = Report.generateHTML(this.model);
				}
			}
			catch (Exception e) {
				data = e.getMessage();
			}

			if (browser instanceof Browser) {
				((Browser) this.browser).setText(data);
			}
			else if (browser instanceof SWTXHTMLRenderer) {
				((SWTXHTMLRenderer) this.browser).setDocumentFromString(data, "", new XhtmlNamespaceHandler());
			}
		}

		this.browser.setFocus();
	}

	private void load() {
		final FileDialog fd = new FileDialog(this.getParent().getShell(), SWT.SINGLE);
		fd.setFilterExtensions(new String[] { "*.mdl" }); //$NON-NLS-1$
		fd.setFilterNames(new String[] { "HoTT Transmitter Model Files (*.mdl)" }); //$NON-NLS-1$
		fd.setFilterPath(PREFS.get("lastLoadDir", System.getProperty(Launcher.MDL_DIR)));

		fd.open();
		if (fd.getFileName().length() > 4) {
			final File file = new File(fd.getFilterPath() + "/" + fd.getFileName()); //$NON-NLS-1$
			if (file.exists() && file.canRead()) {
				PREFS.put("lastLoadDir", file.getParentFile().getAbsolutePath());
				try {
					this.model = Report.getModel(file);
					this.saveMdlButton.setEnabled(true);
					this.saveMdlMenuItem.setEnabled(true);
					this.reloadMenuItem.setEnabled(true);
					updateView(false);
				}
				catch (final Throwable t) {
					MessageBox mb = new MessageBox(this.getParent().getShell(), SWT.NONE);
					mb.setText(t.getClass().getSimpleName());
					mb.setMessage(t.getMessage() == null ? t.getClass().getSimpleName() : t.getMessage());
					mb.open();
					MdlTabItem.log.log(Level.SEVERE, t.getMessage(), t);
				}
			}
		}
	}

	private void save() {
		if (this.model != null) {
			FileDialog fileSaveDialog = new FileDialog(this.getParent().getShell(), SWT.PRIMARY_MODAL | SWT.SAVE);
			fileSaveDialog.setText("save PDF"); //$NON-NLS-1$
			fileSaveDialog.setFilterExtensions(new String[] { "*.pdf", "*.html", "*.xml" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			fileSaveDialog.setFilterNames(new String[] { "Portable Document Format (*.pdf)", "Hypertext Markup Language (*.html)", "Extensible Markup Language (*.xml)" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			fileSaveDialog.setFilterPath(PREFS.get("lastSaveDir", PREFS.get("lastLoadDir", System.getProperty(Launcher.MDL_DIR))));
			fileSaveDialog.setFileName(this.model.getModelName() + ".pdf");
			fileSaveDialog.open();
			File file = new File(fileSaveDialog.getFilterPath() + "/" + fileSaveDialog.getFileName()); //$NON-NLS-1$
			PREFS.put("lastSaveDir", file.getParentFile().getAbsolutePath());

			try {
				String data;
				if (file.getName().endsWith(".xml")) { //$NON-NLS-1$
					data = Report.generateXML(this.model);
				}
				else {
					data = Report.generateHTML(this.model);
				}

				if (file.getName().endsWith(".pdf")) { //$NON-NLS-1$
					Report.savePDF(file, data);
				}
				else {
					Report.save(file, data);
				}
			}
			catch (Throwable e) {
				MdlTabItem.log.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}
}
