package gde.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
  private static final String         BUNDLE_NAME     = "gde.messages.messages";              //$NON-NLS-1$
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  public static String getString(final String key, final Object... args) {
    String string;
    try {
      string = RESOURCE_BUNDLE.getString(key);
    } catch (final MissingResourceException e) {
      return '!' + key + '!';
    }

    if (args != null && args.length > 0 && string.contains("%")) {
      string = String.format(string, args);
    }

    return string;
  }

  private Messages() {}
}
