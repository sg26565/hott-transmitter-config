package gde.model.voice;

public class VoiceFileInfo {
    private final VDFType vdfType;
    private final int size;
    private final int baseAddress;
    private final int index;
    private final int reverseIndex;
    private final int sampleRate;
    private final String name;

    public VoiceFileInfo(final VDFType vdfType, final int size, final int baseAddress, final int index, final int reverseIndex, final int sampleRate,
            final String name) {
        this.vdfType = vdfType;
        this.size = size;
        this.baseAddress = baseAddress;
        this.index = index;
        this.reverseIndex = reverseIndex;
        this.sampleRate = sampleRate;
        this.name = name;
    }

    public int getBaseAddress() {
        return baseAddress;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getReverseIndex() {
        return reverseIndex;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getSize() {
        return size;
    }

    public VDFType getVdfType() {
        return vdfType;
    }
}
