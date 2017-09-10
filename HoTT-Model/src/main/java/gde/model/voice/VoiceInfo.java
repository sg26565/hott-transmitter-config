package gde.model.voice;

public class VoiceInfo {
    private final VDFType vdfType;
    private final int flashSize;
    private final int sectorCount;
    private final int sectorSize;
    private final int voiceVersion;
    private final int infoSize;
    private final int maxDataSize;

    public VoiceInfo(final VDFType vdfType, final int flashSize, final int sectorCount, final int sectorSize, final int voiceVersion, final int infoSize,
            final int maxDataSize) {
        this.vdfType = vdfType;
        this.flashSize = flashSize;
        this.sectorCount = sectorCount;
        this.sectorSize = sectorSize;
        this.voiceVersion = voiceVersion;
        this.infoSize = infoSize;
        this.maxDataSize = maxDataSize;
    }

    public int getFlashSize() {
        return flashSize;
    }

    public int getInfoSize() {
        return infoSize;
    }

    public int getMaxDataSize() {
        return maxDataSize;
    }

    public int getSectorCount() {
        return sectorCount;
    }

    public int getSectorSize() {
        return sectorSize;
    }

    public VDFType getVdfType() {
        return vdfType;
    }

    public int getVoiceVersion() {
        return voiceVersion;
    }
}
