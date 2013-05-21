/*************************************************************************************
  	This file is part of GNU DataExplorer.

    GNU DataExplorer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GNU DataExplorer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GNU DataExplorer.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright (c) 2013 Winfried Bruegmann
 **************************************************************************************/
package gde.mdl.ui;

import gde.model.BaseModel;
import gde.report.Report;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
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

/**
 * tab item for DataExplorer integration of MDL reading and display purpose
 */
public class MdlTabItem extends CTabItem {
	final static Logger					log								= Logger.getLogger(MdlTabItem.class.getName());

	private Browser							browser;
	private Button							saveMdlButton;
	private Label								mdlVersionLabel;
	private Button							loadMdlButton;
	private Composite						tabItemComposite;

	private File								lastLoadDir				= null;
	private BaseModel						model							= null;
	private Font								font;

	public static final boolean	IS_WINDOWS				= System.getProperty("os.name").toLowerCase().startsWith("windows");																//$NON-NLS-1$ //$NON-NLS-2$
	public static final boolean	IS_LINUX					= System.getProperty("os.name").toLowerCase().startsWith("linux");																	//$NON-NLS-1$ //$NON-NLS-2$
	public static final boolean	IS_MAC						= System.getProperty("os.name").toLowerCase().startsWith("mac");																		//$NON-NLS-1$ //$NON-NLS-2$

	public final static int			WIDGET_FONT_SIZE	= MdlTabItem.IS_MAC ? 12 : ((MdlTabItem.IS_LINUX ? 8 : 9) * 96 / Display.getDefault().getDPI().y);
	public final static String	WIDGET_FONT_NAME	= MdlTabItem.IS_WINDOWS ? "Microsoft Sans Serif" : "Sans Serif";																		//$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * @param parent
	 * @param style
	 */
	public MdlTabItem(CTabFolder parent, int style) {
		super(parent, style);
		this.font = new Font(Display.getDefault(), new FontData(MdlTabItem.WIDGET_FONT_NAME, MdlTabItem.WIDGET_FONT_SIZE, SWT.NORMAL));
		this.setFont(new Font(Display.getDefault(), new FontData(MdlTabItem.WIDGET_FONT_NAME, MdlTabItem.WIDGET_FONT_SIZE + 1, SWT.NORMAL)));
		this.setText("MDL Viewer"); //$NON-NLS-1$
		Report.setSuppressExceptions(true);
		this.open(parent);
	}

	/**
	 * @param parent
	 * @param style
	 * @param index
	 */
	public MdlTabItem(CTabFolder parent, int style, int index) {
		super(parent, style, index);
		this.font = new Font(Display.getDefault(), new FontData(MdlTabItem.WIDGET_FONT_NAME, MdlTabItem.WIDGET_FONT_SIZE, SWT.NORMAL));
		this.setFont(new Font(Display.getDefault(), new FontData(MdlTabItem.WIDGET_FONT_NAME, MdlTabItem.WIDGET_FONT_SIZE + 1, SWT.NORMAL)));
		this.setFont(this.font);
		this.setText("MDL Viewer"); //$NON-NLS-1$
		Report.setSuppressExceptions(true);
		this.open(parent);
	}

	public void open(final CTabFolder parent) {
		this.tabItemComposite = new Composite(parent, SWT.NONE);
		GridLayout tabItemCompositeLayout = new GridLayout();
		tabItemCompositeLayout.numColumns = 3;
		this.tabItemComposite.setLayout(tabItemCompositeLayout);
		this.setControl(this.tabItemComposite);
		this.tabItemComposite.setBackground(new Color(Display.getDefault(), 250, 249, 211));
		{
			this.loadMdlButton = new Button(this.tabItemComposite, SWT.PUSH | SWT.CENTER);
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
					if (MdlTabItem.this.lastLoadDir != null) {
						fd.setFilterPath(MdlTabItem.this.lastLoadDir.getPath());
					}
					else {
						fd.setFilterPath(System.getProperty("mdl.dir")); //$NON-NLS-1$
					}

					fd.open();
					if (fd.getFileName().length() > 4) {
						final File file = new File(fd.getFilterPath() + "/" + fd.getFileName()); //$NON-NLS-1$
						if (file.exists() && file.canRead()) {
							MdlTabItem.this.lastLoadDir = file.getParentFile();
							try {
								MdlTabItem.this.model = Report.getModel(file);
								MdlTabItem.this.saveMdlButton.setEnabled(true);
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
			this.mdlVersionLabel = new Label(this.tabItemComposite, SWT.CENTER);
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
			this.saveMdlButton = new Button(this.tabItemComposite, SWT.PUSH | SWT.CENTER);
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
					fileSaveDialog.setFilterPath(MdlTabItem.this.lastLoadDir != null ? MdlTabItem.this.lastLoadDir.getPath() : System.getProperty("mdl.dir")); //$NON-NLS-1$
					fileSaveDialog.open();
					File file = new File(fileSaveDialog.getFilterPath() + "/" + fileSaveDialog.getFileName()); //$NON-NLS-1$
					try {
						String data;
						if (file.getName().endsWith(".xml")) { //$NON-NLS-1$
							data = Report.generateXML(MdlTabItem.this.model);
						}
						else {
							data = Report.generateHTML(MdlTabItem.this.model);
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
			this.browser = new Browser(this.tabItemComposite, SWT.BORDER);
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
