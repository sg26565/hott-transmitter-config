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
package de.treichels.binding;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.madrobot.beans.IntrospectionException;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 * 
 */
public class BindingManagerTest {
  private BindingManager bindingManager;

  @Before
  public void setUp() throws Exception {
    bindingManager = new BindingManager();
  }

  @Test
  public void testAddBinding() {
    bindingManager.addBindig("foo", "bar");

    final String bar = (String) bindingManager.getBindig("foo");
    assertEquals("bar", bar);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateAddBinding() {
    bindingManager.addBindig("foo", "bar");

    bindingManager.addBindig("foo", "baz");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyGetBinding() {
    bindingManager.getBindig("foo");
  }

  @Test
  public void testgetNestedValue() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
    final Foo foo = new Foo();

    foo.setStringValue("bar");
    foo.setBooleanValue(false);
    foo.setDoubleValue(3.14d);
    foo.setFloatValue(2.5f);
    foo.setIntegerValue(5);
    foo.setObjectValue(foo);

    bindingManager.addBindig("foo", foo);

    assertEquals("bar", bindingManager.getValueFromBinding("foo.objectValue.stringValue"));
    assertEquals(false, bindingManager.getValueFromBinding("foo.objectValue.booleanValue"));
    assertEquals(3.14d, bindingManager.getValueFromBinding("foo.objectValue.doubleValue"));
    assertEquals(2.5f, bindingManager.getValueFromBinding("foo.objectValue.floatValue"));
    assertEquals(5, bindingManager.getValueFromBinding("foo.objectValue.integerValue"));
    assertEquals(foo, bindingManager.getValueFromBinding("foo.objectValue.objectValue"));
  }

  @Test
  public void testgetValue() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
    final Foo foo = new Foo();

    foo.setStringValue("bar");
    foo.setBooleanValue(false);
    foo.setDoubleValue(3.14d);
    foo.setFloatValue(2.5f);
    foo.setIntegerValue(5);
    foo.setObjectValue(foo);

    bindingManager.addBindig("foo", foo);

    assertEquals("bar", bindingManager.getValueFromBinding("foo.stringValue"));
    assertEquals(false, bindingManager.getValueFromBinding("foo.booleanValue"));
    assertEquals(3.14d, bindingManager.getValueFromBinding("foo.doubleValue"));
    assertEquals(2.5f, bindingManager.getValueFromBinding("foo.floatValue"));
    assertEquals(5, bindingManager.getValueFromBinding("foo.integerValue"));
    assertEquals(foo, bindingManager.getValueFromBinding("foo.objectValue"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveBinding() {
    bindingManager.addBindig("foo", "bar");

    final String bar = (String) bindingManager.getBindig("foo");
    assertEquals("bar", bar);

    bindingManager.removeBindig("foo");

    bindingManager.getBindig("foo");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnknownRemoveBinding() {
    bindingManager.removeBindig("foo");
  }
}
