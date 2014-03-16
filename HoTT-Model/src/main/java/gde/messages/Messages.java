package gde.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
  private static final String         BUNDLE_NAME     = "gde.messages.messages";              //$NON-NLS-1$
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  private static String _get(final String key) {
    String result;

    try {
      result = RESOURCE_BUNDLE.getString(key);
    } catch (final MissingResourceException e) {
      result = null;
    }

    return result;
  }

  public static String getString(final String key, final Object... args) {
    if (key == null) {
      return null;
    }

    String result = _get(key);

    if (result == null) {
      result = '!' + key + '!';
    }

    if (args != null && args.length > 0 && result.contains("%")) {
      result = String.format(result, args);
    }

    return result;
  }

  public static boolean hasStringFor(final String key) {
    return _get(key) != null;
  }

  private Messages() {}
}
