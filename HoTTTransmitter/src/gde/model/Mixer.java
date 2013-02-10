package gde.model;

public class Mixer {
	private Channel from;
	private MixerInputType inputType;
	private int offset;
	private Channel to;
	private int travelHigh;
	private int travelLow;
	private MixerType type;

	public Channel getFrom() {
		return from;
	}

	public MixerInputType getInputType() {
		return inputType;
	}

	public int getOffset() {
		return offset;
	}

	public Channel getTo() {
		return to;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public MixerType getType() {
		return type;
	}

	public void setFrom(final Channel from) {
		this.from = from;
	}

	public void setInputType(final MixerInputType inputType) {
		this.inputType = inputType;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public void setTo(final Channel to) {
		this.to = to;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}

	public void setType(final MixerType type) {
		this.type = type;
	}
}
