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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

/**
 * tab item for DataExplorer integration of MDL reading and display purpose
 */
public class MdlTabItem extends CTabItem {

	private Browser browser;
	private Button saveMdlButton;
	private Button loadMdlButton;
	private Composite tabItemComposite;

	private File								lastLoadDir		= null;
	private BaseModel						model					= null;

	/**
	 * @param parent
	 * @param style
	 */
	public MdlTabItem(CTabFolder parent, int style) {
		super(parent, style);
		this.setText("MDL View");
		this.open(parent);
	}

	/**
	 * @param parent
	 * @param style
	 * @param index
	 */
	public MdlTabItem(CTabFolder parent, int style, int index) {
		super(parent, style, index);
		this.setText("MDL View");
		this.open(parent);
	}

	public void open(final CTabFolder parent) {
		tabItemComposite = new Composite(parent, SWT.NONE);
		GridLayout tabItemCompositeLayout = new GridLayout();
		tabItemCompositeLayout.numColumns = 4;
		tabItemComposite.setLayout(tabItemCompositeLayout);
		this.setControl(tabItemComposite);
		{
			loadMdlButton = new Button(tabItemComposite, SWT.PUSH | SWT.CENTER);
			GridData loadMdlButtonLData = new GridData();
			loadMdlButtonLData.widthHint = 120;
			loadMdlButtonLData.heightHint = 26;
			loadMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			loadMdlButtonLData.grabExcessHorizontalSpace = true;
			loadMdlButtonLData.horizontalAlignment = GridData.CENTER;
			loadMdlButton.setLayoutData(loadMdlButtonLData);
			loadMdlButton.setText("Load MDL");
			loadMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					System.out.println("loadMdlButton.widgetSelected, event="+evt);

					
					final FileDialog fd = new FileDialog(parent.getShell(), SWT.SINGLE);
					fd.setFilterExtensions(new String[] {"*.mdl"});
					fd.setFilterNames(new String[] {"HoTT Transmitter Model Files"});
					if (lastLoadDir != null) {
						fd.setFilterPath(lastLoadDir.getPath());
					}
					else {
						fd.setFilterPath(System.getProperty("program.dir"));
					}

					fd.open();
					if (fd.getFileName().length() > 4) {
						final File file = new File(fd.getFilterPath() + "/" + fd.getFileName());
	
						if (file.exists() && file.canRead()) {
							lastLoadDir = file.getParentFile();
							try {
								model = Report.getModel(file);
								saveMdlButton.setEnabled(true);
								updateView(false);
							}
							catch (final Throwable t) {
								MessageBox mb = new MessageBox(parent.getShell(), SWT.NONE);
								mb.setText(t.getMessage());
								mb.open();
								System.err.println(t.getStackTrace());
							}
						}
					}
				}
			});
		}
		{
			saveMdlButton = new Button(tabItemComposite, SWT.PUSH | SWT.CENTER);
			GridData saveMdlButtonLData = new GridData();
			saveMdlButtonLData.horizontalAlignment = GridData.CENTER;
			saveMdlButtonLData.widthHint = 120;
			saveMdlButtonLData.heightHint = 26;
			saveMdlButtonLData.verticalAlignment = GridData.BEGINNING;
			saveMdlButtonLData.grabExcessHorizontalSpace = true;
			saveMdlButton.setLayoutData(saveMdlButtonLData);
			saveMdlButton.setText("save PDF/HTML/XML");
			saveMdlButton.setEnabled(false);
			saveMdlButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					System.out.println("saveMdlButton.widgetSelected, event="+evt);
					FileDialog fileSaveDialog = new FileDialog(parent.getShell(), SWT.PRIMARY_MODAL | SWT.SAVE);
					fileSaveDialog.setText("save PDF");
					fileSaveDialog.setFilterExtensions(new String[] {"*.pdf", "*.html", "*.xml"});
					fileSaveDialog.setFilterNames(new String[] {"Portable Document Format (*.pdf)", "Hypertext Markup Language (*.html)", "Extensible Markup Language (*.xml)"});
					fileSaveDialog.setFilterPath(lastLoadDir != null ? lastLoadDir.getPath() : System.getProperty("program.dir"));
					fileSaveDialog.open();
					File file = new File(fileSaveDialog.getFilterPath() + "/" + fileSaveDialog.getFileName());
					try {
						String data;
						if (file.getName().endsWith(".xml")) {
							data = Report.generateXML(model);
						}
						else {
							data = Report.generateHTML(model);
						}

						if (file.getName().endsWith(".pdf")) {
							Report.savePDF(file, data);
						}
						else {
							Report.save(file, data);
						}
					}
					catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
			});
		}
		{
			browser = new Browser(tabItemComposite, SWT.MULTI | SWT.LEFT | SWT.BORDER);
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
			}
			else {
				browser.setText(Report.generateHTML(model));
			}
		}
		catch (Exception e) {
			browser.setText(e.getMessage());
		}
	}


}
