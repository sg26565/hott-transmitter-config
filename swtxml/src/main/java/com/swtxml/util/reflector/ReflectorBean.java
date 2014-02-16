/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.util.reflector;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * ReflectorBean represents all properties (getter+setter or public field) of a
 * java bean class.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class ReflectorBean {

  private final Collection<IReflectorProperty> properties;

  private final Class<?>                       type;

  public ReflectorBean(final Class<?> type, final PublicFields publicFields) {
    assertNotNull("type", type);
    this.type = type;
    properties = Collections.unmodifiableCollection(Reflector.findPublicProperties(type, publicFields));
  }

  public String getName() {
    return type.getSimpleName();
  }

  public Collection<IReflectorProperty> getProperties() {
    return properties;
  }

  public IReflectorProperty getProperty(final String propertyName) {
    for (final IReflectorProperty property : properties) {
      if (propertyName.equals(property.getName())) {
        return property;
      }
    }
    return null;
  }

  public Class<?> getType() {
    return type;
  }

  @Override
  public String toString() {
    return "ReflectorBean[" + getName() + "]";
  }

}
