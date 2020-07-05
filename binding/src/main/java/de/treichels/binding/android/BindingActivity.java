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
package de.treichels.binding.android;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.madrobot.beans.BeanInfo;
import com.madrobot.beans.IntrospectionException;
import com.madrobot.beans.Introspector;
import com.madrobot.beans.PropertyDescriptor;
import de.treichels.binding.BindingManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class BindingActivity extends Activity {
  public static final String NAMESPACE_BEANS = "http://android.treichels.de/beans";

  /**
   * Scan the activity definition xml for binding tags and resolve them via
   * {@link BindingManager}.
   */
  @Override
  public View onCreateView(final String name, final Context context, final AttributeSet attributes) {
    final Map<String, String> attributeMap = new HashMap<String, String>();

    // find all attributes with a binding namespace
    final int attributeCount = attributes.getAttributeCount();
    for (int i = 0; i < attributeCount; i++) {
      final String attributeNameName = attributes.getAttributeName(i);
      final String attributeValue = attributes.getAttributeValue(NAMESPACE_BEANS, attributeNameName);

      if (attributeValue != null) {
        attributeMap.put(attributeNameName, attributeValue);
      }
    }

    if (attributeMap.isEmpty()) {
      // no bindings
      return null;
    }

    // guess package of view class
    final String prefix;
    if (name.contains(".")) {
      // name is already fully qualified
      prefix = "";
    } else if (name.equals("View") || name.equals("ViewGroup")) {
      prefix = "android.view.";
    } else {
      prefix = "android.widget.";
    }

    View view = null;

    try {
      // create view via our inflater
      view = getLayoutInflater().createView(name, prefix, attributes);

      // get bean info for the view
      final BeanInfo beanInfo = Introspector.getBeanInfo(view.getClass(), View.class);

      // loop through all properties
      for (final PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {

        // check if we have a binding for that property
        if (attributeMap.containsKey(property.getName())) {
          final Method writeMethod = property.getWriteMethod();

          // check if property is writable
          if (writeMethod != null) {
            final String propertyName = property.getName();
            final String binding = attributeMap.get(propertyName);
            final Object propertyValue = BindingManager.getBindingManager().getValueFromBinding(binding);
            final Class<?> propertyType = property.getPropertyType();

            if (propertyType.isInstance(propertyValue)) {
              // direct assign
              writeMethod.invoke(view, propertyValue);
            } else if (propertyType.isAssignableFrom(String.class)) {
              // convert to string
              writeMethod.invoke(view, propertyValue.toString());
            } else {
              throw new ClassCastException("cannot convert " + propertyValue.getClass() + " to " + propertyType);
            }
          }
        }
      }
    } catch (final IntrospectionException e) {
      throw new RuntimeException(e);
    } catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (final IllegalArgumentException e) {
      throw new RuntimeException(e);
    } catch (final InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (final ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    return view;
  }
}
