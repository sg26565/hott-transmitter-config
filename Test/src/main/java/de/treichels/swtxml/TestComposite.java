/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.swtxml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.swtxml.swt.byid.ById;
import com.swtxml.views.SwtXmlComposite;

/**
 * @author oli
 * 
 */
public class TestComposite extends SwtXmlComposite {
  public static void main(final String[] args) {
    final Display display = Display.getDefault();
    final Shell shell = new Shell(display);

    final TestComposite composite = new TestComposite(shell, SWT.NONE);
    shell.setLayout(new FillLayout());
    shell.setText("Graupner/SJ - MDL Browser");
    shell.layout();

    composite.pack();
    shell.pack();

    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  @ById
  private Label   versionLabel;

  @ById
  private Browser browser;

  public TestComposite(final Composite parent, final int style) {
    super(parent, style);
  }

  @SuppressWarnings("unused")
  private void onKeyPressed() {
    System.out.println("Key pressed");
  }

  @SuppressWarnings("unused")
  private void onLoadMDL() {
    System.out.println("Load MDL");
  }

  @SuppressWarnings("unused")
  private void onSaveMDL() {
    System.out.println("Save MDL");
  }

  @Override
  protected void setupView() {
    versionLabel.setText("v1.23");
  }
}
