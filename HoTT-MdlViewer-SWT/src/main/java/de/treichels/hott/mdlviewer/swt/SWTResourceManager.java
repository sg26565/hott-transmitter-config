package de.treichels.hott.mdlviewer.swt;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import java.util.HashMap;
import java.util.Vector;

/**
 * Class to manage SWT resources (Font, Color, Image and Cursor) There are no restrictions on the use of this pcmd.
 */
class SWTResourceManager {
    private static final HashMap<String, Object> resources = new HashMap<>();
    private static final Vector<Widget> users = new Vector<>();

    private static final DisposeListener disposeListener = e -> {
        SWTResourceManager.users.remove(e.getSource());
        if (SWTResourceManager.users.size() == 0) dispose();
    };

    private static void dispose() {
        for (String s : SWTResourceManager.resources.keySet()) {
            final Object resource = SWTResourceManager.resources.get(s);
            if (resource instanceof Font)
                ((Font) resource).dispose();
            else if (resource instanceof Color)
                ((Color) resource).dispose();
            else if (resource instanceof Image)
                ((Image) resource).dispose();
            else if (resource instanceof Cursor) ((Cursor) resource).dispose();
        }
        SWTResourceManager.resources.clear();
    }

    public static Color getColor(final int red, final int green, final int blue) {
        final String name = "COLOR:" + red + "," + green + "," + blue;
        if (SWTResourceManager.resources.containsKey(name)) return (Color) SWTResourceManager.resources.get(name);
        final Color color = new Color(Display.getDefault(), red, green, blue);
        SWTResourceManager.resources.put(name, color);
        return color;
    }

    @SuppressWarnings("rawtypes")
    static Font getFont(final int size) {
        final String fontName = SwtMdlBrowser.WIDGET_FONT_NAME + "|" + size + "|" + org.eclipse.swt.SWT.NORMAL + "|" + false + "|" + false;
        if (SWTResourceManager.resources.containsKey(fontName)) return (Font) SWTResourceManager.resources.get(fontName);
        final FontData fd = new FontData(SwtMdlBrowser.WIDGET_FONT_NAME, size, org.eclipse.swt.SWT.NORMAL);
        final Font font = new Font(Display.getDefault(), fd);
        SWTResourceManager.resources.put(fontName, font);
        return font;
    }

    /**
     * This method should be called by *all* Widgets which use resources provided by this SWTResourceManager. When widgets are disposed, they are removed from
     * the "users" Vector, and when no more registered Widgets are left, all resources are disposed.
     * <P>
     * If this method is not called for all Widgets then it should not be called at all, and the "dispose" method should be explicitly called after all
     * resources are no longer being used.
     */
    public static void registerResourceUser(final Widget widget) {
        if (SWTResourceManager.users.contains(widget)) return;
        SWTResourceManager.users.add(widget);
        widget.addDisposeListener(SWTResourceManager.disposeListener);
    }

}
