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

import gde.model.AbstractBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This aspect adds property change support to every property.
 * 
 * @author oli
 */
public aspect PropertyChangeAspect {
  // execution of all public set methods with one argument that inherit from
  // AbstractBase
  pointcut setter(): execution(public void gde.model.AbstractBase+.set*(*));

  void around(AbstractBase target, Object newValue): target(target) && args(newValue) && setter() {
    final String setMethodName = thisJoinPointStaticPart.getSignature().getName();
    final String propertyName = setMethodName.substring(3, 4).toLowerCase() + setMethodName.substring(4);

    if (target.hasListeners(propertyName)) {
      // get old value from getter method
      final String getMethodName;
      if (newValue instanceof Boolean) {
        getMethodName = "is" + setMethodName.substring(3);
      } else {
        getMethodName = "get" + setMethodName.substring(3);
      }

      final Object oldValue;
      try {
        final Method getMethod = target.getClass().getMethod(getMethodName);
        oldValue = getMethod.invoke(target);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      } catch (SecurityException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      }

      // set new value
      proceed(target, newValue);

      // fire change event
      target.firePropertyChange(propertyName, oldValue, newValue);

    } else {
      // set new value
      proceed(target, newValue);

      // no listeners for this property - no need to fire event
    }
  }
}
