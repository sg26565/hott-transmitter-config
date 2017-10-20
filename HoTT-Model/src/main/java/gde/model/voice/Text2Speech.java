package gde.model.voice;

import java.io.File;

public interface Text2Speech {
    public abstract File convert(String text, String language) throws Exception;
}
