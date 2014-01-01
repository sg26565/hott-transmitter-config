package model;

public class Channel {
  private int     limitHigh;
  private int     limitLow;
  private int     number;
  private boolean reverse;
  private int     travelHigh;
  private int     travelLow;

  public int getLimitHigh() {
    return limitHigh;
  }

  public int getLimitLow() {
    return limitLow;
  }

  public int getNumber() {
    return number;
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

  public void setLimitHigh(final int limitHigh) {
    this.limitHigh = limitHigh;
  }

  public void setLimitLow(final int limitLow) {
    this.limitLow = limitLow;
  }

  public void setNumber(final int number) {
    this.number = number;
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

  @Override
  public String toString() {
    return "Channel [number=" + number + "]";
  }
}
