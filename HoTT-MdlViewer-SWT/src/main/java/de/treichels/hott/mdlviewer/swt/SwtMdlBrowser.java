package de.treichels.hott.mdlviewer.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.treichels.hott.util.Util;

class SwtMdlBrowser extends Composite {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");
    private static final boolean IS_LINUX = System.getProperty("os.name").toLowerCase().startsWith("linux");
    private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().startsWith("mac");

    final static int WIDGET_FONT_SIZE = IS_MAC ? 12 : (IS_LINUX ? 8 : 9) * 96 / Display.getDefault().getDPI().y;
    final static String WIDGET_FONT_NAME = IS_WINDOWS ? "Microsoft Sans Serif" : "Sans Serif";

    /**
     * simple UI Window without menu
     */
    public static void main(final String[] args) {
        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        final SwtMdlBrowser inst = new SwtMdlBrowser(shell);
        final Point size = inst.getSize();
        shell.setLayout(new FillLayout());
        shell.setText("Graupner/SJ - MDL Browser");
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

    private SwtMdlBrowser(final Composite parent) {
        super(parent, SWT.NULL);
        initGUI();
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        try {
            this.setSize(650, 480);
            setBackground(SWTResourceManager.getColor(192, 192, 192));
            setFont(SWTResourceManager.getFont(SwtMdlBrowser.WIDGET_FONT_SIZE + 2));
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
