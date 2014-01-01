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
package de.treichels.spring;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author oli
 * 
 */
public class TestComposite {

  /**
   * @param args
   */
  public static void main(final String[] args) {
    final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("TestComposite.xml");
    ctx.registerShutdownHook();

    final Shell shell = ctx.getBean(Shell.class);
    final Display display = ctx.getBean(Display.class);
    final Composite composite = ctx.getBean("topLevel", Composite.class);

    shell.layout();
    composite.pack();
    shell.pack();

    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }

    ctx.close();
  }
}
