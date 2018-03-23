package de.treichels.hott.mdlviewer.swt;

import de.treichels.hott.messages.Messages;
import de.treichels.hott.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.Constructor;

class ModelTabbedDialog extends org.eclipse.swt.widgets.Dialog {
    // final static Logger log =
    // Logger.getLogger(ModelTabbedDialog.class.getName());

    /**
     * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog inside a new Shell.
     */
    public static void main(final String[] args) {
        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        final ModelTabbedDialog inst = new ModelTabbedDialog(shell);
        inst.open();
    }

    private CTabFolder mainTabFolder;

    private ModelTabbedDialog(final Shell parent) {
        super(parent, SWT.NULL);
    }

    /**
     * This function allows to register a device specific CTabItem to the main application tab folder to display device specific curve calculated from point
     * combinations or other specific dialog As default the function should return null which stands for no device custom tab item.
     */
    private void getUtilityGraphicsTabItem() {
        try {
            final String className = "MdlTabItem";
            // log.log(Level.FINE, "loading Class " + className);
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            final Class<?> c = loader.loadClass(className);
            final Constructor<?> constructor = c.getDeclaredConstructor(CTabFolder.class, int.class);
            // log.log(java.util.logging.Level.FINE, "constructor != null -> " +
            // (constructor != null ? "true" : "false"));
            //
            if (constructor != null) constructor.newInstance(mainTabFolder, SWT.NONE);
        } catch (final Throwable t) {
            if (Util.INSTANCE.getDEBUG()) t.printStackTrace();
        }
    }

    /**
     * query if the utility graphics tabulator should be displayed and updated
     *
     * @return the value of the property, if property does not exist return false (default behavior of Boolean)
     */
    private boolean isUtilityGraphicsTabRequested() {
        boolean rc = true;
        try {
            final String className = "HoTTDecoder";
            // log.log(Level.FINE, "loading Class " + className);
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            final Class<?> c = loader.loadClass(className);
            final Constructor<?> constructor = c.getDeclaredConstructor();
            // log.log(java.util.logging.Level.FINE, "constructor != null -> " +
            // (constructor != null ? "true" : "false"));
            //
            if (constructor != null)
                constructor.newInstance();
            else
                rc = false;
        } catch (final Throwable t) {
            if (Util.INSTANCE.getDEBUG()) t.printStackTrace();
            rc = false;
        }
        return rc;
    }

    private void open() {
        try {
            Launcher.Companion.getInstance();
            final Shell parent = getParent();
            Shell dialogShell = new Shell(parent, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
            final FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
            dialogShell.setLayout(dialogShellLayout);
            dialogShell.setText(Messages.getString("ModelTabbedDialog.Title"));
            {
                Composite mainComposite = new Composite(dialogShell, SWT.NONE);
                final FillLayout composite1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
                mainComposite.setLayout(composite1Layout);
                mainComposite.setBounds(0, 0, 500, 300);
                {
                    mainTabFolder = new CTabFolder(mainComposite, SWT.NONE);
                    {
                        if (isUtilityGraphicsTabRequested()) getUtilityGraphicsTabItem();
                        // new MdlTabItem(mainTabFolder, SWT.NONE);
                    }
                    // {
                    // new BaseConfiguration(mainTabFolder, mdlViewTabItem);
                    // }
                    mainTabFolder.setSelection(0);
                }
                mainComposite.layout();
            }
            dialogShell.setLocation(getParent().toDisplay(100, 100));
            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setSize(768, 768);
            dialogShell.open();
            final Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed())
                if (!display.readAndDispatch()) display.sleep();
        } catch (final Exception e) {
            if (Util.INSTANCE.getDEBUG()) e.printStackTrace();
        }
    }

}
