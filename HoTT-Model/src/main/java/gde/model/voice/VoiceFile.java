package gde.model.voice;

import java.util.ArrayList;
import java.util.List;

import gde.model.enums.TransmitterType;
import gde.model.voice.Announcements.VDFType;

/**
 * Representation of a void data file (.vdf).
 *
 * @author oliver.treichel@gmx.de
 */
public class VoiceFile {
    private final List<VoiceData> voiceData;
    private TransmitterType transmitterType;
    private VDFType vdfType;

    public VoiceFile() {
        vdfType = VDFType.User;
        transmitterType = TransmitterType.mc32;
        voiceData = new ArrayList<>();
    }

    public VoiceFile(final VDFType vdfType, final TransmitterType transmitterType, final List<VoiceData> voiceData) {
        this.vdfType = vdfType;
        this.transmitterType = transmitterType;
        this.voiceData = voiceData;
    }

    public int getDataSize() {
        return voiceData.stream().mapToInt(vd -> vd.getRawData().length).sum();
    }

    public TransmitterType getTransmitterType() {
        return transmitterType;
    }

    public VDFType getVdfType() {
        return vdfType;
    }

    public int getVoiceCount() {
        return voiceData.size();
    }

    public List<VoiceData> getVoiceData() {
        return voiceData;
    }

    public void setTransmitterType(final TransmitterType transmitterType) {
        this.transmitterType = transmitterType;
    }

    public void setVdfType(final VDFType type) {
        vdfType = type;
    }
}
