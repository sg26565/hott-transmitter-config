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
package com.swtxml.swt;

import java.util.Locale;

import org.eclipse.swt.widgets.Composite;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.definition.INamespaceResolver;
import com.swtxml.events.internal.SwtEvents;
import com.swtxml.events.visitor.AddEventListeners;
import com.swtxml.extensions.DefaultNamespaceResolver;
import com.swtxml.i18n.ILabelTranslator;
import com.swtxml.i18n.ResourceBundleLabelTranslator;
import com.swtxml.resources.ClassResource;
import com.swtxml.resources.IDocumentResource;
import com.swtxml.swt.byid.ByIdInjector;
import com.swtxml.swt.visitor.BuildWidgets;
import com.swtxml.swt.visitor.CollectIds;
import com.swtxml.swt.visitor.SetAttributes;
import com.swtxml.swt.visitor.TagContextVisitor;
import com.swtxml.tinydom.ITagVisitor;
import com.swtxml.tinydom.Tag;
import com.swtxml.tinydom.TinyDomParser;
import com.swtxml.util.context.Context;

public class SwtXmlParser extends TinyDomParser implements IAdaptable {

  private static INamespaceResolver getSwtNamespaceResolver() {
    return new DefaultNamespaceResolver();
  }

  private final Composite               rootComposite;
  private final Object                  view;
  private SwtResourceManager            resourceManager;

  private ResourceBundleLabelTranslator labelTranslator;

  public SwtXmlParser(final Composite rootComposite, final IDocumentResource resource, final Object view) {
    super(getSwtNamespaceResolver(), resource);
    this.rootComposite = rootComposite;
    this.view = view;
  }

  public SwtXmlParser(final Composite rootComposite, final Object view) {
    this(rootComposite, ClassResource.coLocated(view.getClass(), "swtxml"), view);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <A> A getAdapter(final Class<A> adapterClass) {
    final Object result = super.getAdapter(adapterClass);
    if (result != null) {
      return (A) result;
    }

    if (SwtResourceManager.class.isAssignableFrom(adapterClass)) {
      if (resourceManager == null) {
        resourceManager = new SwtResourceManager(rootComposite);
      }
      return (A) resourceManager;
    }

    if (ILabelTranslator.class.isAssignableFrom(adapterClass)) {
      if (labelTranslator == null) {
        labelTranslator = new ResourceBundleLabelTranslator(document, Locale.getDefault());
      }
      return (A) labelTranslator;
    }

    return null;
  }

  @Override
  protected void onParseCompleted(final Tag root) {
    final CollectIds ids = new CollectIds();
    final ITagVisitor buildWidgets = new TagContextVisitor(new BuildWidgets(rootComposite));
    final ITagVisitor setAttributes = new TagContextVisitor(new SetAttributes());

    root.visitDepthFirst(ids);

    Context.runWith(new Runnable() {
      public void run() {
        Context.addAdapter(ids);
        Context.addAdapter(SwtXmlParser.this);
        root.visitDepthFirst(buildWidgets);
        root.visitDepthFirst(setAttributes);
      }
    });

    if (view != null) {
      root.visitDepthFirst(new AddEventListeners(view, SwtEvents.getNamespace()));
      new ByIdInjector().inject(view, ids);
    }
  }

}