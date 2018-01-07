package de.treichels.hott.messages;

import org.jetbrains.annotations.NonNls;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    @NonNls
    private static final String BUNDLE_NAME = "de.treichels.hott.messages.messages";
    @NonNls
    private static final String[] PACKAGE_NAMES = { "", "de.treichels.hott.model.", "de.treichels.hott.model.enums." };
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString(final String key, final Object... args) {
        if (key == null) return null;

        String result = null;

        if (RESOURCE_BUNDLE.containsKey(key))
            result = RESOURCE_BUNDLE.getString(key);
        else {
            final int pos = key.lastIndexOf('.');
            if (pos > 0 && pos < key.length() - 1) {
                final String base = key.substring(0, pos);
                final String name = key.substring(pos + 1);

                for (final String prefix : PACKAGE_NAMES)
                    try {
                        final ResourceBundle bundle = ResourceBundle.getBundle(prefix + base);
                        result = bundle.getString(name);
                        break;
                    } catch (final MissingResourceException ignored) {
                    }
            }
        }

        if (result == null) result = '!' + key + '!';

        if (args != null && args.length > 0 && result.contains("%")) result = String.format(result, args);

        return result;
    }

    private Messages() {}
}
