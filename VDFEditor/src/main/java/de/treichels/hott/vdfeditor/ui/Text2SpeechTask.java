package de.treichels.hott.vdfeditor.ui;

import java.io.File;

import gde.model.voice.VoiceRSS;
import javafx.concurrent.Task;

final class Text2SpeechTask extends Task<File> {

    private String text;
    private String language;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return this.language;
    }

    @Override
    protected File call() throws Exception {
        return new VoiceRSS().convert(text, language);
    }
}
