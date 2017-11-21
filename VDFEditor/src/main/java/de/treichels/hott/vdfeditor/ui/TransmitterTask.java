package de.treichels.hott.vdfeditor.ui;

import de.treichels.hott.HoTTSerialPort;
import gde.model.serial.UpdateHandler;
import gde.model.voice.VoiceFile;
import javafx.concurrent.Task;

public abstract class TransmitterTask extends Task<VoiceFile> implements UpdateHandler {
    private HoTTSerialPort port;

    TransmitterTask(final String title) {
        updateTitle(title);
    }

    public HoTTSerialPort getPort() {
        return port;
    }

    public void setPort(final HoTTSerialPort port) {
        this.port = port;
    }

    @Override
    public void update(final int step, final int count) {
        updateMessage(String.format("%d %%", step * 100 / count));
        updateProgress(step, count);
    }
}
