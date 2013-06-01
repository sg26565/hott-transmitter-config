package gde.mdl.ui;

import gde.report.Report;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ModelTabbedDialog extends org.eclipse.swt.widgets.Dialog {
	final static Logger	log	= Logger.getLogger(ModelTabbedDialog.class.getName());

	private Shell				dialogShell;
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

			File source = new File(Report.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			// if application was packaged as individual class files, find the classes directory
			if (!source.getName().endsWith(".jar")) {
				while (!source.getName().equals("classes")) {
					source = source.getParentFile();
				}				
			}

			// get the parent directory containing the jar file or the classes directory
			source = source.getParentFile();

			// if we are running inside Eclipse in the target directory, step up to the project level
			if (source.getName().equals("target")) {
				source = source.getParentFile();
			}

			String path = source.getAbsolutePath();
			System.setProperty("log.dir", path);
			log.log(Level.OFF, "log.dir =  " + System.getProperty("log.dir")); //$NON-NLS-1$
			System.setProperty("mdl.dir", path);//$NON-NLS-1$
			log.log(Level.OFF, "mdl.dir =  " + System.getProperty("mdl.dir")); //$NON-NLS-1$
			System.setProperty("program.dir", System.getProperty("mdl.dir"));//$NON-NLS-1$
			log.log(Level.OFF, "program.dir =  " + System.getProperty("program.dir")); //$NON-NLS-1$
			System.setProperty("template.dir", "");//load from classpath //$NON-NLS-1$
			log.log(Level.OFF, "template.dir =  " + System.getProperty("template.dir")); //$NON-NLS-1$

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
			dialogShell = new Shell(parent, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
			FillLayout dialogShellLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			dialogShell.setLayout(dialogShellLayout);
			dialogShell.setText("Graupner/SJ HoTT MDL Explorer");
			{
				mainComposite = new Composite(dialogShell, SWT.NONE);
				FillLayout composite1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
				mainComposite.setLayout(composite1Layout);
				mainComposite.setBounds(0, 0, 500, 300);
				{
					mainTabFolder = new CTabFolder(mainComposite, SWT.NONE);
					{
						if (isUtilityGraphicsTabRequested()) getUtilityGraphicsTabItem();
						//new MdlTabItem(mainTabFolder, SWT.NONE);
					}
					//						{
					//							new BaseConfiguration(mainTabFolder, mdlViewTabItem);
					//						}
					mainTabFolder.setSelection(0);
				}
				mainComposite.layout();
			}
			dialogShell.setLocation(getParent().toDisplay(100, 100));
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setSize(768, 768);
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

	/**
	 * query if the utility graphics tabulator should be displayed and updated
	 * @return the value of the property, if property does not exist return false (default behavior of Boolean)
	 */
	public boolean isUtilityGraphicsTabRequested() {
		boolean rc = true;
		try {
			String className = "de.treichels.hott.HoTTDecoder";//$NON-NLS-1$
			//log.log(Level.FINE, "loading Class " + className); //$NON-NLS-1$
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<?> c = loader.loadClass(className);
			Constructor<?> constructor = c.getDeclaredConstructor();
			//log.log(java.util.logging.Level.FINE, "constructor != null -> " + (constructor != null ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (constructor != null) {
				constructor.newInstance();
			}
			else
				rc = false;
		}
		catch (final Throwable t) {
			t.printStackTrace();
			rc = false;
		}
		return rc;
	}

	/**
	 * This function allows to register a device specific CTabItem to the main application tab folder to display device 
	 * specific curve calculated from point combinations or other specific dialog
	 * As default the function should return null which stands for no device custom tab item.  
	 */
	public CTabItem getUtilityGraphicsTabItem() {
		Object inst = null;
		try {
			String className = "gde.mdl.ui.MdlTabItem";//$NON-NLS-1$
			//log.log(Level.FINE, "loading Class " + className); //$NON-NLS-1$
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<?> c = loader.loadClass(className);
			Constructor<?> constructor = c.getDeclaredConstructor(new Class[] { CTabFolder.class, int.class });
			//log.log(java.util.logging.Level.FINE, "constructor != null -> " + (constructor != null ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (constructor != null) {
				inst = constructor.newInstance(new Object[] { mainTabFolder, SWT.NONE });
			}
		}
		catch (final Throwable t) {
			t.printStackTrace();
		}
		return (CTabItem) inst;
	}

}
