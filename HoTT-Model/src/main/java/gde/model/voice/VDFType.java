package gde.model.voice;

import java.util.ResourceBundle;

public enum VDFType {
    System, User;

    @Override
    public String toString() {
        return ResourceBundle.getBundle(getClass().getName()).getString(name());
    }
}