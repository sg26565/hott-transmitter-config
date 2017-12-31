package de.treichels.hott.mdlviewer.swt;

import de.treichels.hott.mdlviewer.swt.composites.BaseConfiguration;
import de.treichels.hott.mdlviewer.swt.composites.DualExpoConfiguration;
import de.treichels.hott.messages.Messages;
import de.treichels.hott.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class ModelTreeDialog extends org.eclipse.swt.widgets.Dialog {

    public enum Config {
        BASICS(Messages.getString("ModelTreeDialog.BaseSettings")), DUALEXPO(Messages.getString("ModelTreeDialog.DR_Expo"));
        static Config fromValue(final String v) {
            for (final Config c : Config.values())
                if (c.value.equals(v)) return c;
            throw new IllegalArgumentException(v);
        }

        private final String value;

        Config(final String v) {
            value = v;
        }
    }

    /**
     * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog inside a new Shell.
     */
    public static void main(final String[] args) {
        try {
            final Display display = Display.getDefault();
            final Shell shell = new Shell(display);
            final ModelTreeDialog inst = new ModelTreeDialog(shell);
            inst.open();
        } catch (final Exception e) {
            if (Util.INSTANCE.getDEBUG()) e.printStackTrace();
        }
    }

    private Composite editComposite;
    private Composite edit;

    private ModelTreeDialog(final Shell parent) {
        super(parent, SWT.NULL);
    }

    private void open() {
        try {
            Launcher.initSystemProperties();
            final Shell parent = getParent();
            Shell dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            dialogShell.setText(Messages.getString("ModelTreeDialog.Title"));
            {
                Menu menu = new Menu(dialogShell, SWT.BAR | SWT.LEFT_TO_RIGHT);
                dialogShell.setMenuBar(menu);
                {
                    MenuItem quitMenuItem = new MenuItem(menu, SWT.CASCADE);
                    quitMenuItem.setText(Messages.getString("ModelTreeDialog.Quit"));
                }
                {
                    MenuItem loadItem = new MenuItem(menu, SWT.CASCADE);
                    loadItem.setText(Messages.getString("ModelTreeDialog.LoadMdl"));
                }
                {
                    MenuItem saveItem = new MenuItem(menu, SWT.PUSH);
                    saveItem.setText(Messages.getString("ModelTreeDialog.SaveMdl"));
                    saveItem.setEnabled(false);
                }
            }
            final FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
            dialogShell.setLayout(dialogShellLayout);
            {
                Composite mainComposite = new Composite(dialogShell, SWT.NONE);
                final GridLayout mainCompositeLayout = new GridLayout();
                mainCompositeLayout.numColumns = 3;
                mainComposite.setLayout(mainCompositeLayout);
                {
                    final GridData menuTreeLData = new GridData();
                    menuTreeLData.grabExcessVerticalSpace = true;
                    menuTreeLData.widthHint = 150;
                    menuTreeLData.verticalAlignment = GridData.FILL;
                    Tree menuTree = new Tree(mainComposite, SWT.BORDER);
                    menuTree.setLayoutData(menuTreeLData);
                    menuTree.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(final SelectionEvent evt) {
                            System.out.println(evt);
                            if (edit != null) edit.dispose();
                            switch (Config.fromValue(((TreeItem) evt.item).getText())) {
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
                        TreeItem treeItem1 = new TreeItem(menuTree, SWT.NONE);
                        treeItem1.setText(Config.BASICS.value);
                    }
                    {
                        TreeItem treeItem2 = new TreeItem(menuTree, SWT.NONE);
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
            while (!dialogShell.isDisposed())
                if (!display.readAndDispatch()) display.sleep();
        } catch (final Exception e) {
            if (Util.INSTANCE.getDEBUG()) e.printStackTrace();
        }
    }
}
