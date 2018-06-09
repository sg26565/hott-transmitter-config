import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class TestClassLoader {
    public static void main(String[] args) throws NoSuchMethodException {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
    }
}
