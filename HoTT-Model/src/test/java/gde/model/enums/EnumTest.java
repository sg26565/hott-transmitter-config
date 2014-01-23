package gde.model.enums;

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
    final Enumeration<URL> enums = Thread.currentThread().getContextClassLoader().getResources("gde/model/enums");

    while (enums.hasMoreElements()) {
      final File dir = new File(enums.nextElement().toURI());

      if (dir.isDirectory()) {
        final File[] files = dir.listFiles();

        for (final File file : files) {
          final String fileName = file.getName();

          if (fileName.endsWith(".class")) {
            final Class<?> clazz = Class.forName("gde.model.enums." + fileName.substring(0, fileName.length() - 6));

            if (clazz.isEnum()) {
              final Field[] fields = clazz.getDeclaredFields();
              final Method toStringMethod = clazz.getMethod("toString");
              final Method nameMethod = clazz.getMethod("name");

              for (final Field field : fields) {
                if ((field.getModifiers() & Modifier.STATIC & Modifier.PUBLIC) == 0) {
                  // skip non-public, non-static fields
                  continue;
                }

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
