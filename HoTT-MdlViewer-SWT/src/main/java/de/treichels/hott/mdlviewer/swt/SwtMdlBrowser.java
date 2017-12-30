package de.treichels.hott.mdlviewer.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.treichels.hott.util.Util;

public class SwtMdlBrowser extends Composite {
    public static final boolean IS_WINDOWS = System.getProperty("os.portName").toLowerCase().startsWith("windows"); //$NON-NLS-1$ //$NON-NLS-2$

    public static final boolean IS_LINUX = System.getProperty("os.portName").toLowerCase().startsWith("linux"); //$NON-NLS-1$ //$NON-NLS-2$
    public static final boolean IS_MAC = System.getProperty("os.portName").toLowerCase().startsWith("mac"); //$NON-NLS-1$ //$NON-NLS-2$
    public final static int WIDGET_FONT_SIZE = MdlTabItem.IS_MAC ? 12 : (MdlTabItem.IS_LINUX ? 8 : 9) * 96 / Display.getDefault().getDPI().y;

    public final static String WIDGET_FONT_NAME = MdlTabItem.IS_WINDOWS ? "Microsoft Sans Serif" : "Sans Serif"; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * simple UI Window without menu
     */
    public static void main(final String[] args) {
        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        final SwtMdlBrowser inst = new SwtMdlBrowser(shell, SWT.NULL);
        final Point size = inst.getSize();
        shell.setLayout(new FillLayout());
        shell.setText("Graupner/SJ - MDL Browser"); //$NON-NLS-1$
        shell.layout();
        if (size.x == 0 && size.y == 0) {
            inst.pack();
            shell.pack();
        } else {
            final Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
            shell.setSize(shellBounds.width, shellBounds.height);
        }
        shell.open();
        while (!shell.isDisposed())
            if (!display.readAndDispatch()) display.sleep();
    }

    {
        // Register as a resource user - SWTResourceManager will
        // handle the obtaining and disposing of resources
        SWTResourceManager.registerResourceUser(this);
    }

    public SwtMdlBrowser(final Composite parent, final int style) {
        super(parent, style);
        initGUI();
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        try {
            this.setSize(650, 480);
            setBackground(SWTResourceManager.getColor(192, 192, 192));
            setFont(SWTResourceManager.getFont(SwtMdlBrowser.WIDGET_FONT_NAME, SwtMdlBrowser.WIDGET_FONT_SIZE + 2, SWT.NORMAL));
            final FillLayout thisLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
            setLayout(thisLayout);
            {
                new MdlTabItemComposite(this);
            }
            this.layout();
        } catch (final Exception e) {
            if (Util.INSTANCE.getDEBUG()) e.printStackTrace();
        }
    }
}
