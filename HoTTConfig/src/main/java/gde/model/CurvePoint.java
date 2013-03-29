package gde.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class CurvePoint {
	private final String number;
	private boolean enabled;
	private int value;

	public CurvePoint(final int number) {
		this.number = Integer.toString(number);
	}

	public int getValue() {
		return value;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
	}
}
