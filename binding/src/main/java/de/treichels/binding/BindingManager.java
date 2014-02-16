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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.madrobot.beans.BeanInfo;
import com.madrobot.beans.IntrospectionException;
import com.madrobot.beans.Introspector;
import com.madrobot.beans.PropertyDescriptor;

/**
 * @author oli
 * 
 */
public class BindingManager {
  private static final BindingManager BINDING_MANAGER = new BindingManager();

  public static BindingManager getBindingManager() {
    return BINDING_MANAGER;
  }

  private final Map<String, Object> beans = new HashMap<String, Object>();

  public void addBindig(final String name, final Object binding) {
    if (beans.containsKey(name)) {
      throw new IllegalArgumentException("duplicate name: " + name);
    }
    beans.put(name, binding);
  }

  public Object getBindig(final String name) {
    if (!beans.containsKey(name)) {
      throw new IllegalArgumentException("unknown name: " + name);
    }
    return beans.get(name);
  }

  private Object getPropertyValue(final Object bean, final String property) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    // handle nested properties
    if (property.contains(".")) {
      final int pos = property.indexOf('.');
      final String beanPropertyName = property.substring(0, pos);
      final String nestedProperty = property.substring(pos + 1);

      final Object nestedBean = getPropertyValue(bean, beanPropertyName);
      return getPropertyValue(nestedBean, nestedProperty);
    }

    // get bean info
    final BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

    // loop through all properties
    for (final PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
      if (prop.getName().equals(property)) {
        final Method readMethod = prop.getReadMethod();
        final Object value = readMethod.invoke(bean);
        return value;
      }
    }

    // check for special size property for arrays and lists
    if (property.equals("size")) {
      if (bean instanceof List) {
        return ((List<?>) bean).size();
      } else if (bean.getClass().isArray()) {
        return ((Object[]) bean).length;
      }
    }

    throw new IllegalArgumentException("unknown property: " + property);
  }

  /**
   * @param string
   *          Binding
   * @return Value
   * @throws IntrospectionException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public Object getValueFromBinding(final String binding) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    final int pos = binding.indexOf('.');
    final String name = binding.substring(0, pos);
    final String property = binding.substring(pos + 1);
    final Object bean = getBindig(name);
    final Object value = getPropertyValue(bean, property);
    return value;
  }

  /**
   * @param binding
   * @param valueFormatter
   * @return Value
   * @throws IntrospectionException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public Object getValueFromBinding(final String binding, final ValueFormatter valueFormatter) throws IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, IntrospectionException {
    Object result = getValueFromBinding(binding);

    if (valueFormatter != null) {
      result = valueFormatter.format(result);
    }

    return result;

  }

  public boolean hasBinding(final String name) {
    return beans.containsKey(name);
  }

  public void removeBindig(final String name) {
    if (!beans.containsKey(name)) {
      throw new IllegalArgumentException("unknown name: " + name);
    }
    beans.remove(name);
  }
}
