package gde.mdl.ui;

import gde.mdl.ui.composites.BaseConfiguration;
import gde.mdl.ui.composites.DualExpoConfiguration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
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
	private Composite edit;
	private Tree menuTree;
	private MenuItem		loadItem;
	private MenuItem		quitMenuItem;
	
	public enum Config { 
		BASICS("Grundeinstellung"), DUALEXPO("Dualrate/Expotential");
	private final String	value;

	private Config(String v) {
		this.value = v;
	}

	public String value() {
		return this.value;
	}
	
	public static Config fromValue(String v) {
		for (Config c : Config.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
	
	public static List<Config> getAsList() {
		List<Config> sensors = new ArrayList<Config>();
		for (Config sensor : Config.values()) {
			sensors.add(sensor);
		}
		return sensors;
	}
};

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
			Launcher.initSystemProperties();
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
					menuTree.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent evt) {
							System.out.println(evt);
							if (edit != null) edit.dispose();
							switch (Config.fromValue(((TreeItem)evt.item).getText())) {
							case BASICS:
							default:
								edit = new BaseConfiguration(editComposite, Config.BASICS.value);
								break;
							case DUALEXPO:
								edit = new DualExpoConfiguration(editComposite, Config.DUALEXPO.value);
								break;
							}
							editComposite.layout();
						}
					});
					{
						treeItem1 = new TreeItem(menuTree, SWT.NONE);
						treeItem1.setText(Config.BASICS.value);
					}
					{
						treeItem2 = new TreeItem(menuTree, SWT.NONE);
						treeItem2.setText(Config.DUALEXPO.value);
					}

				}
				{
					editComposite = new Composite(mainComposite, SWT.BORDER);
					FormLayout editCompositeLayout = new FormLayout();
					editComposite.setLayout(editCompositeLayout);
					GridData editCompositeLData = new GridData();
					editCompositeLData.horizontalSpan = 2;
					editCompositeLData.grabExcessVerticalSpace = true;
					editCompositeLData.widthHint = 400;
					editCompositeLData.verticalAlignment = GridData.FILL;
					editComposite.setLayoutData(editCompositeLData);
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
