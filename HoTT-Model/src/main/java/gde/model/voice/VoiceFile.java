package gde.model.voice;

import java.util.List;

public class VoiceFile {
    public static final int VDF_CODE = 0x105f12cd;
    public static final int SYSTEM_TYPE1 = 0x000000fe;
    public static final int SYSTEM_TYPE2 = 0x00000001;
    public static final int USER_TYPE1 = 0x0000000e;
    public static final int USER_TYPE2 = 0x000000f1;
    public static final int SYSTEM_DATA_OFFSET_MC28 = 0x8000;
    public static final int SYSTEM_DATA_OFFSET_MC32 = 0x3000;
    public static final int SYSTEM_DATA_SIZE_MC28 = 0x2f6fce;
    public static final int SYSTEM_DATA_SIZE_MC32 = 0x196728;
    public static final int USER_DATA_OFFSET = 0x1000;
    public static final int USER_DATA_SIZE = 0x10fb6;

    private final int code;
    private final List<VoiceData> voideData;
    private final int dataOffset;
    private final int dataSize;
    private final int transmitterType;
    private final int type1;
    private final int type2;
    private final int vdfVersion;

    private final int voiceCount;

    public VoiceFile(final int code, final int type1, final int type2, final int voiceCount, final int dataSize, final int dataOffset, final int vdfVersion,
            final int transmitterType, final List<VoiceData> voiceData) {
        this.code = code;
        this.type1 = type1;
        this.type2 = type2;
        this.voiceCount = voiceCount;
        this.dataSize = dataSize;
        this.dataOffset = dataOffset;
        this.vdfVersion = vdfVersion;
        this.transmitterType = transmitterType;
        voideData = voiceData;
    }

    public int getCode() {
        return code;
    }

    public int getDataOffset() {
        return dataOffset;
    }

    public int getDataSize() {
        return dataSize;
    }

    public int getTransmitterType() {
        return transmitterType;
    }

    public int getType1() {
        return type1;
    }

    public int getType2() {
        return type2;
    }

    public int getVdfVersion() {
        return vdfVersion;
    }

    public int getVoiceCount() {
        return voiceCount;
    }

    public List<VoiceData> getVoiceData() {
        return voideData;
    }
}
