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
	final static Logger log = Logger.getLogger(MdlTabItem.class.getName());

	private Browser browser;
	private Button saveMdlButton;
	private Label		mdlVersionLabel;
	private Button loadMdlButton;
	private Composite tabItemComposite;

	private File lastLoadDir = null;
	private BaseModel model = null;

	/**
	 * @param parent
	 * @param style
	 */
	public MdlTabItem(CTabFolder parent, int style) {
		super(parent, style);
		this.setText("MDL Viewer");
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
		this.setText("MDL Viewer");
		Report.setSuppressExceptions(true);
		this.open(parent);
	}

	public void open(final CTabFolder parent) {
		tabItemComposite = new Composite(parent, SWT.NONE);
		GridLayout tabItemCompositeLayout = new GridLayout();
		tabItemCompositeLayout.numColumns = 3;
		tabItemComposite.setLayout(tabItemCompositeLayout);
		this.setControl(tabItemComposite);
		tabItemComposite.setBackground(new Color(Display.getDefault(), 250,	249, 211));
		{
			loadMdlButton = new Button(tabItemComposite, SWT.PUSH | SWT.CENTER);
			GridData loadMdlButtonLData = new GridData();
			loadMdlButtonLData.widthHint = 180;
			loadMdlButtonLData.heightHint = 26;
			loadMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			loadMdlButtonLData.grabExcessHorizontalSpace = true;
			loadMdlButtonLData.horizontalAlignment = GridData.CENTER;
			loadMdlButton.setLayoutData(loadMdlButtonLData);
			loadMdlButton.setText("Load MDL");
			loadMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					log.log(Level.FINEST, "loadMdlButton.widgetSelected, event=" + evt);

					final FileDialog fd = new FileDialog(parent.getShell(),	SWT.SINGLE);
					fd.setFilterExtensions(new String[] { "*.mdl" });
					fd.setFilterNames(new String[] { "HoTT Transmitter Model Files (*.mdl)" });
					if (lastLoadDir != null) {
						fd.setFilterPath(lastLoadDir.getPath());
					} else {
						fd.setFilterPath(System.getProperty("mdl.dir"));
					}

					fd.open();
					if (fd.getFileName().length() > 4) {
						final File file = new File(fd.getFilterPath() + "/"	+ fd.getFileName());
						if (file.exists() && file.canRead()) {
							lastLoadDir = file.getParentFile();
							try {
								model = Report.getModel(file);
								saveMdlButton.setEnabled(true);
								updateView(false);
							} catch (final Throwable t) {
								MessageBox mb = new MessageBox(parent.getShell(), SWT.NONE);
								mb.setText(t.getClass().getSimpleName());
								mb.setMessage(t.getMessage() == null ? t.getClass().getSimpleName() : t.getMessage());
								mb.open();
								log.log(Level.SEVERE, t.getMessage(), t);
							}
						}
					}
				}
			});
		}
		{
			mdlVersionLabel = new Label(tabItemComposite, SWT.CENTER);
			GridData mdlVersionLabelLData = new GridData();
			mdlVersionLabelLData.horizontalAlignment = GridData.CENTER;
			mdlVersionLabelLData.widthHint = 180;
			mdlVersionLabelLData.heightHint = 26;
			mdlVersionLabelLData.verticalAlignment = GridData.BEGINNING;
			mdlVersionLabelLData.grabExcessHorizontalSpace = true;
			mdlVersionLabel.setLayoutData(mdlVersionLabelLData);
			mdlVersionLabel.setBackground(new Color(Display.getDefault(), 250,	249, 211));
			String version = "?";
			try {
				@SuppressWarnings("rawtypes")
				Class clazz = MdlTabItem.class;
				String className = clazz.getSimpleName() + ".class";
				String classPath = clazz.getResource(className).toString();
				if (classPath.startsWith("jar")) {
					String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
					Manifest manifest = new Manifest(new URL(manifestPath).openStream());
					version = manifest.getMainAttributes().getValue("Implementation-Version"); //$NON-NLS-1$			
				}
				mdlVersionLabel.setText("Implementation-Version: " + version);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		{
			saveMdlButton = new Button(tabItemComposite, SWT.PUSH | SWT.CENTER);
			GridData saveMdlButtonLData = new GridData();
			saveMdlButtonLData.horizontalAlignment = GridData.CENTER;
			saveMdlButtonLData.widthHint = 180;
			saveMdlButtonLData.heightHint = 26;
			saveMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			saveMdlButtonLData.grabExcessHorizontalSpace = true;
			saveMdlButton.setLayoutData(saveMdlButtonLData);
			saveMdlButton.setText("Save PDF/HTML/XML");
			saveMdlButton.setEnabled(false);
			saveMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					log.log(Level.FINEST, "saveMdlButton.widgetSelected, event="
							+ evt);
					FileDialog fileSaveDialog = new FileDialog(parent
							.getShell(), SWT.PRIMARY_MODAL | SWT.SAVE);
					fileSaveDialog.setText("save PDF");
					fileSaveDialog.setFilterExtensions(new String[] { "*.pdf",
							"*.html", "*.xml" });
					fileSaveDialog.setFilterNames(new String[] {
							"Portable Document Format (*.pdf)",
							"Hypertext Markup Language (*.html)",
							"Extensible Markup Language (*.xml)" });
					fileSaveDialog.setFilterPath(lastLoadDir != null ? lastLoadDir.getPath() : System.getProperty("mdl.dir"));
					fileSaveDialog.open();
					File file = new File(fileSaveDialog.getFilterPath() + "/" + fileSaveDialog.getFileName());
					try {
						String data;
						if (file.getName().endsWith(".xml")) {
							data = Report.generateXML(model);
						} else {
							data = Report.generateHTML(model);
						}

						if (file.getName().endsWith(".pdf")) {
							Report.savePDF(file, data);
						} else {
							Report.save(file, data);
						}
					} catch (Throwable e) {
						log.log(Level.WARNING, e.getMessage(), e);
					}
				}
			});
		}
		{
			browser = new Browser(tabItemComposite, SWT.BORDER);
			GridData viewTextLData = new GridData();
			viewTextLData.grabExcessHorizontalSpace = true;
			viewTextLData.verticalAlignment = GridData.FILL;
			viewTextLData.grabExcessVerticalSpace = true;
			viewTextLData.horizontalSpan = 4;
			viewTextLData.horizontalAlignment = GridData.FILL;
			browser.setLayoutData(viewTextLData);
		}
	}

	private void updateView(boolean isXML) {
		try {
			if (isXML) {
				browser.setUrl(Report.generateXML(model));
			} else {
				browser.setText(Report.generateHTML(model));
			}
		} catch (Exception e) {
			browser.setText(e.getMessage());
		}
	}

}
