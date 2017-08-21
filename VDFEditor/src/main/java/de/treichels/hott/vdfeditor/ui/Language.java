package de.treichels.hott.vdfeditor.ui;

import java.util.ResourceBundle;

public enum Language {
    DE, EN, FR, IT, ES, NL, CZ, RU, CN, KR, JP;

    /** @return the locale-dependent languages */
    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}
