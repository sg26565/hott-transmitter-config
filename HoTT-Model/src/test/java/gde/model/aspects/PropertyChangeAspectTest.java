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
package gde.model.aspects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gde.model.BaseModel;
import gde.model.Clock;
import gde.model.LogicalSwitch;
import gde.model.enums.Vendor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author oli
 * 
 */
public class PropertyChangeAspectTest {
  private class TestListener implements PropertyChangeListener {
    @Override
    public void propertyChange(final PropertyChangeEvent event) {
      events.add(event);
    }
  }

  private static final String       MEMORY_VERSION = "memoryVersion";
  private static final String       VENDOR         = "vendor";

  private BaseModel                 model;
  private List<PropertyChangeEvent> events;
  private PropertyChangeListener    listener;

  @Before
  public void setUp() {
    listener = new TestListener();
    model = new BaseModel();
    events = new ArrayList<PropertyChangeEvent>();
  }

  @Test
  public void testDerived1() {
    final LogicalSwitch sw = new LogicalSwitch();
    sw.addPropertyChangeListener(listener);

    // set a base property
    assertTrue(events.isEmpty());
    sw.setNumber(5);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(sw, event.getSource());
    assertEquals("number", event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(5, event.getNewValue());
  }

  @Test
  public void testDerived2() {
    final LogicalSwitch sw = new LogicalSwitch();
    sw.addPropertyChangeListener(listener);

    // set a derived property
    assertTrue(events.isEmpty());
    assertFalse(sw.isEnabled());
    sw.setEnabled(true);
    assertTrue(sw.isEnabled());
    assertEquals(1, events.size());
    PropertyChangeEvent event = events.get(0);
    assertEquals(sw, event.getSource());
    assertEquals("enabled", event.getPropertyName());
    assertEquals(Boolean.FALSE, event.getOldValue());
    assertEquals(Boolean.TRUE, event.getNewValue());

    // set a derived property
    events.clear();
    assertTrue(events.isEmpty());
    assertTrue(sw.isEnabled());
    sw.setEnabled(false);
    assertFalse(sw.isEnabled());
    assertEquals(1, events.size());
    event = events.get(0);
    assertEquals(sw, event.getSource());
    assertEquals("enabled", event.getPropertyName());
    assertEquals(Boolean.TRUE, event.getOldValue());
    assertEquals(Boolean.FALSE, event.getNewValue());

    // set a derived property
    events.clear();
    assertTrue(events.isEmpty());
    assertFalse(sw.isEnabled());
    sw.setEnabled(false);
    assertFalse(sw.isEnabled());
    assertTrue(events.isEmpty());
  }

  @Test
  public void testHasListenersOnClass1() {
    assertFalse(model.hasListeners(VENDOR));
    model.addPropertyChangeListener(listener);
    assertTrue(model.hasListeners(VENDOR));
  }

  @Test
  public void testHasListenersOnClass2() {
    assertFalse(model.hasListeners(MEMORY_VERSION));
    model.addPropertyChangeListener(listener);
    assertTrue(model.hasListeners(MEMORY_VERSION));
  }

  @Test
  public void testHasListenersOnClass3() {
    assertFalse(model.hasListeners("foo"));
    model.addPropertyChangeListener(listener);
    assertTrue(model.hasListeners("foo"));
  }

  @Test
  public void testHasListenersOnVendorProperty1() {
    assertFalse(model.hasListeners(VENDOR));
    model.addPropertyChangeListener(VENDOR, listener);
    assertTrue(model.hasListeners(VENDOR));
  }

  @Test
  public void testHasListenersOnVendorProperty2() {
    assertFalse(model.hasListeners(MEMORY_VERSION));
    model.addPropertyChangeListener(VENDOR, listener);
    assertFalse(model.hasListeners(MEMORY_VERSION));
  }

  @Test
  public void testListenerOnClass1() {
    // register listener for all properties
    model.addPropertyChangeListener(listener);

    // check that event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals(Vendor.Graupner, event.getNewValue());
  }

  @Test
  public void testListenerOnClass2() {
    // register listener for all properties
    model.addPropertyChangeListener(listener);

    // check that event is fired on change of memoryVersion
    assertTrue(events.isEmpty());
    model.setMemoryVersion(1000);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(MEMORY_VERSION, event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(1000, event.getNewValue());
  }

  @Test
  public void testListenerOnVendorProperty1() {
    // register listener for vendor property only
    model.addPropertyChangeListener(VENDOR, listener);

    // check that event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals(Vendor.Graupner, event.getNewValue());
  }

  @Test
  public void testListenerOnVendorProperty2() {
    // register listener for vendor property only
    model.addPropertyChangeListener(VENDOR, listener);

    // check that no event is fired on change of memoryVersion
    assertTrue(events.isEmpty());
    model.setMemoryVersion(1000);
    assertTrue(events.isEmpty());
  }

  @Test
  public void testNestedCall1() {
    final Clock clock = new Clock();
    clock.addPropertyChangeListener(listener);

    assertEquals(0, clock.getTimer());
    assertEquals(0, clock.getTimerMinutes());
    assertEquals(0, clock.getTimerSeconds());

    assertTrue(events.isEmpty());
    clock.setTimer(66);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(clock, event.getSource());
    assertEquals("timer", event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(66, event.getNewValue());

    assertEquals(66, clock.getTimer());
    assertEquals(1, clock.getTimerMinutes());
    assertEquals(6, clock.getTimerSeconds());
  }

  @Test
  public void testNestedCall2() {
    final Clock clock = new Clock();
    clock.addPropertyChangeListener(listener);

    assertEquals(0, clock.getTimer());
    assertEquals(0, clock.getTimerMinutes());
    assertEquals(0, clock.getTimerSeconds());

    assertTrue(events.isEmpty());
    clock.setTimerMinutes(1);
    assertEquals(2, events.size());

    PropertyChangeEvent event = events.get(0);
    assertEquals(clock, event.getSource());
    assertEquals("timer", event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(60, event.getNewValue());

    event = events.get(1);
    assertEquals(clock, event.getSource());
    assertEquals("timerMinutes", event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(1, event.getNewValue());

    assertEquals(60, clock.getTimer());
    assertEquals(1, clock.getTimerMinutes());
    assertEquals(0, clock.getTimerSeconds());
  }

  @Test
  public void testNestedCall3() {
    final Clock clock = new Clock();
    clock.addPropertyChangeListener(listener);

    assertEquals(0, clock.getTimer());
    assertEquals(0, clock.getTimerMinutes());
    assertEquals(0, clock.getTimerSeconds());

    assertTrue(events.isEmpty());
    clock.setTimerSeconds(30);
    assertEquals(2, events.size());

    PropertyChangeEvent event = events.get(0);
    assertEquals(clock, event.getSource());
    assertEquals("timer", event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(30, event.getNewValue());

    event = events.get(1);
    assertEquals(clock, event.getSource());
    assertEquals("timerSeconds", event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(30, event.getNewValue());

    assertEquals(30, clock.getTimer());
    assertEquals(0, clock.getTimerMinutes());
    assertEquals(30, clock.getTimerSeconds());
  }

  @Test
  public void testNoChange() {
    // register listener for all properties
    model.addPropertyChangeListener(listener);

    // check that event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertEquals(1, events.size());
    PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals(Vendor.Graupner, event.getNewValue());

    // no change - no event
    events.clear();
    model.setVendor(Vendor.Graupner);
    assertTrue(events.isEmpty());

    // check that event is fired on change of vendor
    model.setVendor(null);
    assertEquals(1, events.size());
    event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertEquals(Vendor.Graupner, event.getOldValue());
    assertNull(event.getNewValue());
  }

  @Test
  public void testNoListener1() {
    // don't register any listener
    // check that no event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertTrue(events.isEmpty());
  }

  @Test
  public void testNoListener2() {
    // don't register any listener
    // check that no event is fired on change of memoryVersion
    assertTrue(events.isEmpty());
    model.setMemoryVersion(1000);
    assertTrue(events.isEmpty());
  }

  @Test
  public void testNullValues() {
    // register listener for all properties
    model.addPropertyChangeListener(listener);

    // events are always fired on null values
    assertTrue(events.isEmpty());
    model.setVendor(null);
    PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertNull(event.getOldValue());
    assertNull(event.getNewValue());

    // check that event is fired on change of vendor
    events.clear();
    model.setVendor(Vendor.Graupner);
    assertEquals(1, events.size());
    event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals(Vendor.Graupner, event.getNewValue());

    // check that event is fired on change of vendor
    events.clear();
    model.setVendor(null);
    assertEquals(1, events.size());
    event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertEquals(Vendor.Graupner, event.getOldValue());
    assertNull(event.getNewValue());
  }

  @Test
  public void testRemoveListenerOnClass1() {
    // register listener for all properties
    model.addPropertyChangeListener(listener);

    // check that event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals(Vendor.Graupner, event.getNewValue());

    // unregister listener
    model.removePropertyChangeListener(listener);
    events.clear();
    model.setVendor(null);
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertTrue(events.isEmpty());
  }

  @Test
  public void testRemoveListenerOnClass2() {
    // register listener for all properties
    model.addPropertyChangeListener(listener);

    // check that event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setMemoryVersion(1000);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(MEMORY_VERSION, event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(1000, event.getNewValue());

    // unregister listener
    model.removePropertyChangeListener(listener);
    events.clear();
    model.setMemoryVersion(2000);
    assertTrue(events.isEmpty());
  }

  @Test
  public void testRemoveListenerOnVendorProperty1() {
    // register listener for vendor property only
    model.addPropertyChangeListener(VENDOR, listener);

    // check that event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(VENDOR, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals(Vendor.Graupner, event.getNewValue());

    // unregister listener
    model.removePropertyChangeListener(VENDOR, listener);
    events.clear();
    model.setVendor(null);
    assertTrue(events.isEmpty());
    model.setVendor(Vendor.Graupner);
    assertTrue(events.isEmpty());
  }

  @Test
  public void testRemoveListenerOnVendorProperty2() {
    // register listener for memoryVersion property only
    model.addPropertyChangeListener(MEMORY_VERSION, listener);

    // check that event is fired on change of vendor
    assertTrue(events.isEmpty());
    model.setMemoryVersion(1000);
    assertEquals(1, events.size());
    final PropertyChangeEvent event = events.get(0);
    assertEquals(model, event.getSource());
    assertEquals(MEMORY_VERSION, event.getPropertyName());
    assertEquals(0, event.getOldValue());
    assertEquals(1000, event.getNewValue());

    // unregister listener
    model.removePropertyChangeListener(MEMORY_VERSION, listener);
    events.clear();
    model.setMemoryVersion(2000);
    assertTrue(events.isEmpty());
  }
}
