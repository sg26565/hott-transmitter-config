package gde.model;

public class Channel {
	private int center;
	private FailSafeMode failSafeMode;
	private int failSafePosition;
	private Function function;
	private int limitHigh;
	private int limitLow;
	private int outputChannel;
	private boolean reverse;
	private int travelHigh;
	private int travelLow;
	private TrainerMode trainerMode;

	public int getCenter() {
		return center;
	}

	public FailSafeMode getFailSafeMode() {
		return failSafeMode;
	}

	public int getFailSafePosition() {
		return failSafePosition;
	}

	public Function getFunction() {
		return function;
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

	public void setFailSafeMode(final FailSafeMode failSafeMode) {
		this.failSafeMode = failSafeMode;
	}

	public void setFailSafePosition(final int failSafePosition) {
		this.failSafePosition = failSafePosition;
	}

	public void setFunction(final Function function) {
		this.function = function;
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

	public TrainerMode getTrainerMode() {
		return trainerMode;
	}

	public void setTrainerMode(final TrainerMode trainerMode) {
		this.trainerMode = trainerMode;
	}
}
