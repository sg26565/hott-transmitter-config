package gde.model;

import gde.model.enums.FailSafeMode;
import gde.model.enums.Function;
import gde.model.enums.TrainerMode;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class Channel {
	private int center;
	private FailSafeMode failSafeMode;
	private int failSafePosition;
	private Function function;
	private int limitHigh;
	private int limitLow;
	private final String number;
	private int outputChannel;
	private boolean reverse;
	private TrainerMode trainerMode;
	private int travelHigh;
	private int travelLow;
	private boolean virtual = false;

	public Channel(final int number) {
		this.number = Integer.toString(number);
	}

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

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}

	public int getOutputChannel() {
		return outputChannel;
	}

	public TrainerMode getTrainerMode() {
		return trainerMode;
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

	public void setTrainerMode(final TrainerMode trainerMode) {
		this.trainerMode = trainerMode;
	}

	public void setTravelHigh(final int travelHigh) {
		this.travelHigh = travelHigh;
	}

	public void setTravelLow(final int travelLow) {
		this.travelLow = travelLow;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(final boolean virtual) {
		this.virtual = virtual;
	}
}
