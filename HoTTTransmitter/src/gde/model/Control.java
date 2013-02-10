package gde.model;

public class Control {
	private Channel channel;
	private ControlMode mode;
	private int travelHigh;
	private int travelLow;

	public Channel getChannel() {
		return channel;
	}

	public ControlMode getMode() {
		return mode;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public void setChannel(final Channel channel) {
		this.channel = channel;
	}

	public void setMode(final ControlMode mode) {
		this.mode = mode;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}
}
