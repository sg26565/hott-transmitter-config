import java.util.ResourceBundle;

import gde.model.enums.Function;

public class Utf8Test {
	public static void main(final String[] args) {
		final Function function = Function.Elevator;

		System.out.printf("%s = %s\n", function.name(), function.toString());

		final ResourceBundle bundle = ResourceBundle.getBundle(Function.class.getName());
		final String string = bundle.getString(function.name());

		for (final byte b : string.getBytes()) {
			System.out.printf("%c (%02x)\n", new Character((char) b), b);
		}

		for (final byte b : "ö".getBytes()) {
			System.out.printf("%c (%02x)\n", new Character((char) b), b);
		}

		for (final byte b : "\u00f6".getBytes()) {
			System.out.printf("%c (%02x)\n", new Character((char) b), b);
		}
	}
}
