package gde.mdl.ui;

import java.lang.reflect.Constructor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import gde.mdl.messages.Messages;

public class ModelTabbedDialog extends org.eclipse.swt.widgets.Dialog {
	// final static Logger log =
	// Logger.getLogger(ModelTabbedDialog.class.getName());

	/**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
	 * inside a new Shell.
	 */
	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		final ModelTabbedDialog inst = new ModelTabbedDialog(shell, SWT.NULL);
		inst.open();
	}

	private Shell dialogShell;
	private CTabFolder mainTabFolder;

	private Composite mainComposite;

	public ModelTabbedDialog(final Shell parent, final int style) {
		super(parent, style);
	}

	/**
	 * This function allows to register a device specific CTabItem to the main
	 * application tab folder to display device specific curve calculated from
	 * point combinations or other specific dialog As default the function
	 * should return null which stands for no device custom tab item.
	 */
	public CTabItem getUtilityGraphicsTabItem() {
		Object inst = null;
		try {
			final String className = "gde.mdl.ui.MdlTabItem";//$NON-NLS-1$
			// log.log(Level.FINE, "loading Class " + className); //$NON-NLS-1$
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			final Class<?> c = loader.loadClass(className);
			final Constructor<?> constructor = c.getDeclaredConstructor(new Class[] { CTabFolder.class, int.class });
			// log.log(java.util.logging.Level.FINE, "constructor != null -> " +
			// (constructor != null ? "true" : "false")); //$NON-NLS-1$
			// //$NON-NLS-2$ //$NON-NLS-3$
			if (constructor != null) {
				inst = constructor.newInstance(new Object[] { mainTabFolder, SWT.NONE });
			}
		} catch (final Throwable t) {
			t.printStackTrace();
		}
		return (CTabItem) inst;
	}

	/**
	 * query if the utility graphics tabulator should be displayed and updated
	 * 
	 * @return the value of the property, if property does not exist return
	 *         false (default behavior of Boolean)
	 */
	public boolean isUtilityGraphicsTabRequested() {
		boolean rc = true;
		try {
			final String className = "de.treichels.hott.HoTTDecoder";//$NON-NLS-1$
			// log.log(Level.FINE, "loading Class " + className); //$NON-NLS-1$
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			final Class<?> c = loader.loadClass(className);
			final Constructor<?> constructor = c.getDeclaredConstructor();
			// log.log(java.util.logging.Level.FINE, "constructor != null -> " +
			// (constructor != null ? "true" : "false")); //$NON-NLS-1$
			// //$NON-NLS-2$ //$NON-NLS-3$
			if (constructor != null) {
				constructor.newInstance();
			} else {
				rc = false;
			}
		} catch (final Throwable t) {
			t.printStackTrace();
			rc = false;
		}
		return rc;
	}

	public void open() {
		try {
			Launcher.initSystemProperties();
			final Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
			final FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			dialogShell.setLayout(dialogShellLayout);
			dialogShell.setText(Messages.getString("ModelTabbedDialog.Title")); //$NON-NLS-1$
			{
				mainComposite = new Composite(dialogShell, SWT.NONE);
				final FillLayout composite1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
				mainComposite.setLayout(composite1Layout);
				mainComposite.setBounds(0, 0, 500, 300);
				{
					mainTabFolder = new CTabFolder(mainComposite, SWT.NONE);
					{
						if (isUtilityGraphicsTabRequested()) {
							getUtilityGraphicsTabItem();
							// new MdlTabItem(mainTabFolder, SWT.NONE);
						}
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
