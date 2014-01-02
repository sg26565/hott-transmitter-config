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
package com.swtxml.util.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.swtxml.util.reflector.ReflectorException;

public class CollectionUtils {

  /**
   * Returns a new collection containing the result from function.apply(element)
   * for all elements from collection.
   */
  @SuppressWarnings("unchecked")
  public static <FROM, TO> Collection<TO> collect(final Collection<? extends FROM> collection, final IFunction<FROM, TO> function) {
    final Collection<TO> resultList = createCollection(collection);
    for (final FROM a : collection) {
      resultList.add(function.apply(a));
    }
    return resultList;
  }

  /**
   * Returns a new list containing the result from function.apply(element) for
   * all elements from list.
   */
  public static <FROM, TO> List<TO> collect(final List<? extends FROM> list, final IFunction<FROM, TO> function) {
    final List<TO> resultList = new ArrayList<TO>();
    for (final FROM a : list) {
      resultList.add(function.apply(a));
    }
    return resultList;
  }

  @SuppressWarnings("rawtypes")
  private static Collection createCollection(final Collection original) {
    if (original instanceof Set) {
      return new HashSet();
    }
    if (original instanceof Collection) {
      return new ArrayList();
    }
    throw new ReflectorException("Unknown collection type: " + original.getClass());
  }

  /**
   * Returns the first element from iterable for which filter.match(element)
   * returned true.
   */
  public static <A> A find(final Iterable<? extends A> iterable, final IFilter<A> filter) {
    for (final A a : iterable) {
      if (filter.match(a)) {
        return a;
      }
    }
    return null;
  }

  /**
   * Returns a new collection containing all elements from collection for which
   * filter.match(element) returned true.
   */
  @SuppressWarnings("unchecked")
  public static <A> Collection<A> select(final Collection<? extends A> collection, final IFilter<A> filter) {
    final Collection<A> resultList = createCollection(collection);
    for (final A a : collection) {
      if (filter.match(a)) {
        resultList.add(a);
      }
    }
    return resultList;
  }

  /**
   * Returns a comma-separated String of the collection toString values
   * alphabetically sorted by value.
   */
  public static String sortedToString(final Collection<?> collection) {
    final List<String> strings = new ArrayList<String>(collect(collection, Functions.TO_STRING));
    Collections.sort(strings);
    return StringUtils.join(strings, ", ");
  }

}