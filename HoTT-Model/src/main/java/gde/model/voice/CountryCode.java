package gde.model.voice;

import java.util.ResourceBundle;
import java.util.stream.Stream;

public enum CountryCode {
    eu(0), us(1), kr(2), cn(3), jp(4);

    public static CountryCode forCode(final int countryCode) {
        return Stream.of(CountryCode.values()).filter(c -> c.getCode() == countryCode).findFirst().orElse(eu);
    }

    private final int code;

    private CountryCode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}
