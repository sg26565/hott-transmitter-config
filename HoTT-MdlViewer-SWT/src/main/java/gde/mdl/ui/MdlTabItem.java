/*************************************************************************************
  	This file is part of GNU DataExplorer.

    GNU DataExplorer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GNU DataExplorer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GNU DataExplorer.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright (c) 2013 Winfried Bruegmann
 **************************************************************************************/
package gde.mdl.ui;

import gde.report.html.HTMLReport;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * tab item for DataExplorer integration of MDL reading and display purpose this tab item is target to be included in DataExplorer
 */
public class MdlTabItem extends CTabItem {
  final static Logger         log              = Logger.getLogger(MdlTabItem.class.getName());

  public static final boolean IS_WINDOWS       = System.getProperty("os.name").toLowerCase().startsWith("windows");                            //$NON-NLS-1$ //$NON-NLS-2$
  public static final boolean IS_LINUX         = System.getProperty("os.name").toLowerCase().startsWith("linux");                              //$NON-NLS-1$ //$NON-NLS-2$
  public static final boolean IS_MAC           = System.getProperty("os.name").toLowerCase().startsWith("mac");                                //$NON-NLS-1$ //$NON-NLS-2$

  public final static int     WIDGET_FONT_SIZE = MdlTabItem.IS_MAC ? 12 : (MdlTabItem.IS_LINUX ? 8 : 9) * 96 / Display.getDefault().getDPI().y;
  public final static String  WIDGET_FONT_NAME = MdlTabItem.IS_WINDOWS ? "Microsoft Sans Serif" : "Sans Serif";                                //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * @param parent
   * @param style
   */
  public MdlTabItem(final CTabFolder parent, final int style) {
    this(parent, style, 0);
  }

  /**
   * @param parent
   * @param style
   * @param index
   */
  public MdlTabItem(final CTabFolder parent, final int style, final int index) {
    super(parent, style, index);
    setFont(new Font(Display.getDefault(), new FontData(MdlTabItem.WIDGET_FONT_NAME, MdlTabItem.WIDGET_FONT_SIZE + 1, SWT.NORMAL)));
    setText("MDL Viewer"); //$NON-NLS-1$
    HTMLReport.setSuppressExceptions(true);

    // check if environment needs to be initialized - loaded within DataExplorer
    try {
      if (System.getProperty(Launcher.PROGRAM_VERSION) == null) {
        Launcher.initSystemProperties();
        Launcher.initLogging();
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }

    setControl(new MdlTabItemComposite(parent));
  }

}
