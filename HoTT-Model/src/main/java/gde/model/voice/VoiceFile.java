package gde.model.voice;

import gde.model.enums.TransmitterType;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Representation of a void data file (.vdf).
 *
 * @author oliver.treichel@gmx.de
 */
public class VoiceFile {
    private final class DirtyBinding extends BooleanBinding {
        @Override
        protected boolean computeValue() {
            return !VoiceFile.this.equals(savedState);
        }
    }

    private final class SizeBinding extends IntegerBinding {
        @Override
        protected int computeValue() {
            return voiceData.size();
        }
    }

    private final VoiceFile savedState;
    private final ObjectProperty<CountryCode> country = new SimpleObjectProperty<>(CountryCode.GLOBAL);
    private final ObjectProperty<TransmitterType> transmitterType = new SimpleObjectProperty<>();
    private final ObjectProperty<VDFType> vdfType = new SimpleObjectProperty<>();
    private final IntegerProperty vdfVersion = new SimpleIntegerProperty();
    private final ObservableList<VoiceData> voiceData;
    private final IntegerBinding sizeBinding = new SizeBinding();
    private final BooleanBinding dirtyProperty = new DirtyBinding();

    private VoiceFile() {
        voiceData = FXCollections.observableArrayList();
        savedState = null;
    }

    public VoiceFile(final VDFType vdfType, final TransmitterType transmitterType, final int vdfVersion, final int countryCode) {
        this(vdfType, transmitterType, vdfVersion, countryCode, FXCollections.observableArrayList());
    }

    public VoiceFile(final VDFType vdfType, final TransmitterType transmitterType, final int vdfVersion, final int countryCode,
            final ObservableList<VoiceData> voiceData) {
        setVdfType(vdfType);
        setTransmitterType(transmitterType);
        setVdfVersion(vdfVersion);
        setCountry(CountryCode.forCode(countryCode));
        this.voiceData = voiceData;
        this.voiceData.addListener((ListChangeListener<VoiceData>) c -> sizeBinding.invalidate());
        savedState = new VoiceFile();
        savedState.copy(this);

        country.addListener((a, b, c) -> dirtyProperty.invalidate());
        this.transmitterType.addListener((a, b, c) -> dirtyProperty.invalidate());
        this.vdfType.addListener((a, b, c) -> dirtyProperty.invalidate());
        this.vdfVersion.addListener((a, b, c) -> dirtyProperty.invalidate());
        this.voiceData.addListener((ListChangeListener<VoiceData>) c -> dirtyProperty.invalidate());
    }

    public void clean() {
        savedState.copy(this);
        dirtyProperty.invalidate();
    }

    public void copy(final VoiceFile other) {
        setCountry(other.getCountry());
        setTransmitterType(other.getTransmitterType());
        setVdfType(other.getVdfType());
        setVdfVersion(other.getVdfVersion());
        voiceData.clear();
        voiceData.addAll(other.getVoiceData());
    }

    public ObjectProperty<CountryCode> countryProperty() {
        return country;
    }

    public BooleanBinding dirtyProperty() {
        return dirtyProperty;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final VoiceFile other = (VoiceFile) obj;
        if (country.get() == null) {
            if (other.country.get() != null) return false;
        } else if (!country.get().equals(other.country.get())) return false;
        if (transmitterType.get() == null) {
            if (other.transmitterType.get() != null) return false;
        } else if (!transmitterType.get().equals(other.transmitterType.get())) return false;
        if (vdfType.get() == null) {
            if (other.vdfType.get() != null) return false;
        } else if (!vdfType.get().equals(other.vdfType.get())) return false;
        if (vdfVersion.get() != other.vdfVersion.get()) return false;
        if (voiceData == null) {
            if (other.voiceData != null) return false;
        } else if (!voiceData.equals(other.voiceData)) return false;
        return true;
    }

    public CountryCode getCountry() {
        return country.get();
    }

    public int getDataSize() {
        return voiceData.stream().mapToInt(vd -> vd.getRawData().length).sum();
    }

    public TransmitterType getTransmitterType() {
        return transmitterType.get();
    }

    public VDFType getVdfType() {
        return vdfType.get();
    }

    public int getVdfVersion() {
        return vdfVersion.get();
    }

    public int getVoiceCount() {
        return voiceData.size();
    }

    public ObservableList<VoiceData> getVoiceData() {
        return voiceData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (country.get() == null ? 0 : country.get().hashCode());
        result = prime * result + (transmitterType.get() == null ? 0 : transmitterType.get().hashCode());
        result = prime * result + (vdfType.get() == null ? 0 : vdfType.get().hashCode());
        result = prime * result + vdfVersion.get();
        result = prime * result + (voiceData == null ? 0 : voiceData.hashCode());
        return result;
    }

    public void setCountry(final CountryCode country) {
        this.country.set(country);
    }

    public void setTransmitterType(final TransmitterType transmitterType) {
        this.transmitterType.set(transmitterType);
    }

    public void setVdfType(final VDFType vdfType) {
        this.vdfType.set(vdfType);
    }

    public void setVdfVersion(final int vdfVersion) {
        this.vdfVersion.set(vdfVersion);
    }

    public ObjectProperty<TransmitterType> transmitterTypeProperty() {
        return transmitterType;
    }

    public ObjectProperty<VDFType> vdfTypeProperty() {
        return vdfType;
    }

    public IntegerProperty vdfVersionProperty() {
        return vdfVersion;
    }

    public IntegerBinding voiceCountProperty() {
        return sizeBinding;
    }
}
