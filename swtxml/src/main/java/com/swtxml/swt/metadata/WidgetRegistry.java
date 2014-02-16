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
package com.swtxml.swt.metadata;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.DefinitionException;
import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IFilter;
import com.swtxml.util.lang.ResourceUtils;
import com.swtxml.util.parser.ParseException;

public class WidgetRegistry {

  private final Map<String, String[]> widgetStylesByClassName = new HashMap<String, String[]>();

  public WidgetRegistry() {
    final String widgetClasses = ResourceUtils.toString(ResourceUtils.getClassResource(this.getClass(), "txt"));
    for (final String line : StringUtils.split(widgetClasses, "\n")) {
      final String[] parts = StringUtils.split(line, '=');
      final String className = parts[0].trim();
      final String[] allowedStyles = parts.length > 1 ? StringUtils.split(parts[1].trim(), ", ") : new String[] {};
      widgetStylesByClassName.put(className, allowedStyles);
    }
  }

  public Class<?> getAllowedParentType(final Class<? extends Widget> type) {
    final Constructor<?> constructor = getWidgetConstructor(type);
    return constructor.getParameterTypes()[0];

  }

  public Collection<String> getAllowedStylesFor(Class<?> widgetClass) {
    final Set<String> allowedStyles = new HashSet<String>();
    do {
      final String[] styles = widgetStylesByClassName.get(widgetClass.getName());
      if (styles == null) {
        throw new ParseException("Unknown widget super class: " + widgetClass);
      }
      allowedStyles.addAll(Arrays.asList(styles));
      widgetClass = widgetClass.getSuperclass();
    } while (widgetClass.getSuperclass() != null && !Object.class.equals(widgetClass));
    return allowedStyles;
  }

  @SuppressWarnings("unchecked")
  public Class<? extends Widget> getWidgetClass(final String className) {
    try {
      return (Class<? extends Widget>) Class.forName(className);
    } catch (final ClassNotFoundException e) {
      throw new DefinitionException(e);
    }
  }

  public Collection<String> getWidgetClassNames() {
    return widgetStylesByClassName.keySet();
  }

  @SuppressWarnings("rawtypes")
  public Constructor getWidgetConstructor(final Class<? extends Widget> widgetClass) {
    return CollectionUtils.find(Arrays.asList(widgetClass.getConstructors()), new IFilter<Constructor>() {

      @Override
      public boolean match(final Constructor constructor) {
        return constructor.getParameterTypes().length == 2 && constructor.getParameterTypes()[1] == Integer.TYPE;
      }

    });
  }
}
