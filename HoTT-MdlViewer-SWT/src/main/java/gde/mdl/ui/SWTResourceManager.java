package gde.mdl.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import gde.mdl.messages.Messages;

/**
 * Class to manage SWT resources (Font, Color, Image and Cursor) There are no
 * restrictions on the use of this code.
 */
public class SWTResourceManager {

	private static HashMap<String, Object> resources = new HashMap<>();
	private static Vector<Widget> users = new Vector<>();
	private static SWTResourceManager instance = new SWTResourceManager();

	private static DisposeListener disposeListener = e -> {
		SWTResourceManager.users.remove(e.getSource());
		if (SWTResourceManager.users.size() == 0) {
			dispose();
		}
	};

	public static void dispose() {
		final Iterator<String> it = SWTResourceManager.resources.keySet().iterator();
		while (it.hasNext()) {
			final Object resource = SWTResourceManager.resources.get(it.next());
			if (resource instanceof Font) {
				((Font) resource).dispose();
			} else if (resource instanceof Color) {
				((Color) resource).dispose();
			} else if (resource instanceof Image) {
				((Image) resource).dispose();
			} else if (resource instanceof Cursor) {
				((Cursor) resource).dispose();
			}
		}
		SWTResourceManager.resources.clear();
	}

	public static Color getColor(final int red, final int green, final int blue) {
		final String name = "COLOR:" + red + "," + green + "," + blue; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (SWTResourceManager.resources.containsKey(name)) {
			return (Color) SWTResourceManager.resources.get(name);
		}
		final Color color = new Color(Display.getDefault(), red, green, blue);
		SWTResourceManager.resources.put(name, color);
		return color;
	}

	public static Cursor getCursor(final int type) {
		final String name = "CURSOR:" + type; //$NON-NLS-1$
		if (SWTResourceManager.resources.containsKey(name)) {
			return (Cursor) SWTResourceManager.resources.get(name);
		}
		final Cursor cursor = new Cursor(Display.getDefault(), type);
		SWTResourceManager.resources.put(name, cursor);
		return cursor;
	}

	public static Font getFont(final String name, final int size, final int style) {
		return getFont(name, size, style, false, false);
	}

	@SuppressWarnings("rawtypes")
	public static Font getFont(final String name, final int size, final int style, final boolean strikeout, final boolean underline) {
		final String fontName = name + "|" + size + "|" + style + "|" + strikeout + "|" + underline; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (SWTResourceManager.resources.containsKey(fontName)) {
			return (Font) SWTResourceManager.resources.get(fontName);
		}
		final FontData fd = new FontData(name, size, style);
		if (strikeout || underline) {
			try {
				final Class lfCls = Class.forName("org.eclipse.swt.internal.win32.LOGFONT"); //$NON-NLS-1$
				final Object lf = FontData.class.getField("data").get(fd); //$NON-NLS-1$
				if (lf != null && lfCls != null) {
					if (strikeout) {
						lfCls.getField("lfStrikeOut").set(lf, new Byte((byte) 1)); //$NON-NLS-1$
					}
					if (underline) {
						lfCls.getField("lfUnderline").set(lf, new Byte((byte) 1)); //$NON-NLS-1$
					}
				}
			} catch (final Throwable e) {
				System.err.println(Messages.getString("SWTResourceManager.ErrorNoUnderlineStrikeout", e)); //$NON-NLS-1$
			}
		}
		final Font font = new Font(Display.getDefault(), fd);
		SWTResourceManager.resources.put(fontName, font);
		return font;
	}

	public static Image getImage(String url) {
		try {
			url = url.replace('\\', '/');
			if (url.startsWith("/")) //$NON-NLS-1$
			{
				url = url.substring(1);
			}
			if (SWTResourceManager.resources.containsKey(url)) {
				return (Image) SWTResourceManager.resources.get(url);
			}
			final Image img = new Image(Display.getDefault(), SWTResourceManager.instance.getClass().getClassLoader().getResourceAsStream(url));
			SWTResourceManager.resources.put(url, img);
			return img;
		} catch (final Exception e) {
			System.err.println(Messages.getString("SWTResourceManager.ErrorGetImage", url, e)); //$NON-NLS-1$
			return null;
		}
	}

	public static Image getImage(final String url, final Control widget) {
		final Image img = getImage(url);
		if (img != null && widget != null) {
			img.setBackground(widget.getBackground());
		}
		return img;
	}

	/**
	 * This method should be called by *all* Widgets which use resources
	 * provided by this SWTResourceManager. When widgets are disposed, they are
	 * removed from the "users" Vector, and when no more registered Widgets are
	 * left, all resources are disposed.
	 * <P>
	 * If this method is not called for all Widgets then it should not be called
	 * at all, and the "dispose" method should be explicitly called after all
	 * resources are no longer being used.
	 */
	public static void registerResourceUser(final Widget widget) {
		if (SWTResourceManager.users.contains(widget)) {
			return;
		}
		SWTResourceManager.users.add(widget);
		widget.addDisposeListener(SWTResourceManager.disposeListener);
	}

}
