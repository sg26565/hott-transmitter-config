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
package com.swtxml.tinydom;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.internal.NamespaceDefinition;
import com.swtxml.util.parser.ParseException;

public final class Tag implements IAdaptable {

  private final INamespaceDefinition                                         namespaceDefinition;
  private final ITagDefinition                                               tagDefinition;
  private final Map<INamespaceDefinition, Map<IAttributeDefinition, String>> attributeMap;

  private final Tag                                                          parent;
  private List<Tag>                                                          children;

  private final String                                                       locationInfo;
  private final List<Object>                                                 adapterObjects = new ArrayList<Object>();

  public Tag(final INamespaceDefinition namespaceDefinition, final ITagDefinition tagDefinition,
      final Map<INamespaceDefinition, Map<IAttributeDefinition, String>> attributeMap, final Tag parent, final String locationInfo) {
    this.namespaceDefinition = namespaceDefinition;
    this.tagDefinition = tagDefinition;
    this.parent = parent;
    this.locationInfo = locationInfo;
    this.attributeMap = attributeMap;
    if (!isRoot()) {
      if (this.parent.children == null) {
        this.parent.children = new ArrayList<Tag>();
      }
      this.parent.children.add(this);
    }
  }

  public void addAdapter(final Object adapterObject) {
    assertNotNull("adapterObject", adapterObject);
    // TODO: check for conflicts
    adapterObjects.add(adapterObject);
  }

  @SuppressWarnings("unchecked")
  public <T> T getAdapter(final Class<T> type) {
    for (final Object adapterObject : adapterObjects) {
      if (type.isAssignableFrom(adapterObject.getClass())) {
        return (T) adapterObject;
      }
    }
    return (T) (type.isAssignableFrom(getClass()) ? this : null);
  }

  public <T> List<T> getAdapterChildren(final Class<T> type) {
    final List<T> results = new ArrayList<T>();
    for (final Tag tag : getChildren()) {
      final T adapted = tag.getAdapter(type);
      if (adapted != null) {
        results.add(adapted);
      }
    }
    return results;
  }

  public final <T> T getAdapterParent(final Class<T> type) {
    return parent != null ? parent.getAdapter(type) : null;
  }

  public final <T> T getAdapterParentRecursive(final Class<T> type) {
    final T match = getAdapterParent(type);
    if (match != null) {
      return match;
    }
    if (parent != null) {
      return parent.getAdapterParent(type);
    }
    return null;
  }

  /**
   * Returns a list of all definitions from the given namespace which specified
   * set for this tag.
   */
  public Collection<IAttributeDefinition> getAttributes(final INamespaceDefinition namespace) {
    final Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
    if (attributes != null) {
      return attributes.keySet();
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * Returns the attribute value by namespace and attribute definition.
   */
  public String getAttributeValue(final INamespaceDefinition namespace, final IAttributeDefinition attribute) {
    final Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
    return attributes != null ? attributes.get(attribute) : null;
  }

  /**
   * Returns the attribute value by namespace and attribute name.
   */
  public String getAttributeValue(final NamespaceDefinition namespace, final String attributeName) {
    if (getNamespaceDefinition().equals(namespace)) {
      return getAttributeValue(attributeName);
    } else {
      return getAttributeValue(namespace, namespace.getForeignAttribute(attributeName));
    }
  }

  /**
   * Returns the attribute value by name for attributes having the same
   * namespace as the tag.
   */
  public String getAttributeValue(final String attributeName) {
    return getAttributeValue(getNamespaceDefinition(), getTagDefinition().getAttribute(attributeName));
  }

  public List<Tag> getChildren() {
    if (children != null) {
      return children;
    } else {
      return Collections.emptyList();
    }
  }

  public String getLocationInfo() {
    return locationInfo;
  }

  public String getName() {
    return tagDefinition.getName();
  }

  public INamespaceDefinition getNamespaceDefinition() {
    return namespaceDefinition;
  }

  public Tag getParent() {
    return parent;
  }

  public ITagDefinition getTagDefinition() {
    return tagDefinition;
  }

  public boolean isRoot() {
    return getParent() == null;
  }

  @Override
  public String toString() {
    return "Tag[" + tagDefinition + "]";
  }

  /**
   * Calls the given visitors for this node and its children elements. All
   * children elements are visited before going up again (Depth first).
   */
  public void visitDepthFirst(final ITagVisitor... visitors) {
    for (final ITagVisitor visitor : visitors) {
      try {
        visitor.visit(this);
      } catch (final Exception e) {
        throw new ParseException(getLocationInfo() + e.getMessage(), e);
      }
    }
    for (final Tag child : getChildren()) {
      child.visitDepthFirst(visitors);
    }
  }

}
