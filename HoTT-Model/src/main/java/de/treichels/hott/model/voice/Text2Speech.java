package de.treichels.hott.model.voice;

import java.io.File;

public interface Text2Speech {
    File convert(String text, String language) throws Exception;
}
