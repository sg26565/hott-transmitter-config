package de.treichels.hott.vdfeditor.ui;

import de.treichels.hott.HoTTSerialPort;
import gde.model.voice.VoiceFile;

final class SendVoiceFileTask extends TransmitterTask {
    private final VoiceFile voiceFile;

    public SendVoiceFileTask(final String title, final VoiceFile voiceFile) {
        super(title);
        this.voiceFile = voiceFile;
    }

    @Override
    protected VoiceFile call() throws Exception {
        try (HoTTSerialPort port = getPort()) {
            if (port.isOpen()) port.close();
            port.open();
            port.sendVoiceFile(voiceFile, this);
            return voiceFile;
        }
    }
}
