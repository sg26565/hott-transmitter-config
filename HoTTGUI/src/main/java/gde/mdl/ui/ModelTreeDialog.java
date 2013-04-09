package gde.mdl.ui;

import gde.mdl.ui.composites.BaseConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ModelTreeDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private Menu				menu;
	private MenuItem		saveItem;
	private TreeItem treeItem2;
	private TreeItem treeItem1;
	private Composite editComposite;
	private Composite mainComposite;
	private Tree menuTree;
	private MenuItem		loadItem;
	private MenuItem		quitMenuItem;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			ModelTreeDialog inst = new ModelTreeDialog(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ModelTreeDialog(Shell parent, int style) {
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
			FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			dialogShell.setLayout(dialogShellLayout);
			{
				mainComposite = new Composite(dialogShell, SWT.NONE);
				GridLayout mainCompositeLayout = new GridLayout();
				mainCompositeLayout.numColumns = 3;
				mainComposite.setLayout(mainCompositeLayout);
				{
					GridData menuTreeLData = new GridData();
					menuTreeLData.grabExcessVerticalSpace = true;
					menuTreeLData.widthHint = 150;
					menuTreeLData.verticalAlignment = GridData.FILL;
					menuTree = new Tree(mainComposite, SWT.BORDER);
					menuTree.setLayoutData(menuTreeLData);
					{
						treeItem1 = new TreeItem(menuTree, SWT.NONE);
						treeItem1.setText("Grundeinstellung");
					}
					{
						treeItem2 = new TreeItem(menuTree, SWT.NONE);
						treeItem2.setText("Servoeinstellung");
					}

				}
				{
					editComposite = new Composite(mainComposite, SWT.BORDER);
					FillLayout editCompositeLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
					GridData editCompositeLData = new GridData();
					editCompositeLData.horizontalSpan = 2;
					editCompositeLData.grabExcessVerticalSpace = true;
					editCompositeLData.widthHint = 400;
					editCompositeLData.verticalAlignment = GridData.FILL;
					editComposite.setLayoutData(editCompositeLData);
					editComposite.setLayout(editCompositeLayout);
					{
						new BaseConfiguration(editComposite);
					}
				}
			}
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.layout();
			dialogShell.pack();			
			dialogShell.setSize(550, 500);
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
