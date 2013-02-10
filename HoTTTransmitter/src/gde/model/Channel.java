package gde.model;

public class Channel {
	private Function function;
	private int center;
	private int limitHigh;
	private int limitLow;
	private int outputChannel;
	private boolean reverse;
	private int travelHigh;
	private int travelLow;

	public int getCenter() {
		return center;
	}

	public int getLimitHigh() {
		return limitHigh;
	}

	public int getLimitLow() {
		return limitLow;
	}

	public int getOutputChannel() {
		return outputChannel;
	}

	public int getTravelHigh() {
		return travelHigh;
	}

	public int getTravelLow() {
		return travelLow;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setCenter(final int center) {
		this.center = center;
	}

	public void setLimitHigh(final int limitHigh) {
		this.limitHigh = limitHigh;
	}

	public void setLimitLow(final int limitLow) {
		this.limitLow = limitLow;
	}

	public void setOutputChannel(final int outputChannel) {
		this.outputChannel = outputChannel;
	}

	public void setReverse(final boolean reverse) {
		this.reverse = reverse;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}
}
