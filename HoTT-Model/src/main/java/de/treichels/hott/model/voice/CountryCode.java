package de.treichels.hott.model.voice;

import java.util.ResourceBundle;
import java.util.stream.Stream;

public enum CountryCode {
    GLOBAL(0), US(1), KR(2), CN(3), JP(4);

    public static CountryCode forCode(final int countryCode) {
        return Stream.of(CountryCode.values()).filter(c -> c.getCode() == countryCode).findFirst().orElse(GLOBAL);
    }

    private final int code;

    CountryCode(final int code) {
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
