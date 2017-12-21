package de.treichels.hott.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import org.junit.Test;

public class EnumTest {
    @Test
    public void test() throws IOException, URISyntaxException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final Enumeration<URL> enums = Thread.currentThread().getContextClassLoader().getResources("gde/model/enums"); //$NON-NLS-1$

        while (enums.hasMoreElements()) {
            final File dir = new File(enums.nextElement().toURI());

            if (dir.isDirectory()) {
                final File[] files = dir.listFiles();

                for (final File file : files) {
                    final String fileName = file.getName();

                    if (fileName.endsWith(".class")) { //$NON-NLS-1$
                        final Class<?> clazz = Class.forName("gde.model.enums." + fileName.substring(0, fileName.length() - 6)); //$NON-NLS-1$

                        if (clazz.isEnum()) {
                            final Field[] fields = clazz.getDeclaredFields();
                            final Method toStringMethod = clazz.getMethod("toString"); //$NON-NLS-1$
                            final Method nameMethod = clazz.getMethod("name"); //$NON-NLS-1$

                            for (final Field field : fields) {
                                if ((field.getModifiers() & Modifier.STATIC & Modifier.PUBLIC) == 0) // skip non-public, non-static fields
                                    continue;

                                final Object object = field.get(null);
                                final String name = (String) nameMethod.invoke(object);
                                final String string = (String) toStringMethod.invoke(object);

                                assertEquals(field.getName(), name);
                                assertNotNull(string);
                            }
                        }
                    }
                }
            }
        }
    }
}
