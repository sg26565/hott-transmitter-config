package model;

import java.util.List;

public class Transmitter {
  private String        camelCaseString;
  private List<Channel> channels;
  private String[]      fooStrings;
  private String        name;
  private String        vendor;
  private String        version;

  public String getCamelCaseString() {
    return camelCaseString;
  }

  public List<Channel> getChannels() {
    return channels;
  }

  public String[] getFooStrings() {
    return fooStrings;
  }

  public String getName() {
    return name;
  }

  public String getVendor() {
    return vendor;
  }

  public String getVersion() {
    return version;
  }

  public void setCamelCaseString(final String camelCaseString) {
    this.camelCaseString = camelCaseString;
  }

  public void setChannels(final List<Channel> channels) {
    this.channels = channels;
  }

  public void setFooStrings(final String[] fooStrings) {
    this.fooStrings = fooStrings;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setVendor(final String vendor) {
    this.vendor = vendor;
  }

  public void setVersion(final String version) {
    this.version = version;
  }
}
