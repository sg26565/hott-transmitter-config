package gde.mdl.ui;

import gde.mdl.messages.Messages;
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

  public enum Config {
    BASICS(Messages.getString("ModelTreeDialog.BaseSettings")), DUALEXPO(Messages.getString("ModelTreeDialog.DR_Expo")); //$NON-NLS-1$ //$NON-NLS-2$
    public static Config fromValue(final String v) {
      for (final Config c : Config.values()) {
        if (c.value.equals(v)) {
          return c;
        }
      }
      throw new IllegalArgumentException(v);
    }

    public static List<Config> getAsList() {
      final List<Config> sensors = new ArrayList<Config>();
      for (final Config sensor : Config.values()) {
        sensors.add(sensor);
      }
      return sensors;
    }

    private final String	value;

    private Config(final String v) {
      value = v;
    }

    public String value() {
      return value;
    }
  }
  /**
   * Auto-generated main method to display this
   * org.eclipse.swt.widgets.Dialog inside a new Shell.
   */
  public static void main(final String[] args) {
    try {
      final Display display = Display.getDefault();
      final Shell shell = new Shell(display);
      final ModelTreeDialog inst = new ModelTreeDialog(shell, SWT.NULL);
      inst.open();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
  private Shell dialogShell;
  private Menu				menu;
  private MenuItem		saveItem;
  private TreeItem treeItem2;
  private TreeItem treeItem1;
  private Composite editComposite;
  private Composite mainComposite;
  private Composite edit;
  private Tree menuTree;

  private MenuItem		loadItem;;

  private MenuItem		quitMenuItem;

  public ModelTreeDialog(final Shell parent, final int style) {
    super(parent, style);
  }

  public void open() {
    try {
      Launcher.initSystemProperties();
      final Shell parent = getParent();
      dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
      dialogShell.setText(Messages.getString("ModelTreeDialog.Title")); //$NON-NLS-1$
      {
        menu = new Menu(dialogShell, SWT.BAR | SWT.LEFT_TO_RIGHT);
        dialogShell.setMenuBar(menu);
        {
          quitMenuItem = new MenuItem(menu, SWT.CASCADE);
          quitMenuItem.setText(Messages.getString("ModelTreeDialog.Quit")); //$NON-NLS-1$
        }
        {
          loadItem = new MenuItem(menu, SWT.CASCADE);
          loadItem.setText(Messages.getString("ModelTreeDialog.LoadMdl")); //$NON-NLS-1$
        }
        {
          saveItem = new MenuItem(menu, SWT.PUSH);
          saveItem.setText(Messages.getString("ModelTreeDialog.SaveMdl")); //$NON-NLS-1$
          saveItem.setEnabled(false);
        }
      }
      final FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
      dialogShell.setLayout(dialogShellLayout);
      {
        mainComposite = new Composite(dialogShell, SWT.NONE);
        final GridLayout mainCompositeLayout = new GridLayout();
        mainCompositeLayout.numColumns = 3;
        mainComposite.setLayout(mainCompositeLayout);
        {
          final GridData menuTreeLData = new GridData();
          menuTreeLData.grabExcessVerticalSpace = true;
          menuTreeLData.widthHint = 150;
          menuTreeLData.verticalAlignment = GridData.FILL;
          menuTree = new Tree(mainComposite, SWT.BORDER);
          menuTree.setLayoutData(menuTreeLData);
          menuTree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent evt) {
              System.out.println(evt);
              if (edit != null) {
                edit.dispose();
              }
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
          final FormLayout editCompositeLayout = new FormLayout();
          editComposite.setLayout(editCompositeLayout);
          final GridData editCompositeLData = new GridData();
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
      final Display display = dialogShell.getDisplay();
      while (!dialogShell.isDisposed()) {
        if (!display.readAndDispatch()) {
          display.sleep();
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
