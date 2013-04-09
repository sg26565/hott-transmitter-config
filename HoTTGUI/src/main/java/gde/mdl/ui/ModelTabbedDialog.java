package gde.mdl.ui;

import gde.mdl.ui.composites.BaseConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class ModelTabbedDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell				dialogShell;
	private Menu				menu;
	private MenuItem		saveItem;
	private MenuItem		loadItem;
	private MenuItem		quitMenuItem;
	private CTabItem		cTabItem1;
	private CTabFolder	mainTabFolder;
	private Composite		mainComposite;

	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
	 * inside a new Shell.
	 */
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			ModelTabbedDialog inst = new ModelTabbedDialog(shell, SWT.NULL);
			inst.open();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ModelTabbedDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialogShell.setText("Graupner/SJ HoTT MDL Explorer");
			{
				menu = new Menu(dialogShell, SWT.BAR | SWT.LEFT_TO_RIGHT);
				dialogShell.setMenuBar(menu);
				{
					quitMenuItem = new MenuItem(menu, SWT.CASCADE);
					quitMenuItem.setText("  Quit  ");
				}
				{
					loadItem = new MenuItem(menu, SWT.CASCADE);
					loadItem.setText(" load MDL ");
				}
				{
					saveItem = new MenuItem(menu, SWT.PUSH);
					saveItem.setText(" save MDL ");
					saveItem.setEnabled(false);
				}
			}
			{
				mainComposite = new Composite(dialogShell, SWT.NONE);
				FillLayout composite1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
				mainComposite.setLayout(composite1Layout);
				mainComposite.setBounds(0, 0, 500, 300);
				{
					mainTabFolder = new CTabFolder(mainComposite, SWT.NONE);
					{
						cTabItem1 = new CTabItem(mainTabFolder, SWT.NONE);
						cTabItem1.setText("Grundeinstellung");
						{
							new BaseConfiguration(mainTabFolder, cTabItem1);
						}
					}
					mainTabFolder.setSelection(0);
				}
				mainComposite.layout();
			}
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setSize(500, 350);
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch()) display.sleep();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
