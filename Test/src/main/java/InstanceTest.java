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

/**
 * @author oli
 * 
 */
public class InstanceTest {
  public static void main(final String[] args) {
    final Foo o1 = new Foo();
    o1.foo = "foo";

    final Bar o2 = new Bar();
    o2.foo = "foo2";
    o2.bar = "bar";

    System.out.println("o1 instanceof Foo: " + Foo.class.isInstance(o1));
    System.out.println("o2 instanceof Foo: " + Foo.class.isInstance(o2));
    System.out.println("o1 instanceof Bar: " + Bar.class.isInstance(o1));
    System.out.println("o2 instanceof Bar: " + Bar.class.isInstance(o2));

    System.out.println("Foo assignable from Foo: " + Foo.class.isAssignableFrom(Foo.class));
    System.out.println("Foo assignable from Bar: " + Foo.class.isAssignableFrom(Bar.class));
    System.out.println("Bar assignable from Foo: " + Bar.class.isAssignableFrom(Foo.class));
    System.out.println("Bar assignable from Bar: " + Bar.class.isAssignableFrom(Bar.class));

  }
}
