package gde.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * @author oli@treichels.de
 */
public class CurvePoint {
	private boolean enabled;
	private String number;
	private int value;

	public CurvePoint(final int number) {
		this.number = Integer.toString(number);
	}

	@XmlAttribute
	@XmlID
	public String getNumber() {
		return number;
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

	public void setNumber(final String number) {
		this.number = number;
	}

	public void setValue(final int value) {
		this.value = value;
	}
}
