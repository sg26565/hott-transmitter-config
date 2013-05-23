package gde.mdl.ui;

import gde.model.BaseModel;
import gde.report.Report;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
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
import org.eclipse.swt.widgets.MessageBox;

public class MdlTabItemComposite extends Composite {

	private Browser		browser;
	private Button		saveMdlButton;
	private Label			mdlVersionLabel;
	private Button		loadMdlButton;
	private File			lastLoadDir	= null;
	private BaseModel	model				= null;
	private Font			font;

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

					final FileDialog fd = new FileDialog(parent.getShell(), SWT.SINGLE);
					fd.setFilterExtensions(new String[] { "*.mdl" }); //$NON-NLS-1$
					fd.setFilterNames(new String[] { "HoTT Transmitter Model Files (*.mdl)" }); //$NON-NLS-1$
					if (MdlTabItemComposite.this.lastLoadDir != null) {
						fd.setFilterPath(MdlTabItemComposite.this.lastLoadDir.getPath());
					}
					else {
						fd.setFilterPath(System.getProperty("mdl.dir")); //$NON-NLS-1$
					}

					fd.open();
					if (fd.getFileName().length() > 4) {
						final File file = new File(fd.getFilterPath() + "/" + fd.getFileName()); //$NON-NLS-1$
						if (file.exists() && file.canRead()) {
							MdlTabItemComposite.this.lastLoadDir = file.getParentFile();
							try {
								MdlTabItemComposite.this.model = Report.getModel(file);
								MdlTabItemComposite.this.saveMdlButton.setEnabled(true);
								updateView(false);
							}
							catch (final Throwable t) {
								MessageBox mb = new MessageBox(parent.getShell(), SWT.NONE);
								mb.setText(t.getClass().getSimpleName());
								mb.setMessage(t.getMessage() == null ? t.getClass().getSimpleName() : t.getMessage());
								mb.open();
								MdlTabItem.log.log(Level.SEVERE, t.getMessage(), t);
							}
						}
					}
				}
			});
		}
		{
			this.mdlVersionLabel = new Label(this, SWT.CENTER);
			GridData mdlVersionLabelLData = new GridData();
			mdlVersionLabelLData.horizontalAlignment = GridData.CENTER;
			mdlVersionLabelLData.widthHint = 180;
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
					version = manifest.getMainAttributes().getValue("Implementation-Version"); //$NON-NLS-1$			
				}
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
					FileDialog fileSaveDialog = new FileDialog(parent.getShell(), SWT.PRIMARY_MODAL | SWT.SAVE);
					fileSaveDialog.setText("save PDF"); //$NON-NLS-1$
					fileSaveDialog.setFilterExtensions(new String[] { "*.pdf", "*.html", "*.xml" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					fileSaveDialog.setFilterNames(new String[] { "Portable Document Format (*.pdf)", "Hypertext Markup Language (*.html)", "Extensible Markup Language (*.xml)" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					fileSaveDialog.setFilterPath(MdlTabItemComposite.this.lastLoadDir != null ? MdlTabItemComposite.this.lastLoadDir.getPath() : System.getProperty("mdl.dir")); //$NON-NLS-1$
					fileSaveDialog.open();
					File file = new File(fileSaveDialog.getFilterPath() + "/" + fileSaveDialog.getFileName()); //$NON-NLS-1$
					try {
						String data;
						if (file.getName().endsWith(".xml")) { //$NON-NLS-1$
							data = Report.generateXML(MdlTabItemComposite.this.model);
						}
						else {
							data = Report.generateHTML(MdlTabItemComposite.this.model);
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
			});
		}
		{
			this.browser = new Browser(this, SWT.BORDER);
			GridData viewTextLData = new GridData();
			viewTextLData.grabExcessHorizontalSpace = true;
			viewTextLData.verticalAlignment = GridData.FILL;
			viewTextLData.grabExcessVerticalSpace = true;
			viewTextLData.horizontalSpan = 4;
			viewTextLData.horizontalAlignment = GridData.FILL;
			this.browser.setLayoutData(viewTextLData);
		}
	}

	private void updateView(boolean isXML) {
		try {
			if (isXML) {
				this.browser.setUrl(Report.generateXML(this.model));
			}
			else {
				this.browser.setText(Report.generateHTML(this.model));
			}
		}
		catch (Exception e) {
			this.browser.setText(e.getMessage());
		}
	}
}
