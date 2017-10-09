package de.treichels.hott.vdfeditor.ui;

import de.treichels.hott.HoTTSerialPort;
import gde.model.HoTTException;
import gde.model.voice.VoiceFile;

final class LoadVoiceFileTask extends TransmitterTask {
    private final boolean user;

    public LoadVoiceFileTask(final String title, final boolean user) {
        super(title);
        this.user = user;
    }

    @Override
    protected VoiceFile call() throws Exception {
        try (HoTTSerialPort port = getPort()) {
            port.open();
            port.turnRfOutOff();
            port.delay();

            if (user && port.getVoiceInfo(false).getVoiceVersion() == 2000) throw new HoTTException("NoUerVoiceFiles");

            return port.loadVoiceFile(user, this);
        }
    }
}
