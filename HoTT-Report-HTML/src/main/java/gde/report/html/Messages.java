package gde.report.html;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Make localized messages availalbe to FreeMarker.
 * 
 * @author oli@treichels.de
 */
public class Messages implements TemplateHashModel {
  @Override
  public TemplateModel get(final String key) throws TemplateModelException {
    final String message = gde.messages.Messages.getString(key);
    return new SimpleScalar(message);
  }

  @Override
  public boolean isEmpty() throws TemplateModelException {
    return false;
  }
}
