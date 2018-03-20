/*
  HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel

  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
  Foundation, either version 3 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
  A PARTICULAR PURPOSE. See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.mdlviewer.swt;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class MdlTabItem extends CTabItem {
	public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");
	public static final boolean IS_LINUX = System.getProperty("os.name").toLowerCase().startsWith("linux");
	public static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().startsWith("mac");
	public static final int WIDGET_FONT_SIZE;
	public static final String WIDGET_FONT_NAME;

	public MdlTabItem(CTabFolder parent, int style) {
		this(parent, style, 0);
	}

	public MdlTabItem(CTabFolder parent, int style, int index) {
		super(parent, style, index);
		this.setFont(new Font(Display.getDefault(), new FontData(WIDGET_FONT_NAME, WIDGET_FONT_SIZE + 1, 0)));
		this.setText("MDL Viewer");

		try {
			if (System.getProperty("program.version") == null) {
				Launcher.initSystemProperties();
			}
		} catch (Exception arg4) {
				arg4.printStackTrace();
		}

		this.setControl(new MdlTabItemComposite(parent));
	}

	static {
		WIDGET_FONT_SIZE = IS_MAC ? 12 : (IS_LINUX ? 8 : 9) * 96 / Display.getDefault().getDPI().y;
		WIDGET_FONT_NAME = IS_WINDOWS ? "Microsoft Sans Serif" : "Sans Serif";
	}
}