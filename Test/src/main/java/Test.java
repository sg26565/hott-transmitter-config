import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Test {
	public static void main(final String[] args) throws Exception {
		FileHandler handler = new FileHandler("/home/oli/test.log");
		handler.setFormatter(new SimpleFormatter());
		Logger log = Logger.getLogger(Test.class.getName());
		log.addHandler(handler);
		log.log(Level.INFO, "hallo");
	}
}